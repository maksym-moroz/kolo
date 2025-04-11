package com.kolo.example.state

import com.kolo.state.State

data class RootState(
    val counter: Int,
) : State
