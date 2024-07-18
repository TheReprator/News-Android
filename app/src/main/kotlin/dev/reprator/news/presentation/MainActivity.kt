package dev.reprator.news.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.reprator.news.util.composeUtil.LocalWindowAdaptiveInfo
import dev.reprator.news.util.composeUtil.theme.ContrastAwareNewsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalWindowAdaptiveInfo provides currentWindowAdaptiveInfo()
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

@Composable
fun ComposeLocalWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalWindowAdaptiveInfo provides currentWindowAdaptiveInfo(),
        content = content
    )
}