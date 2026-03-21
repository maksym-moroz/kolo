package com.focus.kolo.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val PACKAGE_NAME = "com.focus.kolo"
private const val BUTTON_TEXT = "Click me!"
private const val UI_TIMEOUT_MS = 5_000L

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startupAndFirstInteraction() =
        baselineProfileRule.collect(
            packageName = PACKAGE_NAME,
        ) {
            pressHome()
            startActivityAndWait()

            val button =
                device.wait(Until.findObject(By.text(BUTTON_TEXT)), UI_TIMEOUT_MS)
                    ?: error("Did not find '$BUTTON_TEXT' within $UI_TIMEOUT_MS ms.")

            button.click()
            device.waitForIdle()
        }
}
