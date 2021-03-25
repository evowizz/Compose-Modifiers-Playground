package com.c5inco.modifiers.ui

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.c5inco.modifiers.data.*
import com.c5inco.modifiers.utils.chunk
import java.awt.Cursor

@Composable
fun ColorInput(colorValue: Color, onValueChange: (Color) -> Unit) {
    val availableColors = listOf(
        Color.Black,
        Color.Gray,
        Color.White,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta,
        Color.Transparent,
    )

    var expanded by remember { mutableStateOf(false) }
    val swatchSize = 24
    val horizontalPadding = 6

    Box() {
        ColorSwatch(swatchSize, colorValue, onClick = { expanded = true })
        DropdownMenu(
            modifier = Modifier.padding(horizontal = horizontalPadding.dp),
            expanded = expanded,
            offset = DpOffset(x = (-horizontalPadding).dp, 4.dp),
            onDismissRequest = { expanded = false }
        ) {
            val chunkSize = 4
            val spacerSize = 4

            Column(
                Modifier.width((swatchSize * chunkSize + spacerSize * (chunkSize - 1)).dp),
                verticalArrangement = Arrangement.spacedBy(spacerSize.dp)
            ) {
                val chunkedColors = chunk(availableColors, chunkSize)
                chunkedColors.forEach { colors ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacerSize.dp)
                    ) {
                        colors.forEach {
                            ColorSwatch(swatchSize, it, onClick = {
                                onValueChange(it)
                                expanded = false
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    swatchSize: Int,
    colorValue: Color,
    onClick: () -> Unit
) {
    Box(Modifier
        .size(swatchSize.dp)
        .clip(CircleShape)
        .background(colorValue)
        .border(width = 1.dp, color = Color.Black.copy(alpha = 0.25f), shape = CircleShape)
        .clickable { onClick() }
    ) {
        if (colorValue == Color.Transparent) {
            TransparentBackground()
        }
    }
}

@Composable
private fun TransparentBackground() {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = canvasWidth, y = 0f),
            end = Offset(x = 0f, y = canvasHeight),
            color = Color.Red.copy(alpha = 0.7f),
            strokeWidth = 3f
        )
    }
}

@Composable
fun ShapeInput(
    shapeValue: AvailableShapes,
    cornerValue: Int,
    onValueChange: (shape: AvailableShapes, corner: Int) -> Unit,
) {
    val shapesList = listOf(
        Pair("rectangle", AvailableShapes.Rectangle),
        Pair("circle", AvailableShapes.Circle),
        Pair("rounded-corner", AvailableShapes.RoundedCorner),
        Pair("cut-corner", AvailableShapes.CutCorner),
    )
    var hovered = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .pointerMoveFilter(
                onEnter = {
                    hovered.value = true
                    false
                },
                onExit = {
                    hovered.value = false
                    false
                }
            )
            .border(
                width = 1.dp,
                color = if (hovered.value) Color.LightGray else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            ),
    ) {
        for (pair in shapesList) {
            var mod = Modifier.clip(shape = RoundedCornerShape(4.dp))
            if (shapeValue == pair.second) mod = mod.background(MaterialTheme.colors.secondary)

            Icon(
                painter = svgResource("icons/${pair.first}.svg"),
                contentDescription = "${pair.first} shape button",
                modifier = mod.clickable {
                    onValueChange(pair.second, cornerValue)
                }
            )
        }
    }

    Spacer(Modifier.width(8.dp))
    DpInput(cornerValue, onValueChange = {
        onValueChange(shapeValue, it)
    })
}

@Composable
fun HorizontalArrangementInput(
    arrangementValue: AvailableHorizontalArrangements = AvailableHorizontalArrangements.Start,
    spacingValue: Int = 0,
    onValueChange: (arrangement: AvailableHorizontalArrangements, spacing: Int) -> Unit,
) {
    DropdownInput2(
        items = AvailableHorizontalArrangements.values().toList(),
        activeItem = arrangementValue,
        onSelect = {
            onValueChange(it, spacingValue)
        }
    )
    Spacer(Modifier.width(8.dp))
    DpInput(spacingValue, onValueChange = {
        onValueChange(arrangementValue, it)
    })
}

@Composable
fun VerticalArrangementInput(
    arrangementValue: AvailableVerticalArrangements = AvailableVerticalArrangements.Top,
    spacingValue: Int = 0,
    onValueChange: (arrangement: AvailableVerticalArrangements, spacing: Int) -> Unit,
) {
    DropdownInput2(
        items = AvailableVerticalArrangements.values().toList(),
        activeItem = arrangementValue,
        onSelect = {
            onValueChange(it, spacingValue)
        }
    )
    Spacer(Modifier.width(8.dp))
    DpInput(spacingValue, onValueChange = {
        onValueChange(arrangementValue, it)
    })
}

@Composable
fun HorizontalAlignmentInput(
    alignmentValue: Alignment.Horizontal = Alignment.Start,
    onValueChange: (alignment: Alignment.Horizontal) -> Unit,
) {
    DropdownInput(
        items = AvailableHorizontalAlignments,
        activeItem = AvailableHorizontalAlignments[alignmentValue]!!,
        onSelect = {
            onValueChange(it)
        }
    )
}

@Composable
fun VerticalAlignmentInput(
    alignmentValue: Alignment.Vertical = Alignment.Top,
    onValueChange: (alignment: Alignment.Vertical) -> Unit,
) {
    DropdownInput(
        items = AvailableVerticalAlignments,
        activeItem = AvailableVerticalAlignments[alignmentValue]!!,
        onSelect = {
            onValueChange(it)
        }
    )
}

@Composable
fun ContentAlignmentInput(
    alignmentValue: Alignment = Alignment.TopStart,
    onValueChange: (alignment: Alignment) -> Unit,
) {
    DropdownInput(
        items = AvailableContentAlignments,
        activeItem = AvailableContentAlignments[alignmentValue]!!,
        onSelect = {
            onValueChange(it)
        }
    )
}

@Composable
fun DpInput(value: Int, onValueChange: (Int) -> Unit) {
    var hasError by remember { mutableStateOf(false) }

    BasicTextField(
        value = if (value == 0) "" else value.toString(),
        onValueChange = {
            if (it.isBlank()) {
                onValueChange(0)
                hasError = false
            } else {
                val convertedValue = it.toIntOrNull()
                if (convertedValue != null) {
                    onValueChange(convertedValue)
                    hasError = false
                } else {
                    onValueChange(0)
                    hasError = true
                }
            }
        },
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .border(width = 1.dp, color = if (hasError) MaterialTheme.colors.error else Color.LightGray)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    innerTextField()
                    if (value == 0) Text("0", color = LocalContentColor.current.copy(alpha = ContentAlpha.disabled))
                }
            }
        },
        modifier = Modifier.width(40.dp),
        textStyle = MaterialTheme.typography.body2
    )
}

@Composable
fun <T> DropdownInput2(items: List<T>, activeItem: T, onSelect: (T) -> Unit) {
    Box {
        var hovered by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        Row(
            Modifier
                .clickable { expanded = true }
                .pointerMoveFilter(
                    onEnter = {
                        hovered = true
                        false
                    },
                    onExit = {
                        hovered = false
                        false
                    }
                )
                .height(24.dp)
                .width(120.dp)
                .border(width = 1.dp, color = if (hovered) Color.LightGray else Color.Transparent)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (hovered) Arrangement.SpaceBetween else Arrangement.Start
        ) {
            Text("$activeItem", style = MaterialTheme.typography.body2)
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Dropdown icon",
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(onClick = {
                    onSelect(it)
                    expanded = false
                }) {
                    Text("$it")
                }
            }
        }
    }
}

@Composable
fun <T> DropdownInput(items: Map<T, String>, activeItem: String, onSelect: (T) -> Unit) {
    Box {
        var hovered by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        Row(
            Modifier
                .clickable { expanded = true }
                .pointerMoveFilter(
                    onEnter = {
                        hovered = true
                        false
                    },
                    onExit = {
                        hovered = false
                        false
                    }
                )
                .height(24.dp)
                .width(120.dp)
                .border(width = 1.dp, color = if (hovered) Color.LightGray else Color.Transparent)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (hovered) Arrangement.SpaceBetween else Arrangement.Start
        ) {
            Text(activeItem, style = MaterialTheme.typography.body2)
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Dropdown icon",
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { (key, it) ->
                DropdownMenuItem(onClick = {
                    onSelect(key)
                    expanded = false
                }) {
                    Text(it)
                }
            }
        }
    }
}

private fun Modifier.cursorForHorizontalResize(
): Modifier = composed {
    var isHover by remember { mutableStateOf(false) }

    if (isHover) {
        LocalAppWindow.current.window.cursor = Cursor(Cursor.E_RESIZE_CURSOR)
    } else {
        LocalAppWindow.current.window.cursor = Cursor.getDefaultCursor()
    }

    pointerMoveFilter(
        onEnter = { isHover = true; true },
        onExit = { isHover = false; true }
    )
}