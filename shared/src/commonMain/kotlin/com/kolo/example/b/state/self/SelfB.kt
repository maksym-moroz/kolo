package com.kolo.example.b.state.self

import com.kolo.state.Self

data class SelfB(
    val accumulator: Int,
    val area: Int = 0,
) : Self
