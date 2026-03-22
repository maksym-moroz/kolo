package com.focus.kolo.store.fixture

import com.focus.kolo.store.UiState

data class CounterState(
    val count: Int = 0,
    val loading: Boolean = false
) : UiState
