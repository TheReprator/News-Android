package dev.reprator.news.presentation.newsListDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.presentation.newsDetail.NewsDetailScreen
import dev.reprator.news.presentation.newsList.ui.NewsListScreen
import dev.reprator.news.util.composeUtil.LocalWindowAdaptiveInfo


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

@Composable
internal fun NewsListDetailScreen(viewModel: NewsPaneViewModel = hiltViewModel()) {

    val selectedNews by viewModel.selectedNews.collectAsStateWithLifecycle()

    NewsListDetailScreen(
        selectedNews = selectedNews,
        onNewsClick = viewModel::onNewsClick,
        updateBookMark = viewModel::updateBookMarks
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun NewsListDetailScreen(
    selectedNews: ModalNews?,
    onNewsClick: (ModalNews) -> Unit,
    updateBookMark: () -> Unit,
) {
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(LocalWindowAdaptiveInfo.current),

        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
            ThreePaneScaffoldDestinationItem<ModalNews>(ListDetailPaneScaffoldRole.Detail).takeIf {
                selectedNews != null
            },
        ),
    )

    BackHandler(listDetailNavigator.canNavigateBack()) {
        listDetailNavigator.navigateBack()
    }

    fun onNewsClickShowDetailPane(selectedNews: ModalNews) {
        onNewsClick(selectedNews)
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, selectedNews)
    }

    ListDetailPaneScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        value = listDetailNavigator.scaffoldValue,
        directive = listDetailNavigator.scaffoldDirective,
        listPane = {
            AnimatedPane {
                NewsListScreen(
                    onNewsClick = ::onNewsClickShowDetailPane,
                    highlightSelectedNews = listDetailNavigator.isDetailPaneVisible(),
                )
            }
        },
        detailPane = {
            if (null == selectedNews)
                null
            else
                AnimatedPane {
                    NewsDetailScreen(selectedNews, !listDetailNavigator.isListPaneVisible(), {
                        if (listDetailNavigator.canNavigateBack()) {
                            listDetailNavigator.navigateBack()
                        }
                    }, {
                        updateBookMark()
                    })
                }

        },
    )
}
