package com.nei.ichigo.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
/**
 * Custom Preview annotation that centralizes common preview configurations.
 * Applies Light/Dark mode and adds a landscape variant.
 */
@PreviewLightDark
// @Preview(name = "Landscape", device = "spec:width=640dp,height=360dp,dpi=480")
annotation class IchigoPreview

/**
 * Provider that wraps previews with the [IchigoTheme] and [AsyncImagePreviewProvider].
 */
class IchigoPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        IchigoThemePreview {
            content()
            /*         AsyncImagePreviewProvider(
                     //    width = 1215 / 2,
                     //    height = 717 / 2
                     ) {
                         content()
                     }*/
        }
    }
}
