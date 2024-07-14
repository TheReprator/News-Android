package dev.reprator.news.util.composeUtil.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.reprator.news.util.composeUtil.AppIcons


@Composable
fun AppViewError(errorMessage: String, modifier: Modifier = Modifier, onReload: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {
        Text(errorMessage, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onReload) {
            Row {
                val buttonData = AppIcons.Refresh
                val description = stringResource(AppIcons.Refresh.second)

                Icon(imageVector = buttonData.first, contentDescription = description)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = description)
            }
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewStateError() {
    AppViewError("An error occurred")
}

@Preview
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewStateErrorFullScreen() {
    val m = Modifier.fillMaxSize()
    AppViewError("An error occurred", modifier = m)
}