import SwiftUI
import ComposeApp

struct DebugMenuHostView: UIViewControllerRepresentable {
    let factory: ComposeDebugMenuControllerFactory
    let onClose: () -> Void
    let onRestartRequired: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        factory.makeViewController(
            onClose: onClose,
            onRestartRequired: onRestartRequired
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
