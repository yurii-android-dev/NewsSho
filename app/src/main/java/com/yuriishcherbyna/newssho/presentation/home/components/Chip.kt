package com.yuriishcherbyna.newssho.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import java.util.Locale

@Composable
fun Chip(
    selectedCategory: Category,
    text: String,
    onCategoryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val isSelected = selectedCategory.name == text

    val background =
        if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
    else MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline

    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        onClick = { onCategoryClicked() },
        color = background,
        modifier = modifier
    ) {
        Text(
            text = text.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            },
            color = contentColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}