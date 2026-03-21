package com.focus.kolo

import dev.zacsweers.metro.Inject

@Inject
class Greeting(
    private val platform: Platform,
) {
    constructor() : this(getPlatform())

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
