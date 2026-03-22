package com.focus.kolo.config.impl.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.focus.kolo.config.AppEnvironment
import com.focus.kolo.config.impl.override.AppConfigOverride
import com.focus.kolo.config.impl.override.AppConfigOverrideEffect
import com.focus.kolo.config.impl.override.AppConfigOverrideField
import com.focus.kolo.config.impl.override.AppFeatureFlagKey
import com.focus.kolo.config.impl.override.AppFeatureFlagsOverride
import com.focus.kolo.config.impl.override.AppUrlsOverride
import com.focus.kolo.config.impl.override.AppVersionPolicyOverride
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesLocalAppConfigOverrideSource(
    private val dataStore: DataStore<Preferences>
) : LocalAppConfigOverrideSource {
    override val overrides: Flow<AppConfigOverride> = dataStore.data
        .map(::toAppConfigOverride)

    override suspend fun update(mutation: AppConfigOverrideEffect) {
        dataStore.edit { preferences ->
            when (mutation) {
                is AppConfigOverrideEffect.SetEnvironment -> {
                    preferences[ENVIRONMENT] = mutation.value.name
                }
                is AppConfigOverrideEffect.SetFeatureFlag -> {
                    preferences[flagKey(mutation.key)] = mutation.enabled
                }
                is AppConfigOverrideEffect.SetLatestVersion -> {
                    preferences[LATEST_VERSION] = mutation.value
                }
                is AppConfigOverrideEffect.SetMinimumSupportedVersion -> {
                    preferences[MINIMUM_SUPPORTED_VERSION] = mutation.value
                }
                is AppConfigOverrideEffect.SetSupportUrl -> {
                    preferences[SUPPORT_URL] = mutation.value
                }
                is AppConfigOverrideEffect.SetPrivacyPolicyUrl -> {
                    preferences[PRIVACY_POLICY_URL] = mutation.value
                }
            }
        }
    }

    override suspend fun clear(field: AppConfigOverrideField) {
        dataStore.edit { preferences ->
            when (field) {
                AppConfigOverrideField.ENVIRONMENT -> preferences.remove(ENVIRONMENT)
                AppConfigOverrideField.TASKS_ENABLED -> preferences.remove(flagKey(AppFeatureFlagKey.TASKS_ENABLED))
                AppConfigOverrideField.JOURNAL_ENABLED -> preferences.remove(flagKey(AppFeatureFlagKey.JOURNAL_ENABLED))
                AppConfigOverrideField.REMINDERS_ENABLED -> preferences.remove(flagKey(AppFeatureFlagKey.REMINDERS_ENABLED))
                AppConfigOverrideField.FORCED_UPDATE_ENABLED -> preferences.remove(flagKey(AppFeatureFlagKey.FORCED_UPDATE_ENABLED))
                AppConfigOverrideField.DEEPLINK_HANDLING_ENABLED -> preferences.remove(flagKey(AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED))
                AppConfigOverrideField.LATEST_VERSION -> preferences.remove(LATEST_VERSION)
                AppConfigOverrideField.MINIMUM_SUPPORTED_VERSION -> preferences.remove(MINIMUM_SUPPORTED_VERSION)
                AppConfigOverrideField.SUPPORT_URL -> preferences.remove(SUPPORT_URL)
                AppConfigOverrideField.PRIVACY_POLICY_URL -> preferences.remove(PRIVACY_POLICY_URL)
            }
        }
    }

    override suspend fun reset() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun toAppConfigOverride(preferences: Preferences): AppConfigOverride = AppConfigOverride(
        environment = preferences[ENVIRONMENT]?.let(AppEnvironment::valueOf),
        featureFlags = AppFeatureFlagsOverride(
            tasksEnabled = preferences[flagKey(AppFeatureFlagKey.TASKS_ENABLED)],
            journalEnabled = preferences[flagKey(AppFeatureFlagKey.JOURNAL_ENABLED)],
            remindersEnabled = preferences[flagKey(AppFeatureFlagKey.REMINDERS_ENABLED)],
            forcedUpdateEnabled = preferences[flagKey(AppFeatureFlagKey.FORCED_UPDATE_ENABLED)],
            deeplinkHandlingEnabled = preferences[flagKey(AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED)]
        ),
        versionPolicy = AppVersionPolicyOverride(
            latestVersion = preferences[LATEST_VERSION],
            minimumSupportedVersion = preferences[MINIMUM_SUPPORTED_VERSION]
        ),
        urls = AppUrlsOverride(
            supportUrl = preferences[SUPPORT_URL],
            privacyPolicyUrl = preferences[PRIVACY_POLICY_URL]
        )
    )

    private fun flagKey(key: AppFeatureFlagKey): Preferences.Key<Boolean> = when (key) {
        AppFeatureFlagKey.TASKS_ENABLED -> TASKS_ENABLED
        AppFeatureFlagKey.JOURNAL_ENABLED -> JOURNAL_ENABLED
        AppFeatureFlagKey.REMINDERS_ENABLED -> REMINDERS_ENABLED
        AppFeatureFlagKey.FORCED_UPDATE_ENABLED -> FORCED_UPDATE_ENABLED
        AppFeatureFlagKey.DEEPLINK_HANDLING_ENABLED -> DEEPLINK_HANDLING_ENABLED
    }

    private companion object {
        val ENVIRONMENT = stringPreferencesKey("app_config.environment")
        val TASKS_ENABLED = booleanPreferencesKey("app_config.feature_flags.tasks_enabled")
        val JOURNAL_ENABLED = booleanPreferencesKey("app_config.feature_flags.journal_enabled")
        val REMINDERS_ENABLED = booleanPreferencesKey("app_config.feature_flags.reminders_enabled")
        val FORCED_UPDATE_ENABLED = booleanPreferencesKey("app_config.feature_flags.forced_update_enabled")
        val DEEPLINK_HANDLING_ENABLED = booleanPreferencesKey("app_config.feature_flags.deeplink_handling_enabled")
        val LATEST_VERSION = stringPreferencesKey("app_config.version_policy.latest_version")
        val MINIMUM_SUPPORTED_VERSION = stringPreferencesKey("app_config.version_policy.minimum_supported_version")
        val SUPPORT_URL = stringPreferencesKey("app_config.urls.support_url")
        val PRIVACY_POLICY_URL = stringPreferencesKey("app_config.urls.privacy_policy_url")
    }
}
