package com.c5inco.modifiers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import com.c5inco.modifiers.data.*

@Composable
fun ModifierEntry(
    modifierData: Triple<Modifier, Any, Boolean>,
    order: Int,
    size: Int,
    move: (index: Int, up: Boolean) -> Unit,
    onModifierChange: (Int, Triple<Modifier, Any, Boolean>) -> Unit,
    onRemove: (Int) -> Unit,
) {
    var rowHovered by remember { mutableStateOf(false) }
    val visible = modifierData.third

    Box(
        modifier = Modifier
            .pointerMoveFilter(
                onEnter = {
                    rowHovered = true
                    false
                },
                onExit = {
                    rowHovered = false
                    false
                }
            )
            //.height(48.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (rowHovered) {
            Column(
                modifier = Modifier
                    .offset(x = (-18).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Move modifier up",
                    tint = LocalContentColor.current.copy(alpha = if (order != 0) 1f else ContentAlpha.disabled),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(enabled = order != 0) {
                            move(order, true)
                        })
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Move modifier down",
                    tint = LocalContentColor.current.copy(alpha = if (order < size - 1) 1f else ContentAlpha.disabled),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(enabled = order < size - 1) {
                            move(order, false)
                        }
                )
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(Modifier.alpha(if (visible) 1f else 0.4f)) {
                when (modifierData.second) {
                    is SizeModifierData -> {
                        val data = modifierData.second as SizeModifierData
                        SizeModifier(
                            widthValue = data.width,
                            heightValue = data.height,
                            onChange = {
                                val (width, height) = it
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.size(width = width.dp, height = height.dp),
                                        it,
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is BackgroundModifierData -> {
                        val data = modifierData.second as BackgroundModifierData
                        BackgroundModifier(
                            colorValue = data.color,
                            shapeValue = data.shape,
                            cornerValue = data.corner,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.background(
                                            color = it.color,
                                            shape = getShape(it.shape, it.corner)
                                        ),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is BorderModifierData -> {
                        val data = modifierData.second as BorderModifierData
                        BorderModifier(
                            widthValue = data.width,
                            colorValue = data.color,
                            shapeValue = data.shape,
                            cornerValue = data.corner,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.border(
                                            width = (it.width).dp,
                                            color = it.color,
                                            shape = getShape(it.shape, it.corner)
                                        ),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is PaddingModifierData -> {
                        val data = modifierData.second as PaddingModifierData
                        PaddingModifier(
                            allValue = data.all,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.padding((it.all).dp),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is ShadowModifierData -> {
                        val data = modifierData.second as ShadowModifierData
                        ShadowModifier(
                            elevationValue = data.elevation,
                            shapeValue = data.shape,
                            cornerValue = data.corner,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.shadow(
                                            elevation = it.elevation.dp,
                                            shape = getShape(it.shape, it.corner)
                                        ),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is OffsetDesignModifierData -> {
                        val data = modifierData.second as OffsetDesignModifierData
                        OffsetDesignModifier(
                            xValue = data.x,
                            yValue = data.y,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.offset(
                                            x = (it.x).dp,
                                            y = (it.y).dp
                                        ),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is ClipModifierData -> {
                        val (shape, corner) = modifierData.second as ClipModifierData
                        ClipModifier(
                            shapeValue = shape,
                            cornerValue = corner,
                            onChange = {
                                println("$it.shape, $it.corner")
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.clip(getShape(it.shape, it.corner)),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is RotateModifierData -> {
                        val (degrees) = modifierData.second as RotateModifierData
                        RotateModifier(
                            degreesValue = degrees,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.rotate(it.degrees),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                    is ScaleModifierData -> {
                        val (scale) = modifierData.second as ScaleModifierData
                        ScaleModifier(
                            scaleValue = scale,
                            onChange = {
                                onModifierChange(
                                    order,
                                    Triple(
                                        Modifier.scale(it.scale),
                                        it.copy(),
                                        visible
                                    )
                                )
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(16.dp))
            Row {
                SmallIconButton(onClick = {
                    onModifierChange(order, Triple(modifierData.first, modifierData.second, !visible))
                }) {
                    Icon(
                        imageVector = if (visible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = "Toggle visibility",
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                SmallIconButton(onClick = {
                    onRemove(order)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Remove,
                        contentDescription = "Remove modifier",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
    Divider()
}

enum class ModifierEntry {
    Size,
    FillMaxWidth,
    FillMaxHeight,
    FillMaxSize,
    Padding,
    Border,
    Background,
    Shadow,
    Offset,
    Clip,
    Rotate,
    Scale,
}

fun getShape(shape: AvailableShapes, corner: Int): Shape {
    var realShape: Shape = RectangleShape

    when (shape) {
        AvailableShapes.Circle -> realShape = CircleShape
        AvailableShapes.RoundedCorner -> realShape = RoundedCornerShape(size = corner.dp)
        AvailableShapes.CutCorner -> realShape = CutCornerShape(size = corner.dp)
    }

    return realShape
}