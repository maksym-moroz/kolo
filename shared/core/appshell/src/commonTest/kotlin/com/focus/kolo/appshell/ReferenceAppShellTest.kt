package com.focus.kolo.appshell

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReferenceAppShellTest {
    @Test
    fun referenceShellMirrorsCurrentCapabilitySet() {
        assertEquals(
            expected =
                listOf(
                    AppCapability.Tasks,
                    AppCapability.Journal,
                    AppCapability.Reminders,
                    AppCapability.Settings,
                    AppCapability.DebugMenu
                ),
            actual = ReferenceAppShell.definition.capabilities
        )
    }

    @Test
    fun referenceShellEnablesDebugMenu() {
        assertTrue(
            ReferenceAppShell.definition
                .supports(AppCapability.DebugMenu)
        )
    }
}
