package com.example.kolodemoactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.kolodemoactivity.generate.component.configuration.getComponentConfiguration
import com.example.kolodemoactivity.generate.component.initialiseComponentA
import com.example.kolodemoactivity.generate.component.initialiseComponentB
import com.example.kolodemoactivity.generate.context.reduce.initialiseReduceContext
import com.example.kolodemoactivity.generate.store.getStoreConfiguration
import com.example.kolodemoactivity.ui.theme.KoloTheme
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.component.composition.context.store.StoreContextDelegate
import com.kolo.component.configuration.ComponentConfiguration
import com.kolo.effect.Effect
import com.kolo.example.a.container.EffectContainerA
import com.kolo.example.a.state.SelfA
import com.kolo.example.b.container.EffectContainerB
import com.kolo.example.b.state.contract.ContractB
import com.kolo.example.b.state.self.SelfB
import com.kolo.middleware.communication.ParentDispatch
import com.kolo.middleware.communication.ParentDispatchImpl
import com.kolo.middleware.communication.ParentDispatchNoop
import com.kolo.reducer.Reducer
import com.kolo.reducer.ReducerImpl
import com.kolo.state.WithNoContract
import com.kolo.store.KoloStore
import com.kolo.store.configuration.StoreConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialSelfA =
            SelfA(
                counter = -1,
                height = 10,
                width = 10,
                children = emptyList(),
            )
        val initialContractA: WithNoContract = WithNoContract

        val containerA: EffectContainer = EffectContainerA()
        val componentA: KoloComponent<*, SelfA, WithNoContract> = initialiseComponentA(initialSelfA, containerA)

        val reduceContextA: ReduceContext = initialiseReduceContext()

        val reducerA: Reducer<SelfA, WithNoContract> = ReducerImpl(componentA, reduceContextA)

        // todo split action and event effects into containers
        val effectsA: List<Effect> = containerA.effects()

        val componentConfigurationA: ComponentConfiguration = getComponentConfiguration(componentA)

        // looks like a circular dependency but not really, since either components don't have a parent
        // in that there is no mismatch, or the component have a parent, which means store will be available
        // but this definitely requires a refactor

        // [component, store, etc] as a single entity, if hasContracts store should be accessible
        val storeContextA: StoreContext? =
            if (componentConfigurationA.hasContracts) {
                StoreContextDelegate<SelfA, WithNoContract>(TODO())
            } else {
                null
            }

        val parentDispatchA: ParentDispatch =
            if (storeContextA != null) {
                ParentDispatchImpl(storeContextA)
            } else {
                ParentDispatchNoop
            }

        val storeConfigurationA: StoreConfiguration = getStoreConfiguration(effectsA, parentDispatchA)

        val storeA =
            KoloStore<SelfA, WithNoContract>(
                configuration = storeConfigurationA,
                initialState = initialSelfA,
                initialContract = initialContractA,
                reducer = reducerA,
                outerScope = lifecycleScope,
            )

        val componentDispatchA: StoreContext = StoreContextDelegate(storeA)

        // ### Extract all of this into separate functions

        val initialSelfB = SelfB(accumulator = 0)
        val initialContractB = ContractB(area = 0)

        val containerB: EffectContainer = EffectContainerB()
        val componentB: KoloComponent<*, SelfB, ContractB> = initialiseComponentB(initialSelfB, containerB)

        val reduceContextB: ReduceContext = initialiseReduceContext()

        val reducerB: Reducer<SelfB, ContractB> = ReducerImpl(componentB, reduceContextB)

        // todo split action and event effects into containers
        val effectsB: List<Effect> = containerB.effects()

        val componentConfigurationB: ComponentConfiguration = getComponentConfiguration(componentB)

        val storeContextB: StoreContext? =
            if (componentConfigurationB.hasContracts) {
                StoreContextDelegate<SelfA, WithNoContract>(storeA)
            } else {
                null
            }

        val parentDispatchB: ParentDispatch =
            if (storeContextB != null) {
                ParentDispatchImpl(storeContextB)
            } else {
                ParentDispatchNoop
            }

        val storeConfigurationB: StoreConfiguration = getStoreConfiguration(effectsB, parentDispatchB)

        val storeB: KoloStore<SelfB, ContractB> =
            KoloStore<SelfB, ContractB>(
                configuration = storeConfigurationB,
                initialState = initialSelfB,
                initialContract = initialContractB,
                reducer = reducerB,
                outerScope = lifecycleScope,
            )

        val componentDispatchB: StoreContext = StoreContextDelegate(storeB)

        setContent {
            KoloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    componentA.content.android(
                        componentDispatchA,
                        storeA.states.collectAsState().value,
                        storeA.contracts.collectAsState().value,
                    )
                }
            }
        }
    }
}

//            [parent (initial) --> child, parent (eventual) --> child]
//            child declares contract, it's on parent to fulfill it, otherwise can't create
//            reduce signature includes both State and Contract to react accordingly
