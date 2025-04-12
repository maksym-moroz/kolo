import SwiftUI
import shared

struct RootComponentUi: View {
    var storeContext: StoreContext
    var stateFlow: SkieSwiftStateFlow<RootState>
    
    @SwiftUICore.State var state: RootState
    
    init(storeContext: StoreContext, stateFlow: SkieSwiftStateFlow<RootState>) {
        self.storeContext = storeContext
        self.stateFlow = stateFlow
        
        state = stateFlow.value
    }
    
    var body: some View {
        VStack {
            Text("Counter: \(state.counter)")
                .padding()
                .collect(flow: stateFlow, into: $state)
            Button("Increment") {
                storeContext.dispatch(action:RootActionIncrement())
            }
            Button("Decrement") {
                storeContext.dispatch(action:RootActionDecrement())
            }
        }
    }
}
