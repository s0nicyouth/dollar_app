package com.syouth.dollarapp.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil3.compose.AsyncImage

internal object Image {

    @Composable
    fun Widget(
        model: WidgetModel,
        modifier: Modifier = Modifier,
    ) {
        when (model) {
            is WidgetModel.RemoteImage -> {
                AsyncImage(
                    modifier = Modifier
                        .then(modifier),
                    model = model.url,
                    contentDescription = model.url,
                )
            }

            is WidgetModel.VectorResource -> {
                AsyncImage(
                    modifier = Modifier
                        .then(modifier),
                    model = model.res,
                    contentDescription = model.res.toString(),
                    colorFilter = model.tint?.let(ColorFilter::tint),
                )
            }
        }
    }

    sealed interface WidgetModel {
        data class RemoteImage(val url: String) : WidgetModel
        data class VectorResource(val res: Int, val tint: Color? = null) : WidgetModel
    }
}