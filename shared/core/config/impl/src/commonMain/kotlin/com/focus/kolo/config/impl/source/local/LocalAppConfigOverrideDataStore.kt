package com.focus.kolo.config.impl.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val LOCAL_APP_CONFIG_OVERRIDE_DATASTORE_FILE_NAME = "app_config_overrides.preferences_pb"

fun createPreferencesLocalAppConfigOverrideSource(dataStore: DataStore<Preferences>): LocalAppConfigOverrideSource =
    PreferencesLocalAppConfigOverrideSource(dataStore)

internal fun createLocalAppConfigOverrideDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
