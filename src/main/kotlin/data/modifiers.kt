package data

import androidx.compose.ui.graphics.Color

data class SizeModifierData(
    val size: Int = 0
)

enum class AvailableShapes {
    Circle,
    RoundedCorner,
    CutCorner,
    Rectangle
}

data class BackgroundModifierData(
    val color: Color,
    val shape: AvailableShapes = AvailableShapes.Rectangle,
    val corner: Int = 0
)

data class BorderModifierData(
    val width: Int = 0,
    val color: Color,
    val shape: AvailableShapes = AvailableShapes.Rectangle,
    val corner: Int = 0
)

data class PaddingModifierData(
    val all: Int = 0
)

data class ShadowModifierData(
    val elevation: Int = 0,
    val shape: AvailableShapes = AvailableShapes.Rectangle,
    val corner: Int = 0
)