package com.syouth.dollarapp.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal object TextWithImage {
    @Composable
    fun Widget(
        model: WidgetModel,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val boxModified = if (model.withImageBackground) {
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(10.dp)
                    ).size(32.dp)
            } else Modifier
            Box(
                modifier = Modifier
                    .then(boxModified),
                contentAlignment = Alignment.Center,
            ) {
                Image.Widget(
                    model = model.image,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(75)),
                )
            }
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = model.text,
                fontSize = 16.sp,
                color = model.textColor ?: MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

    data class WidgetModel(
        val text: String,
        val image: Image.WidgetModel,
        val textColor: Color? = null,
        val withImageBackground: Boolean = false,
    )
}