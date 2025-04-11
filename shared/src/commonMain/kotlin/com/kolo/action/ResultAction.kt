package com.kolo.action

sealed interface ResultAction : Action {
    data object WithNoResultAction : ResultAction
}
