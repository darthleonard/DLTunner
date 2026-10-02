package com.darthleonard.dltunner.presentation.tuner.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darthleonard.dltunner.R
import com.darthleonard.dltunner.domain.model.TunerState

/**
 * Visual banner displaying current tuning status text in Spanish.
 */
@Composable
fun TunerStatus(
    state: TunerState,
    modifier: Modifier = Modifier
) {
    val (statusTextRes, targetColor) = when (state) {
        TunerState.NO_SIGNAL -> Pair(R.string.status_no_signal, MaterialTheme.colorScheme.onSurfaceVariant)
        TunerState.TOO_LOW -> Pair(R.string.status_too_low, Color(0xFFFF9100))
        TunerState.IN_TUNE -> Pair(R.string.status_in_tune, Color(0xFF00E676))
        TunerState.TOO_HIGH -> Pair(R.string.status_too_high, Color(0xFFFF1744))
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 200),
        label = "statusTextColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(animatedColor.copy(alpha = 0.15f))
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Text(
            text = stringResource(id = statusTextRes),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            ),
            color = animatedColor
        )
    }
}
