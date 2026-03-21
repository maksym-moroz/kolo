package com.focus.kolo

import platform.UIKit.UIDevice

class IosPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IosPlatform()
