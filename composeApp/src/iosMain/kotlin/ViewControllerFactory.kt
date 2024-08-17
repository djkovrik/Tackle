import com.sedsoftware.tackle.compose.ComposeGlobals
import platform.UIKit.UIImage
import platform.UIKit.UIViewController

interface ViewControllerFactory {
    companion object {
        var shared: ViewControllerFactory? = null
            set(value) {
                field = value
                value?.let {
                    ComposeGlobals.controllerFactory = it::makeController
                }
            }
    }

    fun makeController(view: UIImage): UIViewController
}
