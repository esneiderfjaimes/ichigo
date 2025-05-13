package com.red.code015.ui.pages.masteries_and_chests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.red.code015.domain.Profile
import com.red.code015.ui.common.*
import com.red.code015.ui.components.AsyncImage
import com.red.code015.utils.Coil

@Composable
fun ProfileSection(
    profile: Profile,
    onProfileClick: () -> Unit,
) {
    MyCard(Modifier, minHeight = Dp.Unspecified) {
        val sizeSummonerIcon = 50.dp
        Column(Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick)
            .padding(margin2), Arrangement.spacedBy(margin)) {
            Row(horizontalArrangement = Arrangement.spacedBy(margin4),
                verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.height(sizeSummonerIcon)) {
                    AsyncImage(modifier = Modifier
                        .size(sizeSummonerIcon)
                        .clip(RoundedCornerShape(12.dp)),
                        model = Coil.urlProfileIcon(profile.profileIconID),
                        loading = {
                            Box(Modifier.size(sizeSummonerIcon)) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        append(profile.name)
                        withStyle(style = SpanStyle(
                            fontSize = typography.bodySmall.fontSize,
                            color = colorScheme.onBackground.copy(0.5f))) {
                            append(" " + profile.platformID.name)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    style = typography.bodyLarge
                )

                MyIcon(Icons.Rounded.ArrowForwardIos)
            }
        }
    }
}
