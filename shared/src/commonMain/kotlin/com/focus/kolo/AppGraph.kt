package com.focus.kolo

import com.focus.kolo.store.StoreFactory

interface AppGraph {
    val greeting: Greeting
    val storeFactory: StoreFactory
}

expect fun createPlatformAppGraph(): AppGraph

object AppServices {
    fun greetingText(): String = createPlatformAppGraph().greeting.greet()
}
