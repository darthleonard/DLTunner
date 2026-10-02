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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darthleonard.dltunner.domain.model.GuitarString
import com.darthleonard.dltunner.domain.model.TunerState

/**
 * Visual indicator row displaying all six guitar strings.
 * Emphasizes active playing string and retains saved [TunerState] status colors.
 */
@Composable
fun StringIndicators(
    strings: List<GuitarString>,
    activeString: GuitarString?,
    modifier: Modifier = Modifier,
    stringStates: Map<Int, TunerState> = emptyMap(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Strings ordered 6 to 1
        strings.sortedByDescending { it.stringNumber }.forEach { string ->
            val isActive = activeString?.stringNumber == string.stringNumber
            val savedState = stringStates[string.stringNumber] ?: TunerState.NO_SIGNAL

            val (targetBgColor, targetBorderColor, targetTextColor) = when (savedState) {
                TunerState.IN_TUNE -> Triple(
                    Color(0xFF00E676).copy(alpha = 0.22f),
                    Color(0xFF00E676),
                    Color(0xFF00E676)
                )
                TunerState.TOO_LOW -> Triple(
                    Color(0xFFFF9100).copy(alpha = 0.22f),
                    Color(0xFFFF9100),
                    Color(0xFFFF9100)
                )
                TunerState.TOO_HIGH -> Triple(
                    Color(0xFFFF1744).copy(alpha = 0.22f),
                    Color(0xFFFF1744),
                    Color(0xFFFF1744)
                )
                TunerState.NO_SIGNAL -> Triple(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Active string gets bright white/primary outline accent
            val effectiveBorderColor = if (isActive) {
                Color.White
            } else {
                targetBorderColor
            }

            val backgroundColor by animateColorAsState(
                targetValue = targetBgColor,
                animationSpec = tween(durationMillis = 200),
                label = "stringBgColor"
            )

            val borderColor by animateColorAsState(
                targetValue = effectiveBorderColor,
                animationSpec = tween(durationMillis = 200),
                label = "stringBorderColor"
            )

            val textColor by animateColorAsState(
                targetValue = targetTextColor,
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
                            width = if (isActive) 3.dp else 1.5.dp,
                            color = borderColor,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = string.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = if (isActive && (savedState == TunerState.NO_SIGNAL)) Color.White else textColor
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = string.stringNumber.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal
                    ),
                    color = if (isActive) Color.White else targetTextColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
