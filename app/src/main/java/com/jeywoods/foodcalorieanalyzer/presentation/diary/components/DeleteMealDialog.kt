package com.jeywoods.foodcalorieanalyzer.presentation.diary.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun DeleteMealDialog(
    mealName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить приём пищи?") },
        text = {
            Text("Вы уверены, что хотите удалить «$mealName»?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}