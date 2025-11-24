package com.syouth.dollarapp.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.syouth.dollarapp.R
import com.syouth.dollarapp.ui.theme.White

typealias OnSwitchClicked = () -> Unit

internal object SwitchSides {
    @Composable
    fun Widget(
        onSwitchClicked: OnSwitchClicked,
    ) {
        Box(
            modifier = Modifier
                .clickable { onSwitchClicked() }
                .background(
                    color = MaterialTheme.colorScheme.surfaceBright,
                    shape = CircleShape,
                ).size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image.Widget(
                model = Image.WidgetModel.VectorResource(R.drawable.arrow_down_long, tint = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}