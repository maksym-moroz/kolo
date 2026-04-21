package com.focus.kolo.debugmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focus.kolo.DebugMenuGraph
import com.focus.kolo.config.AppConfigSnapshot
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DebugMenuHost(
    appGraph: DebugMenuGraph,
    onClose: () -> Unit,
    onRestartRequired: () -> Unit
) {
    val dependencies = remember(appGraph) {
        appGraph.asDebugMenuDependencies()
    }

    DebugMenuRoute(
        dependencies = dependencies,
        onClose = onClose,
        onRestartRequired = onRestartRequired
    )
}

@Composable
internal fun DebugMenuRoute(
    dependencies: DebugMenuDependencies,
    onClose: () -> Unit,
    onRestartRequired: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val store = remember(dependencies, coroutineScope) {
        dependencies.storeFactory.create(
            scope = coroutineScope,
            initialState = DebugMenuState(),
            reducer = DebugMenuReducer(),
            middlewares = listOf(
                DebugMenuCommandMiddleware(
                    updateAppConfigOverrideUseCase = dependencies.updateAppConfigOverrideUseCase,
                    clearAppConfigOverrideUseCase = dependencies.clearAppConfigOverrideUseCase,
                    resetAppConfigOverridesUseCase = dependencies.resetAppConfigOverridesUseCase
                ),
                DebugMenuObserveMiddleware(dependencies.appConfigRepository)
            )
        )
    }
    val state by store.state
        .collectAsState()
    val intentMapper = remember { DebugMenuIntentMapper() }

    LaunchedEffect(store) {
        store.effects.collectLatest { effect ->
            when (effect) {
                DebugMenuEffect.Close -> onClose()
                DebugMenuEffect.RestartRequired -> onRestartRequired()
            }
        }
    }

    val snapshot = state.snapshot
    if (snapshot == null) {
        DebugMenuLoadingScaffold(onClose = onClose)
        return
    }

    var latestVersionInput by remember(snapshot.config.versionPolicy.latestVersion) {
        mutableStateOf(snapshot.config.versionPolicy.latestVersion)
    }
    var minimumSupportedVersionInput by remember(snapshot.config.versionPolicy.minimumSupportedVersion) {
        mutableStateOf(snapshot.config.versionPolicy.minimumSupportedVersion)
    }

    DebugMenuScaffold(
        commandFailureMessage = state.commandFailureMessage,
        snapshot = snapshot,
        latestVersionInput = latestVersionInput,
        minimumSupportedVersionInput = minimumSupportedVersionInput,
        onLatestVersionInputChange = { latestVersionInput = it },
        onMinimumSupportedVersionInputChange = { minimumSupportedVersionInput = it },
        onIntent = { intent ->
            coroutineScope.launch {
                store.dispatch(intentMapper.map(intent))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DebugMenuScaffold(
    commandFailureMessage: String?,
    snapshot: AppConfigSnapshot,
    latestVersionInput: String,
    minimumSupportedVersionInput: String,
    onLatestVersionInputChange: (String) -> Unit,
    onMinimumSupportedVersionInputChange: (String) -> Unit,
    onIntent: (DebugMenuIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Debug Menu") },
                navigationIcon = {
                    TextButton(onClick = { onIntent(DebugMenuIntent.Close) }) {
                        Text("Close")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (commandFailureMessage != null) {
                CommandFailureSection(message = commandFailureMessage)
            }
            ConfigSourceSection(sourceState = snapshot.sourceState)
            EnvironmentSection(
                environment = snapshot.config.environment,
                onIntent = onIntent
            )
            FeatureFlagsSection(
                featureFlags = snapshot.config.featureFlags,
                onIntent = onIntent
            )
            VersionPolicySection(
                latestVersionInput = latestVersionInput,
                minimumSupportedVersionInput = minimumSupportedVersionInput,
                onLatestVersionInputChange = onLatestVersionInputChange,
                onMinimumSupportedVersionInputChange = onMinimumSupportedVersionInputChange,
                onIntent = onIntent
            )
            UrlsSection(urls = snapshot.config.urls)
            Button(
                onClick = { onIntent(DebugMenuIntent.ResetAllOverrides) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset All Overrides")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DebugMenuLoadingScaffold(onClose: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Debug Menu") },
                navigationIcon = {
                    TextButton(onClick = onClose) {
                        Text("Close")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading runtime config...")
        }
    }
}

@Composable
private fun CommandFailureSection(message: String) {
    DebugMenuSection(title = "Command Error") {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
internal fun DebugMenuSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        content()
        HorizontalDivider()
    }
}
