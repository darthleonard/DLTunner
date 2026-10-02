package com.darthleonard.dltunner.presentation.tuner.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darthleonard.dltunner.R
import com.darthleonard.dltunner.domain.model.TunerState
import kotlin.math.abs

/**
 * Visual needle/gauge meter displaying deviation in cents (-50 to +50).
 */
@Composable
fun PitchMeter(
    cents: Double?,
    state: TunerState,
    modifier: Modifier = Modifier
) {
    val targetCents = (cents ?: 0.0).toFloat().coerceIn(-50f, 50f)
    val animatedCents by animateFloatAsState(
        targetValue = targetCents,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.8f),
        label = "animatedCents"
    )

    val activeColor = when (state) {
        TunerState.NO_SIGNAL -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        TunerState.TOO_LOW -> Color(0xFFFF9100)
        TunerState.IN_TUNE -> Color(0xFF00E676)
        TunerState.TOO_HIGH -> Color(0xFFFF1744)
    }

    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val inZoneColor = Color(0xFF00E676).copy(alpha = 0.35f)
    val centerMarkColor = Color(0xFF00E676)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            val width = size.width
            val height = size.height
            val centerY = height / 2f
            val startX = width * 0.08f
            val endX = width * 0.92f
            val trackWidth = endX - startX

            // 1. Draw main background track
            drawLine(
                color = trackColor,
                start = Offset(startX, centerY),
                end = Offset(endX, centerY),
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round
            )

            // 2. Draw in-tune target band (-3 to +3 cents)
            val inTuneCentsWidth = (6f / 100f) * trackWidth
            val inTuneStartX = startX + (47f / 100f) * trackWidth
            drawRect(
                color = inZoneColor,
                topLeft = Offset(inTuneStartX, centerY - 16.dp.toPx()),
                size = Size(inTuneCentsWidth, 32.dp.toPx())
            )

            // 3. Draw tick marks for -50, -25, 0, +25, +50
            val ticks = listOf(-50f, -25f, 0f, 25f, 50f)
            ticks.forEach { tickCents ->
                val fraction = (tickCents + 50f) / 100f
                val tickX = startX + fraction * trackWidth
                val isCenter = tickCents == 0f
                val tickHeight = if (isCenter) 24.dp.toPx() else 14.dp.toPx()

                drawLine(
                    color = if (isCenter) centerMarkColor else trackColor,
                    start = Offset(tickX, centerY - tickHeight / 2),
                    end = Offset(tickX, centerY + tickHeight / 2),
                    strokeWidth = if (isCenter) 3.dp.toPx() else 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 4. Draw active indicator needle/ball if signal present
            if (state != TunerState.NO_SIGNAL) {
                val fraction = (animatedCents + 50f) / 100f
                val indicatorX = startX + fraction * trackWidth

                // Vertical needle line
                drawLine(
                    color = activeColor,
                    start = Offset(indicatorX, centerY - 28.dp.toPx()),
                    end = Offset(indicatorX, centerY + 28.dp.toPx()),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Indicator ball
                drawCircle(
                    color = activeColor,
                    radius = 10.dp.toPx(),
                    center = Offset(indicatorX, centerY)
                )

                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = Offset(indicatorX, centerY)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Secondary text below meter
        val subtext = if (state == TunerState.NO_SIGNAL) {
            stringResource(id = R.string.instruction_play_string)
        } else if (cents != null) {
            val absCents = abs(cents)
            if (absCents <= 3.0) {
                stringResource(id = R.string.status_in_tune)
            } else {
                stringResource(id = R.string.cents_format, cents)
            }
        } else {
            stringResource(id = R.string.instruction_play_string)
        }

        Text(
            text = subtext,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            color = if (state == TunerState.NO_SIGNAL) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            } else {
                activeColor
            }
        )
    }
}
