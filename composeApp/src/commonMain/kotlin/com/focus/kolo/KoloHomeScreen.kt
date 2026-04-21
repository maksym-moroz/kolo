package com.focus.kolo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.focus.kolo.appshell.AppShellDefinition

@Composable
internal fun KoloHomeScreen(
    appShellDefinition: AppShellDefinition,
    onOpenCapability: (String) -> Unit
) {
    Column(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = appShellDefinition.displayName,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Shared Compose is mounted from a narrow app-shell contract instead of the full runtime-composition module.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier.weight(1f, fill = true),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = appShellDefinition.capabilities,
                key = { capability -> capability.wireName }
            ) { capability ->
                Button(
                    onClick = {
                        onOpenCapability(capability.wireName)
                    }
                ) {
                    Text(
                        text = capability.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Text(
            text = "Capabilities are declared once in shared core and opened through a typed shared route model.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
