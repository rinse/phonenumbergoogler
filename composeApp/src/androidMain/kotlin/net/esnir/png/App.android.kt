package net.esnir.png

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import net.esnir.png.di.androidModules
import net.esnir.png.di.mockModules

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Napier.base(DebugAntilog())
        val module = androidModules(this.applicationContext)
        setContent { App(module) }
    }
}

@Preview
@Composable
fun AppPreview() {
    val module = mockModules()
    App(module)
}
