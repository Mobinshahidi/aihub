package com.foss.aihub.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.foss.aihub.models.AiService
import com.foss.aihub.utils.aiServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareModelsScreen(
enabledServices: Set<String>,
serviceOrder: List<String>,
onOpenService: (AiService) -> Unit,
onContinueToChats: () -> Unit
) {
val context = LocalContext.current
val orderedEnabledServices = remember(enabledServices, serviceOrder, aiServices.toList()) {
serviceOrder.filter { it in enabledServices }
.mapNotNull { id -> aiServices.find { it.id == id } }
}

var promptText by remember { mutableStateOf("") }
val selectedIds = remember { mutableStateSetOf<String>() }
val responseNotes = remember { mutableStateMapOf<String, String>() }

Column(
modifier = Modifier
.fillMaxSize()
.verticalScroll(rememberScrollState())
.padding(horizontal = 16.dp, vertical = 12.dp),
verticalArrangement = Arrangement.spacedBy(16.dp)
) {
Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
Text(
text = "Compare models",
style = MaterialTheme.typography.headlineSmall,
fontWeight = FontWeight.SemiBold
)
Text(
text = "Select multiple assistants, send the same prompt, and compare their answers. " +
"AI Hub does not auto-submit or read responses from external sites.",
style = MaterialTheme.typography.bodyMedium,
color = MaterialTheme.colorScheme.onSurfaceVariant
)
}

Card(
shape = RoundedCornerShape(16.dp),
colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant)
) {
Column(
modifier = Modifier
.fillMaxWidth()
.padding(16.dp),
verticalArrangement = Arrangement.spacedBy(12.dp)
) {
Text(
text = "Prompt",
style = MaterialTheme.typography.titleSmall,
fontWeight = FontWeight.Medium
)
OutlinedTextField(
value = promptText,
onValueChange = { promptText = it },
modifier = Modifier
.fillMaxWidth()
.height(140.dp),
placeholder = { Text("Describe what you want, e.g. Generate a login page") },
keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
)

Row(
horizontalArrangement = Arrangement.spacedBy(12.dp),
modifier = Modifier.fillMaxWidth()
) {
OutlinedButton(
onClick = { copyPromptToClipboard(context, promptText) },
enabled = promptText.isNotBlank(),
modifier = Modifier.weight(1f)
) {
Icon(
imageVector = Icons.Outlined.ContentCopy,
contentDescription = null,
modifier = Modifier.size(18.dp)
)
Spacer(modifier = Modifier.width(8.dp))
Text("Copy prompt")
}

Button(
onClick = onContinueToChats,
modifier = Modifier.weight(1f)
) {
Text("Continue to chats")
}
}
}
}

Card(
shape = RoundedCornerShape(16.dp),
colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
) {
Column(
modifier = Modifier
.fillMaxWidth()
.padding(16.dp),
verticalArrangement = Arrangement.spacedBy(12.dp)
) {
Text(
text = "Select models",
style = MaterialTheme.typography.titleSmall,
fontWeight = FontWeight.Medium
)

Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
OutlinedButton(
onClick = {
selectedIds.clear()
selectedIds.addAll(orderedEnabledServices.map { it.id })
}
) {
Text("Select all")
}

OutlinedButton(onClick = { selectedIds.clear() }) {
Text("Clear")
}
}

LazyColumn(
modifier = Modifier
.fillMaxWidth()
.height(260.dp),
verticalArrangement = Arrangement.spacedBy(8.dp),
contentPadding = PaddingValues(bottom = 8.dp)
) {
items(orderedEnabledServices, key = { it.id }) { service ->
val isSelected = service.id in selectedIds
Surface(
onClick = {
if (isSelected) selectedIds.remove(service.id)
else selectedIds.add(service.id)
},
shape = RoundedCornerShape(12.dp),
color = MaterialTheme.colorScheme.surfaceContainerLow
) {
Row(
modifier = Modifier
.fillMaxWidth()
.padding(horizontal = 12.dp, vertical = 10.dp),
verticalAlignment = Alignment.CenterVertically,
horizontalArrangement = Arrangement.spacedBy(12.dp)
) {
Checkbox(
checked = isSelected,
onCheckedChange = {
if (it) selectedIds.add(service.id)
else selectedIds.remove(service.id)
}
)

Column(modifier = Modifier.weight(1f)) {
Text(
text = service.name,
style = MaterialTheme.typography.titleSmall,
maxLines = 1,
overflow = TextOverflow.Ellipsis
)
Text(
text = service.category,
style = MaterialTheme.typography.bodySmall,
color = MaterialTheme.colorScheme.onSurfaceVariant
)
}
}
}
}
}
}
}

Card(
shape = RoundedCornerShape(16.dp),
colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
) {
Column(
modifier = Modifier
.fillMaxWidth()
.padding(16.dp),
verticalArrangement = Arrangement.spacedBy(12.dp)
) {
Text(
text = "Comparison workspace",
style = MaterialTheme.typography.titleSmall,
fontWeight = FontWeight.Medium
)

if (selectedIds.isEmpty()) {
Text(
text = "Select at least one model to start comparing.",
style = MaterialTheme.typography.bodySmall,
color = MaterialTheme.colorScheme.onSurfaceVariant
)
} else {
selectedIds.mapNotNull { id ->
orderedEnabledServices.find { it.id == id }
}.forEach { service ->
Column(
modifier = Modifier.fillMaxWidth(),
verticalArrangement = Arrangement.spacedBy(8.dp)
) {
Row(
modifier = Modifier.fillMaxWidth(),
horizontalArrangement = Arrangement.SpaceBetween,
verticalAlignment = Alignment.CenterVertically
) {
Column(modifier = Modifier.weight(1f)) {
Text(
text = service.name,
style = MaterialTheme.typography.titleSmall,
fontWeight = FontWeight.SemiBold
)
Text(
text = "Open the chat, paste the prompt, and paste the reply here.",
style = MaterialTheme.typography.bodySmall,
color = MaterialTheme.colorScheme.onSurfaceVariant
)
}

OutlinedButton(
onClick = { onOpenService(service) }
) {
Icon(
imageVector = Icons.Outlined.OpenInNew,
contentDescription = null,
modifier = Modifier.size(18.dp)
)
Spacer(modifier = Modifier.width(6.dp))
Text("Open chat")
}
}

OutlinedTextField(
value = responseNotes[service.id] ?: "",
onValueChange = { responseNotes[service.id] = it },
modifier = Modifier
.fillMaxWidth()
.height(140.dp),
placeholder = { Text("Paste ${service.name} response...") },
keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
)

Divider()
}
}
}
}
}

Spacer(modifier = Modifier.height(12.dp))
}
}

private fun copyPromptToClipboard(context: Context, prompt: String) {
if (prompt.isBlank()) return
val clipboard = context.getSystemService<android.content.ClipboardManager>() ?: return
val clip = android.content.ClipData.newPlainText("AI Hub Prompt", prompt)
clipboard.setPrimaryClip(clip)
Toast.makeText(context, "Prompt copied", Toast.LENGTH_SHORT).show()
}
