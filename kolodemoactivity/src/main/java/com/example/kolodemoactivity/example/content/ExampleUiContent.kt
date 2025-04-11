package com.example.kolodemoactivity.example.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.example.action.RootAction
import com.kolo.example.state.RootState
import kotlinx.coroutines.launch

// probably annotation processing to bind to needed component
@Composable
internal fun UiContent(
    storeContext: StoreContext,
    state: RootState,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${state.counter}",
            fontSize = 30.sp,
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    storeContext.dispatch(RootAction.Increment)
                }
            },
        ) {
            Text("Increment")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    storeContext.dispatch(RootAction.Decrement)
                }
            },
        ) {
            Text("Decrement")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    storeContext.dispatch(RootAction.DisplaySnackbarAction)
                }
            },
        ) {
            // WiP
            Text("Event action")
        }
    }
}
