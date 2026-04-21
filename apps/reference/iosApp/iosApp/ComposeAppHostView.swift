import SwiftUI
import ComposeApp

struct ComposeAppHostView: UIViewControllerRepresentable {
    private let factory = ComposeAppControllerFactory()

    func makeUIViewController(context: Context) -> UIViewController {
        factory.makeViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
