package com.focus.kolo.config

import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    val snapshot: Flow<AppConfigSnapshot>
}
