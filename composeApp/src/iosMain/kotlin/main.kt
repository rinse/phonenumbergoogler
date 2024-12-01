import androidx.compose.ui.window.ComposeUIViewController
import net.esnir.png.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
