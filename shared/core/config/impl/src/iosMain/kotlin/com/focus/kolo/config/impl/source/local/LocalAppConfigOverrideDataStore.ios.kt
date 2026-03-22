package com.focus.kolo.config.impl.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

private var iosLocalAppConfigOverrideSource: LocalAppConfigOverrideSource? = null

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
fun createLocalAppConfigOverrideDataStore(): DataStore<Preferences> = createLocalAppConfigOverrideDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        requireNotNull(documentDirectory).path + "/$LOCAL_APP_CONFIG_OVERRIDE_DATASTORE_FILE_NAME"
    }
)

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
fun createLocalAppConfigOverrideSource(): LocalAppConfigOverrideSource =
    iosLocalAppConfigOverrideSource ?: createPreferencesLocalAppConfigOverrideSource(
        createLocalAppConfigOverrideDataStore()
    ).also { created ->
        iosLocalAppConfigOverrideSource = created
    }
