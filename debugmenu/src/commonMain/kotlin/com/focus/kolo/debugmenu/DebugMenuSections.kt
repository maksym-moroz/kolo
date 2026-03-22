package com.focus.kolo.debugmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focus.kolo.config.AppEnvironment
import com.focus.kolo.config.AppFeatureFlags
import com.focus.kolo.config.AppUrls
import com.focus.kolo.config.impl.override.AppConfigOverrideField
import com.focus.kolo.config.impl.override.AppFeatureFlagKey

@Composable
internal fun EnvironmentSection(
    environment: AppEnvironment,
    onIntent: (DebugMenuIntent) -> Unit
) {
    DebugMenuSection(title = "Environment") {
        EnvironmentSelector(
            environment = environment,
            onSelect = { selected -> onIntent(DebugMenuIntent.SetEnvironment(selected)) },
            onClear = { onIntent(DebugMenuIntent.ClearOverride(AppConfigOverrideField.ENVIRONMENT)) }
        )
    }
}

@Composable
private fun EnvironmentSelector(
    environment: AppEnvironment,
    onSelect: (AppEnvironment) -> Unit,
    onClear: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EnvironmentOption(
            label = "Dev",
            selected = environment == AppEnvironment.DEV,
            onClick = { onSelect(AppEnvironment.DEV) }
        )
        EnvironmentOption(
            label = "Prod",
            selected = environment == AppEnvironment.PROD,
            onClick = { onSelect(AppEnvironment.PROD) }
        )
        TextButton(onClick = onClear) {
            Text("Clear Environment Override")
        }
    }
}

@Composable
private fun EnvironmentOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(label)
    }
}

@Composable
internal fun FeatureFlagsSection(
    featureFlags: AppFeatureFlags,
    onIntent: (DebugMenuIntent) -> Unit
) {
    DebugMenuSection(title = "Feature Flags") {
        featureFlagRows(featureFlags).forEach { row ->
            FeatureFlagRow(
                label = row.label,
                enabled = row.enabled,
                onToggle = { enabled ->
                    onIntent(DebugMenuIntent.SetFeatureFlag(row.key, enabled))
                },
                onClear = {
                    onIntent(DebugMenuIntent.ClearOverride(row.field))
                }
            )
        }
    }
}

@Composable
private fun FeatureFlagRow(
    label: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = enabled,
            onCheckedChange = onToggle
        )
        TextButton(onClick = onClear) {
            Text("Clear")
        }
    }
}

@Composable
internal fun VersionPolicySection(
    latestVersionInput: String,
    minimumSupportedVersionInput: String,
    onLatestVersionInputChange: (String) -> Unit,
    onMinimumSupportedVersionInputChange: (String) -> Unit,
    onIntent: (DebugMenuIntent) -> Unit
) {
    DebugMenuSection(title = "Version Policy") {
        VersionField(
            label = "Latest version",
            value = latestVersionInput,
            onValueChange = onLatestVersionInputChange,
            onApply = { onIntent(DebugMenuIntent.SetLatestVersion(latestVersionInput)) },
            onClear = { onIntent(DebugMenuIntent.ClearOverride(AppConfigOverrideField.LATEST_VERSION)) }
        )
        VersionField(
            label = "Minimum supported version",
            value = minimumSupportedVersionInput,
            onValueChange = onMinimumSupportedVersionInputChange,
            onApply = { onIntent(DebugMenuIntent.SetMinimumSupportedVersion(minimumSupportedVersionInput)) },
            onClear = {
                onIntent(
                    DebugMenuIntent.ClearOverride(
                        AppConfigOverrideField.MINIMUM_SUPPORTED_VERSION
                    )
                )
            }
        )
    }
}

@Composable
private fun VersionField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(onClick = onApply) {
                Text("Apply")
            }
        }
        TextButton(onClick = onClear) {
            Text("Clear Override")
        }
    }
}

@Composable
internal fun UrlsSection(urls: AppUrls) {
    DebugMenuSection(title = "URLs") {
        ReadOnlyRow(
            label = "Support URL",
            value = urls.supportUrl
        )
        ReadOnlyRow(
            label = "Privacy policy URL",
            value = urls.privacyPolicyUrl
        )
    }
}

@Composable
internal fun ReadOnlyRow(
    label: String,
    value: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold
        )
        Text(value)
    }
}

private data class FeatureFlagRowState(
    val key: AppFeatureFlagKey,
    val field: AppConfigOverrideField,
    val label: String,
    val enabled: Boolean
)

private fun featureFlagRows(featureFlags: AppFeatureFlags): List<FeatureFlagRowState> = listOf(
    FeatureFlagRowState(
        key = AppFeatureFlagKey.TASKS_ENABLED,
        field = AppConfigOverrideField.TASKS_ENABLED,
        label = "Tasks enabled",
        enabled = featureFlags.tasksEnabled
    ),
    FeatureFlagRowState(
        key = AppFeatureFlagKey.JOURNAL_ENABLED,
        field = AppConfigOverrideField.JOURNAL_ENABLED,
        label = "Journal enabled",
        enabled = featureFlags.journalEnabled
    ),
    FeatureFlagRowState(
        key = AppFeatureFlagKey.REMINDERS_ENABLED,
        field = AppConfigOverrideField.REMINDERS_ENABLED,
        label = "Reminders enabled",
        enabled = featureFlags.remindersEnabled
    ),
    FeatureFlagRowState(
        key = AppFeatureFlagKey.FORCED_UPDATE_ENABLED,
        field = AppConfigOverrideField.FORCED_UPDATE_ENABLED,
        label = "Forced update enabled",
        enabled = featureFlags.forcedUpdateEnabled
    ),
    FeatureFlagRowState(
        key = AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED,
        field = AppConfigOverrideField.DEEPLINK_HANDLING_ENABLED,
        label = "Deep-link handling enabled",
        enabled = featureFlags.deeplinkHandlingEnabled
    )
)
