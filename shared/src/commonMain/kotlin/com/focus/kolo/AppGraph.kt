package com.focus.kolo

import com.focus.kolo.config.AppConfigRepository
import com.focus.kolo.store.StoreFactory

interface AppGraph {
    val storeFactory: StoreFactory
    val appConfigRepository: AppConfigRepository
}
