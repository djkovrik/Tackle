import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        ViewControllerFactoryCompanion().shared = TheViewControllerFactory()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
