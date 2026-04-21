package com.focus.kolo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.ui.NavDisplay
import com.focus.kolo.appshell.AppShellDefinition
import kotlinx.serialization.serializer

@Composable
internal fun KoloNavigationHost(appShellDefinition: AppShellDefinition) {
    val backStack =
        rememberSerializable(
            serializer = NavBackStackSerializer(serializer<KoloRoute>())
        ) {
            NavBackStack(KoloRoute.Home)
        }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider =
            entryProvider<KoloRoute> {
                entry<KoloRoute.Home> {
                    KoloHomeScreen(
                        appShellDefinition = appShellDefinition,
                        onOpenCapability = { capabilityWireName ->
                            backStack.add(KoloRoute.Capability(capabilityWireName))
                        }
                    )
                }
                entry<KoloRoute.Capability> { route ->
                    KoloCapabilityScreen(
                        appShellDefinition = appShellDefinition,
                        capabilityWireName = route.capabilityWireName,
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    )
                }
            }
    )
}
