package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.*

@Composable
fun ElementRow(
    elementValue: Pair<AvailableElements, Any>,
    onValueChange: (Pair<AvailableElements, Any>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val element = elementValue.first
    val elementData: Any = elementValue.second

    Column(Modifier.fillMaxWidth()) {
        Row {
            Box {
                var hovered by remember { mutableStateOf(false) }

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
                    Text("$element", style = MaterialTheme.typography.body2)
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Dropdown icon",
                        modifier = Modifier.size(18.dp)
                    )
                }

                val select: (AvailableElements) -> Unit = {
                    expanded = false
                    var newElementData: Any = BoxElementData()

                    when (it) {
                        AvailableElements.Column -> newElementData = ColumnElementData()
                        AvailableElements.Row -> newElementData = RowElementData()
                    }

                    onValueChange(Pair(
                        it,
                        newElementData
                    ))
                }

                DropdownMenu(
                    expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    AvailableElements.values().forEach {
                        DropdownMenuItem(onClick = { select(it) }) {
                            Text("$it")
                        }
                    }
                }
            }
        }

        when (element) {
            AvailableElements.Box -> {
                val data = elementData as BoxElementData
                Column(Modifier.padding(vertical = 8.dp)) {
                    Text(
                        "contentAlignment",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 12.sp,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                    Spacer(Modifier.height(4.dp))
                    ContentAlignmentInput(data.contentAlignment, onValueChange = { it ->
                        onValueChange(Pair(elementValue.first, BoxElementData(it)))
                    })
                }
            }
            AvailableElements.Column -> {
                val data = elementData as ColumnElementData
                Row(Modifier.padding(vertical = 8.dp)) {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(
                            "verticalArrangement",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 12.sp,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        )
                        Spacer(Modifier.height(4.dp))
                        VerticalArrangementInput(
                            data.verticalArrangement,
                            data.verticalSpacing,
                            onValueChange = { arrangement, spacing ->
                                onValueChange(
                                    Pair(
                                        elementValue.first,
                                        ColumnElementData(
                                            arrangement,
                                            spacing,
                                            data.horizontalAlignment
                                        )
                                    )
                                )
                            }
                        )
                    }
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(
                            "horizontalAlignment",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 12.sp,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        )
                        Spacer(Modifier.height(4.dp))
                        HorizontalAlignmentInput(data.horizontalAlignment, onValueChange = {
                            onValueChange(
                                Pair(
                                    elementValue.first,
                                    ColumnElementData(
                                        data.verticalArrangement,
                                        data.verticalSpacing,
                                        it
                                    )
                                )
                            )
                        })
                    }
                }
            }
            AvailableElements.Row -> {
                val data = elementData as RowElementData
                Row(Modifier.padding(vertical = 8.dp)) {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(
                            "horizontalArrangement",
                            fontSize = 12.sp,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        )
                        Spacer(Modifier.height(4.dp))
                        HorizontalArrangementInput(
                            data.horizontalArrangement,
                            data.horizontalSpacing,
                            onValueChange = { arrangement, spacing ->
                                onValueChange(
                                    Pair(
                                        elementValue.first,
                                        RowElementData(
                                            arrangement,
                                            spacing,
                                            data.verticalAlignment
                                        )
                                    )
                                )
                            }
                        )
                    }
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(
                            "verticalAlignment",
                            fontSize = 12.sp,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        )
                        Spacer(Modifier.height(4.dp))
                        VerticalAlignmentInput(data.verticalAlignment, onValueChange = {
                            onValueChange(
                                Pair(
                                    elementValue.first,
                                    RowElementData(
                                        elementData.horizontalArrangement,
                                        elementData.horizontalSpacing,
                                        it
                                    )
                                )
                            )
                        })
                    }
                }
            }
        }
    }
}