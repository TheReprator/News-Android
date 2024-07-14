package dev.reprator.news.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.reprator.news.util.composeUtil.LocalWindowSizeClass
import dev.reprator.news.util.composeUtil.theme.ContrastAwareNewsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalWindowSizeClass provides calculateWindowSizeClass()
            ) {
                ContrastAwareNewsTheme {
                    NewsApp()
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun NewsAppPreviewTabletPortrait() {
    ComposeLocalWrapper {
        ContrastAwareNewsTheme {
            NewsApp()
        }
    }
}

@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@Composable
fun NewsAppPreviewDesktop() {
    ComposeLocalWrapper {
        ContrastAwareNewsTheme {
            NewsApp()
        }
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 1100)
@Composable
fun NewsAppPreviewDesktopPortrait() {
    ComposeLocalWrapper {
        ContrastAwareNewsTheme {
            NewsApp()
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ComposeLocalWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalWindowSizeClass provides calculateWindowSizeClass(),
        content = content
    )
}