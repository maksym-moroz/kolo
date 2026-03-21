package com.focus.kolo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
