package com.kolo.effect

import com.kolo.action.Action
import kotlinx.coroutines.flow.Flow

interface Effect {
    fun intercept(actions: Flow<Action>): Flow<Action>
}
