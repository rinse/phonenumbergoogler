package net.esnir.png

import androidx.compose.runtime.Composable
import net.esnir.png.compose.RootContent
import net.esnir.png.theme.AppTheme
import org.kodein.di.DI
import org.kodein.di.compose.withDI

@Composable
internal fun App(platformSpecificModule: DI.Module) = AppTheme {
    withDI(platformSpecificModule) {
        RootContent()
    }
}
