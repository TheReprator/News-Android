package dev.reprator.news.ui.newsList.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import dev.reprator.news.R

@Composable
fun CategoriesScreen() {
    /*    val tabItems = getAllArticleCategory()
        val mainState by mainViewModel.mainState.collectAsState()

        LaunchedEffect(key1 = mainState.selectedCategory) {
            mainViewModel.getArticlesByCategory("business")
        }

        if (mainState.isLoading) {
            LoadingScreen()
        }

        mainState.error?.let {
            ErrorScreen(error = it.toError())
        }*/

    val categoryArray = stringArrayResource(R.array.news_category)

}


@Composable
fun CategoryTab(
    tabList: List<String>,
    category: String,
    isSelected: Boolean = false,
    onFetchCategory: (String) -> Unit,
    pagerState: PagerState
) {
    val selectedIndex = pagerState.currentPage
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        divider = {
            Spacer(modifier = Modifier.height(5.dp))
        },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

    ) {
        tabList.forEachIndexed { index, s ->
            Tab(selected = pagerState.currentPage ==
                    index, onClick = {
            },
                text = {
                    Text(text = s)
                })
        }
    }
}

@Composable
fun HorizontalPagerContent(pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 20.dp), pageSpacing = 10.dp
    ) { page ->
        //NewsCard()
    }
}