package com.example.lolvoices.Components.BotonesUtils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

//Es una clase que extiende de Shape, y que se utiliza para darle forma a un box
class CustomShapeStats : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        moveTo(60f, 0f) // Move to right 60f to create the left "peak"
        lineTo(0f, size.height / 2) // Draw to the middle of the left side
        lineTo(60f, size.height) // Draw to the bottom left
        lineTo(size.width, size.height) // Draw to the bottom right
        lineTo(size.width, 0f) // Draw to the top right
        close() // Close the path
    })
}
