import SwiftUI
import Combine
import shared

@main
struct iOSApp: App {
    let rootState: RootState
    
    let container: RootEffectContainer
    let effects: [Effect]
    let middleware: [Middleware<RootState>]
    
    
    let uiContent: UiContent<RootState>
    let component: RootComponent
    
    let mainScope: Kotlinx_coroutines_coreCoroutineScope
    
    let store: KoloStore<RootState>
    let storeContext: StoreContext
    
    init() {
        rootState = RootState(counter: -1)
        
        container = RootEffectContainer()
        effects = container.effects()
        
        middleware = [
            ActionEffectMiddleware(effects: effects),
            EventEffectMiddleware(effects: effects)
            
        ]
        
        uiContent = RootUiContentImpl()
        component = RootComponent(
            content: uiContent,
            initialState: rootState,
            container: container,
        )
        
        
        mainScope = createMainScope()
        
        let context = ReduceContextDelegate(externalInput: [:])
        
        store = KoloStore(
            initialState: rootState,
            middleware: middleware,
            reducer: { [component] state, action in
                component.processReduce(
                    context: context,
                    state: state,
                    action: action
                )
            },
            outerScope: mainScope
        )
        storeContext = StoreContextDelegate<RootState>(store: store)
    }
    
    var body: some Scene {
        WindowGroup {
            let swiftUiView = component.content.ios(storeContext: storeContext, state: store.states) as! (any View)
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

