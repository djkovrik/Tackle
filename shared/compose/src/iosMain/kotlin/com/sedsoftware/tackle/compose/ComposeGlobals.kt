package com.sedsoftware.tackle.compose

import platform.UIKit.UIImage
import platform.UIKit.UIViewController

object ComposeGlobals {
    var controllerFactory: ((UIImage) -> UIViewController)? = null
}
