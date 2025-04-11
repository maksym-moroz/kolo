package com.kolo.example.middleware

import com.kolo.example.container.RootEffectContainer
import com.kolo.example.state.RootState
import com.kolo.middleware.Middleware
import com.kolo.middleware.effect.ActionEffectMiddleware
import com.kolo.middleware.effect.EventEffectMiddleware

typealias RootMiddleware = Middleware<RootState>

fun createRootMiddlewareList(): List<RootMiddleware> {
    val effects = RootEffectContainer().effects()
    return listOf(
        ActionEffectMiddleware(effects),
        EventEffectMiddleware(effects),
    )
}
