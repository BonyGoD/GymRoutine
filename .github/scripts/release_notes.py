import os
import re
import subprocess
import sys

SECTIONS = [
    ("feat", "Novedades"),
    ("feature", "Novedades"),
    ("fix", "Arreglos"),
    ("perf", "Rendimiento"),
    ("refactor", "Interno"),
    ("docs", "Documentación"),
]

CONVENTIONAL = re.compile(r"(\w+)(\([^)]*\))?(!?):\s*(.+)")


def git(*args: str) -> str:
    return subprocess.run(
        ["git", *args],
        capture_output=True,
        text=True,
        check=True,
        encoding="utf-8",
        errors="replace",
    ).stdout.strip()


def read(path: str) -> str:
    try:
        with open(path, encoding="utf-8") as handle:
            return handle.read()
    except OSError:
        return ""


def find(pattern: str, text: str, default: str = "?") -> str:
    match = re.search(pattern, text)
    return match.group(1).strip() if match else default


def latest_tag() -> str:
    tags = git("tag", "--sort=-v:refname").splitlines()
    for tag in tags:
        if re.match(r"^v\d", tag):
            return tag
    return ""


def build_notes(rng: str, new: str, prev: str, repo: str) -> str:
    buckets: dict[str, list[str]] = {key: [] for key, _ in SECTIONS}
    breaking: list[str] = []
    seen: set[str] = set()

    for line in git("log", "--no-merges", "--format=%s", rng).splitlines():
        match = CONVENTIONAL.match(line.strip())
        if not match:
            continue
        kind, _, bang, message = match.groups()
        kind = kind.lower()
        message = message.strip()
        if message.lower() in seen:
            continue
        seen.add(message.lower())
        entry = message[0].upper() + message[1:]
        if bang:
            breaking.append(entry)
        if kind in buckets:
            buckets[kind].append(entry)

    out: list[str] = []

    store = read(f".github/release-notes/{new}.md").strip()
    if store:
        out += [store, "", "---", ""]

    if breaking:
        out.append("### ⚠️ Cambios que rompen compatibilidad\n")
        out += [f"- {entry}" for entry in breaking]
        out.append("")

    for title in dict.fromkeys(title for _, title in SECTIONS):
        entries = [entry for key, section in SECTIONS if section == title for entry in buckets[key]]
        if entries:
            out.append(f"### {title}\n")
            out += [f"- {entry}" for entry in entries]
            out.append("")

    if not breaking and not any(buckets.values()):
        out += [f"- Release version v{new}", ""]

    gradle = read("androidApp/build.gradle.kts")
    pbx = read("iosApp/iosApp.xcodeproj/project.pbxproj")
    version_code = find(r"versionCode\s*=\s*(\d+)", gradle)
    ios_version = find(r"MARKETING_VERSION = ([^;]+);", pbx)
    ios_build = find(r"CURRENT_PROJECT_VERSION = ([^;]+);", pbx)

    out += [
        "---",
        "",
        "| Plataforma | Versión |",
        "|---|---|",
        f"| Android | `{new}` · versionCode `{version_code}` |",
        f"| iOS | `{ios_version}` · build `{ios_build}` |",
    ]

    if prev:
        out += ["", f"**Full Changelog**: https://github.com/{repo}/compare/v{prev}...v{new}"]

    return "\n".join(out)


def main() -> None:
    sys.stdout.reconfigure(encoding="utf-8")

    gradle = read("androidApp/build.gradle.kts")
    if not gradle:
        raise SystemExit("Ejecuta esto desde la raíz del repo: no veo androidApp/build.gradle.kts")

    new = os.environ.get("NEW_VERSION") or find(r'versionName\s*=\s*"([^"]+)"', gradle, "")
    repo = os.environ.get("REPO") or "BonyGoD/GymRoutine"

    rng = sys.argv[1] if len(sys.argv) > 1 else os.environ.get("RANGE", "")
    prev = os.environ.get("PREV_VERSION", "")

    if not rng:
        tag = latest_tag()
        if not tag:
            raise SystemExit("No hay ningún tag v*; pasa el rango a mano")
        rng = f"{tag}..HEAD"
        prev = tag.lstrip("v")
        print(f"# rango: {rng}", file=sys.stderr)

    print(build_notes(rng, new, prev, repo))


if __name__ == "__main__":
    main()
