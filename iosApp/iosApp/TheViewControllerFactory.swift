import Foundation
import UIKit
import SwiftUI
import ComposeApp

class TheViewControllerFactory : ViewControllerFactory {
    func makeController(view: UIImage) -> UIViewController {
        let image = Image(uiImage: view)
        return UIHostingController(rootView: image)
    }
}
