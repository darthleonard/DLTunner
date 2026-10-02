package com.darthleonard.dltunner.presentation.tuner.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darthleonard.dltunner.domain.model.GuitarString

/**
 * Visual indicator row displaying all six guitar strings.
 * Emphasizes the automatically detected active string.
 */
@Composable
fun StringIndicators(
    strings: List<GuitarString>,
    activeString: GuitarString?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Strings ordered 6 to 1
        strings.sortedByDescending { it.stringNumber }.forEach { string ->
            val isActive = activeString?.stringNumber == string.stringNumber

            val backgroundColor by animateColorAsState(
                targetValue = if (isActive) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                animationSpec = tween(durationMillis = 200),
                label = "stringBgColor"
            )

            val borderColor by animateColorAsState(
                targetValue = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                },
                animationSpec = tween(durationMillis = 200),
                label = "stringBorderColor"
            )

            val textColor by animateColorAsState(
                targetValue = if (isActive) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                animationSpec = tween(durationMillis = 200),
                label = "stringTextColor"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .border(
                            width = if (isActive) 2.5.dp else 1.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = string.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                            fontSize = 18.sp
                        ),
                        color = textColor
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = string.stringNumber.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
