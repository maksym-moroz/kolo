package com.focus.kolo.config.impl.repo

import dev.zacsweers.metro.Inject

@Inject
class AppConfigSource {
    internal val config = BundledAppConfig.config
}
