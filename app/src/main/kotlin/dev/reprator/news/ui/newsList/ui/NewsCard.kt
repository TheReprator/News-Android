package dev.reprator.news.ui.newsList.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.ui.ComposeLocalWrapper
import dev.reprator.news.util.composeUtil.AppIcons.CLOCK
import dev.reprator.news.util.composeUtil.theme.ContrastAwareNewsTheme
import dev.reprator.news.util.composeUtil.theme.Dimens.ExtraSmallPadding
import dev.reprator.news.util.composeUtil.theme.Dimens.ExtraSmallPadding2
import dev.reprator.news.util.composeUtil.theme.Dimens.NewsCardSize
import dev.reprator.news.util.composeUtil.theme.Dimens.SmallIconSize
import dev.reprator.news.util.composeUtil.ui.DynamicAsyncImage

@Composable
fun NewsCard(
    article: ModalNews,
    onClick: (ModalNews) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.clickable { onClick(article) }) {
        DynamicAsyncImage(
            imageUrl = article.url,
            contentDescription = null)

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = ExtraSmallPadding)
                .height(NewsCardSize)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium.copy(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.width(ExtraSmallPadding2))
                Icon(
                    imageVector = CLOCK.first,
                    contentDescription = stringResource(CLOCK.second),
                    modifier = Modifier.size(SmallIconSize),
                )
                Spacer(modifier = Modifier.width(ExtraSmallPadding))
                Text(article.publishedAt.toString(),
                    style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewArticleCard() {
    ComposeLocalWrapper {
        ContrastAwareNewsTheme {
            NewsCard(
                article = ModalNews(
                    category = "Tech",
                    author = "Vikram Singh",
                    content = "Vikram Content",
                    description = "Vikram description",
                    publishedAt = System.currentTimeMillis(),
                    source = "BBC",
                    title = "Her train broke down. Her phone died. And then she met her Saver in a",
                    url = "",
                    urlToImage = "https://ichef.bbci.co.uk/live-experience/cps/624/cpsprodpb/11787/production/_124395517_bbcbreakingnewsgraphic.jpg"
                ), {})
        }
    }
}