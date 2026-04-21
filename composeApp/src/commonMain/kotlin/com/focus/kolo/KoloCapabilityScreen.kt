package com.focus.kolo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
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
internal fun KoloCapabilityScreen(
    appShellDefinition: AppShellDefinition,
    capabilityWireName: String,
    onBack: () -> Unit
) {
    val capability =
        appShellDefinition.capabilities.firstOrNull { candidate ->
            candidate.wireName == capabilityWireName
        }

    Column(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .safeContentPadding()
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = capability?.displayName ?: capabilityWireName,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "This screen is reached through a typed Navigation 3 route owned by shared Compose.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
