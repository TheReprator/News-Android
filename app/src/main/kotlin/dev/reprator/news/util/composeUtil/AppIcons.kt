package dev.reprator.news.util.composeUtil

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import dev.reprator.news.R

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object AppIcons {
    val Refresh = Icons.Default.Refresh to R.string.retry
    val CLOCK = Icons.Default.AccessTime to R.string.publistedAt
}
