package dev.reprator.news.ui.newsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.reprator.news.ui.composeUtil.AppIcons
import dev.reprator.news.ui.composeUtil.CollectSideEffect
import dev.reprator.news.ui.newsList.archModel.Event

@Composable
internal fun NewsListScreen(
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsListViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    CollectSideEffect(viewModel.eventFlow) {
        when (it) {
            is Event.ToastMessage -> {

            }
            else -> {

            }
        }
    }
}

@Composable
fun AppViewError(errorMessage: String, onReload:() -> Unit = {}, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(errorMessage)
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

@Composable
fun AppViewLoader(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun PreviewStateLoader() {
    AppViewLoader()
}

@Preview
@Composable
private fun PreviewStateError() {
    AppViewError("An error occurrred")
}

@Preview
@Composable
private fun PreviewStateErrorFullScreen() {
    val m = Modifier.fillMaxSize()
    AppViewError("An error occurrred", modifier = m )
}