package com.processdumper.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessListModal(
    showModal: Boolean,
    onDismiss: () -> Unit,
    clickableLine: (line: String) -> Unit,
    itens: List<String>
) {
    if (showModal) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .size(280.dp) // quadrado
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                LazyColumn {
                    items(itens) { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    clickableLine(item)

                                }
                        )
                    }
                }

            }
        }
    }
}