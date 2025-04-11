package com.kolo.action

sealed interface ResultAction : Action {
    data object WithNoResult : ResultAction
}
