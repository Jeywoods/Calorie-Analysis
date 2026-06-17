package com.jeywoods.foodcalorieanalyzer.presentation.diary.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun MealActions(
    isEditing: Boolean,
    gramsText: String,
    onGramsTextChanged: (String) -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: (Float) -> Unit,
    onDelete: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val textFieldValueState = remember { mutableStateOf(TextFieldValue(gramsText, TextRange(gramsText.length))) }
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            delay(300)
            focusRequester.requestFocus()
            showError = false
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                if (showError) 1.dp else 1.dp,
                                if (showError) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = textFieldValueState.value,
                            onValueChange = { newValue ->
                                val text = newValue.text
                                if (text.isEmpty()) {
                                    onGramsTextChanged("")
                                    textFieldValueState.value = TextFieldValue("", TextRange(0))
                                    showError = false
                                } else if (text.all { it.isDigit() } && text.length <= 4) {
                                    onGramsTextChanged(text)
                                    textFieldValueState.value = TextFieldValue(text, TextRange(text.length))
                                    showError = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .focusRequester(focusRequester),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            ),
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    innerTextField()
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("г", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                            }
                        )
                    }
                    Button(
                        onClick = {
                            val grams = gramsText.toFloatOrNull() ?: 0f
                            if (grams > 0) {
                                onSaveClick(grams)
                                showError = false
                            } else {
                                showError = true
                            }
                        },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Outlined.Check, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Сохранить", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            AnimatedVisibility(visible = !isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            textFieldValueState.value = TextFieldValue(gramsText, TextRange(gramsText.length))
                            onEditClick()
                        },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(18.dp))
                        Text("Изменить вес", style = MaterialTheme.typography.labelLarge)
                    }

                    Button(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Outlined.Delete, null, modifier = Modifier.size(18.dp))
                        Text("Удалить", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

        // Ошибка под полем
        if (showError) {
            Text(
                "Вес не может быть 0",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}