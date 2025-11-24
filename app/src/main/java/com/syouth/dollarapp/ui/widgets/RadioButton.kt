package com.syouth.dollarapp.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.syouth.dollarapp.R

internal typealias OnClicked = () -> Unit

internal object RadioButton {

    @Composable
    fun Widget(
        model: WidgetModel,
        modifier: Modifier = Modifier,
        onClicked: OnClicked = {},
    ) = Box(modifier = Modifier.then(modifier).clickable { onClicked() }) {
        if (!model.selected) {
            Box(
                modifier = Modifier
                    .border(2.dp, MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {}
        } else {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = CircleShape,
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Image.Widget(
                    model = Image.WidgetModel.VectorResource(R.drawable.check, tint = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }

    data class WidgetModel(
        val selected: Boolean,
    )
}