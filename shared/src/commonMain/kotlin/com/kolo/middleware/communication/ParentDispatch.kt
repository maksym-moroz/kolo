package com.kolo.middleware.communication

import com.kolo.action.Action

sealed interface ParentDispatch {
    fun dispatch(action: Action)
}
