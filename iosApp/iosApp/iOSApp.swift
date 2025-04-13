import SwiftUI
import Combine
import shared

@main
struct iOSApp: App {
    let rootState: RootState
    
    let container: EffectContainer
    let effects: [Effect]
    
    let reduceContext: ReduceContext
    
    let uiContent: UiContent<RootState>
    let component: RootComponent
    
    let componentConfiguration: ComponentConfiguration
    
    let reducer: ReducerImpl<RootState>
    
    let storeConfiguration: StoreConfiguration
    let store: KoloStore<RootState>
    let componentStoreContext: StoreContext
    
    init() {
        rootState = RootState(counter: -1)
        
        container = RootEffectContainer()
        effects = container.effects()
        
        reduceContext = ReduceContextDelegate(externalInput: [:])
        
        uiContent = RootUiContentImpl()
        component = RootComponent(
            content: uiContent,
            initialState: rootState,
            container: container,
        )
        
        componentConfiguration = ComponentConfiguration(
            hasContracts: false,
            hasResults: false,
            hasEffects: false,
            hasEvents: false
        )
        
        reducer = ReducerImpl(component: component.asGenericComponent(), context: reduceContext)
        
        let parentStoreContext: StoreContext? = nil
        
        let parentDispatch: ParentDispatch
        if componentConfiguration.hasContracts {
            parentDispatch = ParentDispatchImpl(storeContext: parentStoreContext!)
        } else {
            parentDispatch = ParentDispatchNoop()
        }
        
        storeConfiguration = StoreConfiguration(
            actionEffects: effects,
            eventEffects: effects,
            parentDispatch: parentDispatch
        )
        store = KoloStore(
            configuration: storeConfiguration,
            initialState: rootState,
            reducer: reducer,
            outerScope: createMainScope()
        )
        componentStoreContext = StoreContextDelegate<RootState>(store: store)
    }
    
    var body: some Scene {
        WindowGroup {
            let swiftUiView = component.content.ios(storeContext: componentStoreContext, state: store.states) as! (any View)
            AnyView(swiftUiView)
        }
    }
}

class RootUiContentImpl: UiContent<RootState> {
    override func ios(storeContext: any StoreContext, state: SkieSwiftStateFlow<RootState>) -> Any {
        RootComponentUi(
            storeContext: storeContext,
            stateFlow: state
        )
    }
}
