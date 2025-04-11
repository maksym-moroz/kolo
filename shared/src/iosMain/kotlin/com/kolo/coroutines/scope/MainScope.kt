package com.kolo.coroutines.scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MainScope {
    fun create(): CoroutineScope = MainScope()
}
