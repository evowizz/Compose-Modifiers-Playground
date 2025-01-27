package ui.panel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import data.*
import ui.ElementRow
import ui.ModifierEntry
import ui.controls.*
import utils.DottedLine

@Composable
fun PropertiesPanel(
    modifier: Modifier = Modifier,
    activeTemplate: Template,
    parentElement: ElementModel,
    parentModifiersList: List<Pair<Any, Boolean>>,
    childScopeModifiersList: List<List<Pair<Any, Boolean>>>,
    childModifiersList: List<List<Pair<Any, Boolean>>>,
    onTemplateChange: (Template) -> Unit,
    onTemplateReset: () -> Unit,
    onParentElementChange: (ElementModel) -> Unit,
    onParentModifiersChange: (List<Pair<Any, Boolean>>) -> Unit,
    onChildScopeModifiersChange: (List<Pair<Any, Boolean>>, Int) -> Unit,
    onChildModifiersChange: (List<Pair<Any, Boolean>>, Int) -> Unit,
) {
    Surface(
        modifier = modifier
            .width(350.dp)
            .fillMaxHeight()
            .shadow(4.dp),
        elevation = 2.dp
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        "Template //",
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                    Spacer(Modifier.width(4.dp))
                    DropdownInput(
                        modifier = Modifier.weight(1f),
                        items = listOf(
                            Templates.PinkSquare,
                            Templates.Rainbow,
                            Templates.Sun,
                            Templates.SimpleCard
                        ),
                        shape = RoundedCornerShape(6.dp),
                        hoverBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                        hoverBorderColor = Color.Transparent,
                        activeItem = activeTemplate,
                        onSelect = onTemplateChange
                    )
                },
                modifier = Modifier.shadow(elevation = 3.dp),
                actions = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        ResetDefaultModifiersAction(onClick = onTemplateReset)
                        Spacer(Modifier.width(12.dp))
                    }
                }
            )
            ScrollableColumn {
                ParentGroup(
                    baseElement = parentElement,
                    modifiersList = parentModifiersList,
                    onElementChange = onParentElementChange,
                    onModifierChange = { event, data ->
                        val newList = parentModifiersList.toMutableList()

                        if (event == ModifierChangeEvent.ADD) {
                            newList.add(Pair(getNewModifierData(data as ModifierEntry), true))
                        }
                        if (event == ModifierChangeEvent.REMOVE) {
                            newList.removeAt(data as Int)
                        }
                        if (event == ModifierChangeEvent.EDIT) {
                            val entry = data as ModifierEntryData
                            newList[entry.order] = entry.data
                        }
                        if (event == ModifierChangeEvent.REORDER) {
                            val (idx, targetIdx, newData) = data as Triple<Int, Int, Pair<Any, Boolean>>
                            val prev = newList[targetIdx]

                            newList[targetIdx] = newData
                            newList[idx] = prev
                        }

                        onParentModifiersChange(newList.toList())
                   },
                )

                activeTemplate.childElements.forEachIndexed { i, element ->
                    Divider()
                    ChildGroup(
                        name = getChildElementHeader(element),
                        parentElement = parentElement.type,
                        scopeModifiersList = childScopeModifiersList[i],
                        modifiersList = childModifiersList[i],
                        onScopeModifierChange = { event, data ->
                            val newList = childScopeModifiersList[i].toMutableList()

                            if (event == ModifierChangeEvent.ADD) {
                                newList.add(Pair(getNewScopeModifierData(data), true))
                            }
                            if (event == ModifierChangeEvent.REMOVE) {
                                newList.removeAt(data as Int)
                            }
                            if (event == ModifierChangeEvent.EDIT) {
                                val entry = data as ModifierEntryData
                                newList[entry.order] = entry.data
                            }
                            if (event == ModifierChangeEvent.REORDER) {
                                val (idx, targetIdx, newData) = data as Triple<Int, Int, Pair<Any, Boolean>>
                                val prev = newList[targetIdx]

                                newList[targetIdx] = newData
                                newList[idx] = prev
                            }

                            onChildScopeModifiersChange(newList.toList(), i)
                        },
                        onModifierChange = { event, data ->
                            val newList = childModifiersList[i].toMutableList()

                            if (event == ModifierChangeEvent.ADD) {
                                newList.add(Pair(getNewModifierData(data as ModifierEntry), true))
                            }
                            if (event == ModifierChangeEvent.REMOVE) {
                                newList.removeAt(data as Int)
                            }
                            if (event == ModifierChangeEvent.EDIT) {
                                val entry = data as ModifierEntryData
                                newList[entry.order] = entry.data
                            }
                            if (event == ModifierChangeEvent.REORDER) {
                                val (idx, targetIdx, newData) = data as Triple<Int, Int, Pair<Any, Boolean>>
                                val prev = newList[targetIdx]

                                newList[targetIdx] = newData
                                newList[idx] = prev
                            }

                            onChildModifiersChange(newList.toList(), i)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ParentGroup(
    baseElement: ElementModel,
    modifiersList: List<Pair<Any, Boolean>>,
    onElementChange: (ElementModel) -> Unit,
    onModifierChange: (ModifierChangeEvent, Any) -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }

    ComponentHeader("Parent element", expanded, onExpand = { expanded = !expanded })

    if (expanded) {
        PropertiesSection(
            empty = modifiersList.isEmpty(),
            modifier = Modifier.padding(8.dp),
        ) {
            ElementRow(
                model = baseElement,
                onValueChange = { onElementChange(it) }
            )
        }

        DottedLine(Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))

        PropertiesSection(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxHeight(),
            name = "Modifiers",
            empty = modifiersList.isEmpty(),
            actions = {
                AddModifierAction(
                    onSelect = {
                        onModifierChange(ModifierChangeEvent.ADD, it)
                    })
            }
        ) {
            modifiersList.forEachIndexed { idx, modifier ->
                ModifierEntry(
                    modifierData = modifier,
                    order = idx,
                    size = modifiersList.size,
                    move = { index, targetIndex, newData ->
                        onModifierChange(ModifierChangeEvent.REORDER, Triple(index, targetIndex, newData))
                    },
                    onChange = { data ->
                        onModifierChange(ModifierChangeEvent.EDIT, data)
                    },
                    onRemove = { index ->
                        onModifierChange(ModifierChangeEvent.REMOVE, index)
                    }
                )
            }
        }
    }
}

private fun getChildElementHeader(data: Any): String = (
    when (data) {
        is TextChildData -> {
            val (text) = data
            "Text(\"$text\")"
        }
        is ImageChildData -> {
            val (imagePath) = data
            "Image(\"$imagePath\")"
        }
        else -> {
            val (emoji) = data as ImageEmojiChildData
            "$emoji emoji"
        }
    }
)

@Composable
fun ChildGroup(
    name: String,
    parentElement: AvailableElements,
    scopeModifiersList: List<Pair<Any, Boolean>>,
    modifiersList: List<Pair<Any, Boolean>>,
    onScopeModifierChange: (ModifierChangeEvent, Any) -> Unit,
    onModifierChange: (ModifierChangeEvent, Any) -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }

    ComponentHeader(name, expanded, onExpand = { expanded = !expanded })

    if (expanded) {
        PropertiesSection(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxHeight(),
            name = "${parentElement}Scope modifiers",
            empty = scopeModifiersList.isEmpty(),
            actions = {
                AddChildModifierAction(
                    parentElement,
                    onSelect = {
                        onScopeModifierChange(ModifierChangeEvent.ADD, it)
                    }
                )
            }
        ) {
            scopeModifiersList.forEachIndexed { idx, modifier ->
                ModifierEntry(
                    modifierData = modifier,
                    order = idx,
                    size = scopeModifiersList.size,
                    move = { index, targetIndex, newData ->
                        onScopeModifierChange(ModifierChangeEvent.REORDER, Triple(index, targetIndex, newData))
                    },
                    onChange = { data ->
                        onScopeModifierChange(ModifierChangeEvent.EDIT, data)
                    },
                    onRemove = { index ->
                        onScopeModifierChange(ModifierChangeEvent.REMOVE, index)
                    }
                )
            }
        }

        DottedLine(Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))

        PropertiesSection(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxHeight(),
            name = "Modifiers",
            empty = modifiersList.isEmpty(),
            actions = {
                AddModifierAction(
                    onSelect = {
                        onModifierChange(ModifierChangeEvent.ADD, it)
                    })
            }
        ) {
            modifiersList.forEachIndexed { i, modifier ->
                ModifierEntry(
                    modifierData = modifier,
                    order = i,
                    size = modifiersList.size,
                    move = { index, targetIndex, newData ->
                        onModifierChange(ModifierChangeEvent.REORDER, Triple(index, targetIndex, newData))
                    },
                    onChange = { data ->
                        onModifierChange(ModifierChangeEvent.EDIT, data)
                    },
                    onRemove = { index ->
                        onModifierChange(ModifierChangeEvent.REMOVE, index)
                    }
                )
            }
        }
    }
}

@Composable
fun PropertiesSection(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    name: String? = null,
    empty: Boolean = false,
    actions: @Composable RowScope.() -> Unit = { },
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val alphaAnim by animateFloatAsState(if (hovered || !empty) 1f else ContentAlpha.disabled)

    Column {
        name?.let {
            Row(
                Modifier
                    .hoverable(interactionSource)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                    .alpha(alphaAnim),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    actions()
                }
            }
        }
        Column(modifier) {
            content()
        }
    }
}

@Composable
private fun ComponentHeader(name: String, expanded: Boolean, onExpand: () -> Unit) {
    val expandAnim by animateFloatAsState(if (expanded) 180f else 0f)

    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val primaryColor = MaterialTheme.colors.primary
        Canvas(Modifier.width(4.dp).fillMaxHeight()) {
            drawRoundRect(
                color = primaryColor,
                size = Size(width = size.width, height = size.height),
                cornerRadius = CornerRadius(50f)
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(
            name,
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f).padding(end = 4.dp)
        )
        SmallIconButton(onClick = { onExpand() }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Expand/collapse component group",
                modifier = Modifier.rotate(expandAnim)
            )
        }
    }
}

@Composable
private fun ResetDefaultModifiersAction(onClick: () -> Unit) {
    SmallIconButton(
        requestFocus = false,
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = Icons.Outlined.RestartAlt,
            contentDescription = "Reset default modifiers",
            modifier = Modifier.size(18.dp),
            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
        )
    }
}

@Composable
private fun AddModifierAction(onSelect: (Any) -> Unit) {
    DropdownAction(
        onSelect = { onSelect(it) },
        onDismiss = { },
        menuContent = { handleSelect ->
            val select: (ModifierEntry) -> Unit = {
                handleSelect(it)
            }

            ModifierEntry.values().toList().forEach { entry ->
                CompactDropdownItem(entry = entry, onClick = { select(entry) })
            }
        }
    ) {
        SmallIconButton(onClick = it) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add modifier",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun AddChildModifierAction(parentType: AvailableElements, onSelect: (Any) -> Unit) {
    DropdownAction(
        onSelect = { onSelect(it) },
        onDismiss = { },
        menuContent = { handleSelect ->
            val select: (Any) -> Unit = {
                handleSelect(it)
            }

            when (parentType) {
                AvailableElements.Box -> {
                    BoxScopeModifierEntry.values().toList().forEach { entry ->
                        CompactDropdownItem(entry = entry, onClick = { select(entry) })
                    }
                }
                AvailableElements.Column -> {
                    ColumnScopeModifierEntry.values().toList().forEach { entry ->
                        CompactDropdownItem(entry = entry, onClick = { select(entry) })
                    }
                }
                AvailableElements.Row -> {
                    RowScopeModifierEntry.values().toList().forEach { entry ->
                        CompactDropdownItem(entry = entry, onClick = { select(entry) })
                    }
                }
            }
        }
    ) {
        SmallIconButton(onClick = it) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add child modifier",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}