import SwiftUI
import Firebase
import GoogleSignIn
import SignInKMPSwift
import AdMobKMPSwift
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        CrashlyticsInitializerKt.configureCrashlytics()
        _ = SignInCallbackHelper.shared
        return true
    }

    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
        return GIDSignIn.sharedInstance.handle(url)
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        AppModuleKt.doInitKoin(config: { _ in })
        AdMobKMPBridge.start()
        AdsInitializerKt.configureAds()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
