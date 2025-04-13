package com.kolo.middleware.communication

import com.kolo.action.Action

data object ParentDispatchNoop : ParentDispatch {
    override fun dispatch(action: Action) = Unit
}
