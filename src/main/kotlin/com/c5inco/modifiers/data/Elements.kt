package com.c5inco.modifiers.data

import androidx.compose.ui.text.TextStyle

enum class AvailableElements {
    Box,
    Column,
    Row,
}

data class BoxElementData(
    val contentAlignment: AvailableContentAlignments = AvailableContentAlignments.TopStart,
)

data class ColumnElementData(
    val verticalArrangement: AvailableVerticalArrangements = AvailableVerticalArrangements.Top,
    val verticalSpacing: Int = 0,
    val horizontalAlignment: AvailableHorizontalAlignments = AvailableHorizontalAlignments.Start
)

data class RowElementData(
    val horizontalArrangement: AvailableHorizontalArrangements = AvailableHorizontalArrangements.Start,
    val horizontalSpacing: Int = 0,
    val verticalAlignment: AvailableVerticalAlignments = AvailableVerticalAlignments.Top
)

data class ElementModel(
    val type: AvailableElements,
    val data: Any,
    val themeAware: Boolean = true
)

data class EmojiChildData(
    val emoji: String
)

data class TextChildData(
    val text: String,
    val style: TextStyle,
    val alpha: AvailableContentAlphas = AvailableContentAlphas.High
)

data class ImageChildData(
    val imagePath: String
)

enum class AvailableContentAlphas {
    High,
    Medium,
    Disabled
}