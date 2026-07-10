---
mode: primary
---

## Graphify — regla obligatoria

Este proyecto tiene un knowledge graph en `graphify-out/`. **Antes de explorar cualquier archivo o responder cualquier pregunta sobre el código**, ejecuta siempre:

```
graphify query "<pregunta>"
```

- Usa `graphify path "<A>" "<B>"` para relaciones entre símbolos.
- Usa `graphify explain "<concepto>"` para conceptos concretos.
- Lee `graphify-out/wiki/index.md` para navegación amplia.
- Lee `graphify-out/GRAPH_REPORT.md` solo para revisión de arquitectura general.
- Solo accede a archivos directamente si el grafo no devuelve suficiente contexto.
- Después de modificar código, ejecuta `graphify update .` para mantener el grafo actualizado.

No omitas este paso bajo ninguna circunstancia, incluso si la tarea parece simple.
