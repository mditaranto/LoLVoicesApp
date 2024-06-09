package com.example.lolvoices.Components.BotonesUtils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

//Es una clase que extiende de Shape, y que se utiliza para darle forma a un box
class CustomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width - 60f, 0f)
        lineTo(size.width, size.height / 2)
        lineTo(size.width - 60f, size.height)
        lineTo(0f, size.height)
        close()
    })
}
