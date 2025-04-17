package com.kolo.action.variant

import com.kolo.state.Contract

interface AsContract<C : Contract> {
    val value: Contract
}
