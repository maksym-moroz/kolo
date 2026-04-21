import SwiftUI
import DebugMenu

@main
struct iOSApp: App {
    @State private var isDebugMenuPresented = false
    @State private var showsRestartRequiredAlert = false
    private let debugMenuFactory = IosDebugMenuControllerFactory()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    #if DEBUG
                    guard let route = DebugRoute(url: url) else { return }

                    switch route {
                    case .debugMenu:
                        isDebugMenuPresented = true
                    }
                    #endif
                }
                .sheet(isPresented: $isDebugMenuPresented) {
                    DebugMenuHostView(
                        factory: debugMenuFactory,
                        onClose: { isDebugMenuPresented = false },
                        onRestartRequired: {
                            isDebugMenuPresented = false
                            showsRestartRequiredAlert = true
                        }
                    )
                }
                .alert("Restart Required", isPresented: $showsRestartRequiredAlert) {
                    Button("OK", role: .cancel) {}
                } message: {
                    Text("Environment changes are persisted. Relaunch the app to apply them.")
                }
        }
    }
}
