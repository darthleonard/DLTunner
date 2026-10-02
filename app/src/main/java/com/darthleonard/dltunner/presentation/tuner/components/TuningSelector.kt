package com.darthleonard.dltunner.presentation.tuner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darthleonard.dltunner.domain.model.Tuning

/**
 * Dropdown selector for choosing active guitar tuning preset.
 */
@Composable
fun TuningSelector(
    selectedTuning: Tuning,
    availableTunings: List<Tuning>,
    onTuningSelected: (Tuning) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { expanded = true }
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = stringResource(id = selectedTuning.nameResId).uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "▼",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            availableTunings.forEach { tuning ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = tuning.nameResId),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (tuning.id == selectedTuning.id) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            fontWeight = if (tuning.id == selectedTuning.id) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onTuningSelected(tuning)
                        expanded = false
                    }
                )
            }
        }
    }
}
