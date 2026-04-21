import SwiftUI
import DebugMenu

struct DebugMenuHostView: UIViewControllerRepresentable {
    let factory: IosDebugMenuControllerFactory
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
