import SwiftUI
import Combine
import shared

@main
struct iOSApp: App {
    let rootState: RootState
    
    let container: RootEffectContainer
    let effects: [Effect]
    let middleware: [Middleware]
    

    let uiContent: UiContent<RootState>
    let component: RootComponent
    
    let mainScope: Kotlinx_coroutines_coreCoroutineScope
    
    let store: KoloStore<RootState>
    let storeContext: StoreContext
    
    init() {
        rootState = RootState(counter: -1)
        
        container = RootEffectContainer()
        effects = container.effects()

        middleware = createRootMiddlewareList()
        
        uiContent = RootUiContentImpl()
        component = RootComponent(
            content: uiContent,
            initialState: rootState,
            container: container,
        )
        
        
        mainScope = createMainScope()
        
        store = KoloStore(
            initialState: rootState,
            middleware: middleware,
            reducer: { [component] state, action in
                component.reduce(state: state, action: action)
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

class RootUiContentImpl: RootUiContent {
    override func ios(storeContext: any StoreContext, state: SkieSwiftStateFlow<RootState>) -> Any {
                RootComponentUi(
                    storeContext: storeContext,
                    stateFlow: state
                )
    }
}

