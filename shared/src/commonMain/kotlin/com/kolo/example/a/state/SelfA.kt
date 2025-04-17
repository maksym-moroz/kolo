package com.kolo.example.a.state

import com.kolo.component.common.KoloComponent
import com.kolo.state.Self

data class SelfA(
    val counter: Int,
    val height: Int,
    val width: Int,
    val children: ArrayDeque<KoloComponent<*, *, *>>,
) : Self
