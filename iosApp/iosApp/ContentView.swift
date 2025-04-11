import SwiftUI
import shared
import Combine

struct RootComponentUi: View {
    var storeContext: StoreContext
    var state: RootState
    
    // Custom initializer to set the initial state.
    init(storeContext: StoreContext, state: RootState) {
        self.storeContext = storeContext
        self.state = state
    }
    
    var body: some View {
        VStack {
            Text("Counter: \(state.counter)")
                .padding()
            Button("Increment") {
                storeContext.dispatch(action:RootActionIncrement())
            }
            Button("Decrement") {
                storeContext.dispatch(action:RootActionDecrement())
            }
        }
    }
}
