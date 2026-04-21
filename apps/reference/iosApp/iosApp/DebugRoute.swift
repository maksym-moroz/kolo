import Foundation

enum DebugRoute {
    case debugMenu

    init?(url: URL) {
        guard url.scheme == "kolo" else { return nil }
        guard url.host == "debug" else { return nil }
        guard url.path == "/menu" else { return nil }

        self = .debugMenu
    }
}
