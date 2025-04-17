package com.kolo.action.variant

import com.kolo.action.Action
import com.kolo.state.Contract

interface ContractUpdateAction<C : Contract> : Action {
    val value: C
}
