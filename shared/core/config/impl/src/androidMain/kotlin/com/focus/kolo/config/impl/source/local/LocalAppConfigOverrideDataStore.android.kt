package com.focus.kolo.config.impl.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

private var androidLocalAppConfigOverrideSource: LocalAppConfigOverrideSource? = null

fun createLocalAppConfigOverrideDataStore(context: Context): DataStore<Preferences> = createLocalAppConfigOverrideDataStore(
    producePath = {
        context.applicationContext.filesDir
            .resolve(LOCAL_APP_CONFIG_OVERRIDE_DATASTORE_FILE_NAME)
            .absolutePath
    }
)

fun createLocalAppConfigOverrideSource(context: Context): LocalAppConfigOverrideSource =
    androidLocalAppConfigOverrideSource ?: createPreferencesLocalAppConfigOverrideSource(
        createLocalAppConfigOverrideDataStore(context)
    ).also { created ->
        androidLocalAppConfigOverrideSource = created
    }
