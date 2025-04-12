package com.example.kolodemoactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.kolodemoactivity.example.content.UiContent
import com.example.kolodemoactivity.ui.theme.KoloTheme
import com.kolo.action.Action
import com.kolo.component.composition.content.UiContent
import com.kolo.component.composition.context.reduce.ReduceContextDelegate
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.component.composition.context.store.StoreContextDelegate
import com.kolo.example.component.RootComponent
import com.kolo.example.container.RootEffectContainer
import com.kolo.example.state.RootState
import com.kolo.middleware.Middleware
import com.kolo.middleware.effect.ActionEffectMiddleware
import com.kolo.middleware.effect.EventEffectMiddleware
import com.kolo.store.KoloStore
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = RootEffectContainer()
        val middleware: List<Middleware<RootState>> =
            listOf(
                ActionEffectMiddleware<RootState>(container.effects()),
                EventEffectMiddleware<RootState>(container.effects()),
            )

        val state = RootState(counter = -1)

        val content =
            object : UiContent<RootState>() {
                @Composable
                override fun android(
                    storeContext: StoreContext,
                    state: RootState,
                ) {
                    UiContent(storeContext, state)
                }

                override fun ios(
                    storeContext: StoreContext,
                    state: StateFlow<RootState>,
                ) = Unit
            }

        val component = RootComponent(content, state, container)
        val context = ReduceContextDelegate(emptyMap())
        val reducer = { state: RootState, action: Action ->
            component.processReduce(context, state, action)
        }

        val store =
            KoloStore<RootState>(
                initialState = state,
                middleware = middleware,
                reducer = reducer,
                outerScope = lifecycleScope,
            )

        setContent {
            KoloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = remember(store) { StoreContextDelegate(store) }

                    component.content.android(context, store.states.collectAsState().value)
                }
            }
        }
    }
}
