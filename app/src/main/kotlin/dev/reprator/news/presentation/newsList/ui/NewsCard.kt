package dev.reprator.news.presentation.newsList.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.reprator.news.modal.ModalNews
import dev.reprator.news.util.composeUtil.AppIcons.CLOCK
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
    isSelected: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.inversePrimary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { onClick(article) },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
        ) {
            DynamicAsyncImage(
                imageUrl = "https://i.natgeofe.com/n/1f58c33f-56ba-4982-b924-f642e75d8393/Tower11_3x4.jpg",
                //imageUrl = article.urlToImage,
                contentDescription = null,
                Modifier
                    .clip(shape = RoundedCornerShape(10.dp)))

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(horizontal = ExtraSmallPadding)
                    .height(NewsCardSize)
            ) {
                Text(
                    text = article.id.title,
                    style = MaterialTheme.typography.bodyMedium.copy(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.id.source,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(ExtraSmallPadding2))
                    Icon(
                        imageVector = CLOCK.first,
                        contentDescription = stringResource(CLOCK.second),
                        modifier = Modifier.size(SmallIconSize),
                    )
                    Spacer(modifier = Modifier.width(ExtraSmallPadding))
                    Text(
                        article.publishedAt.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}