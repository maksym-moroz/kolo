package com.example.kolodemoactivity.generate.context.reduce

import com.kolo.component.composition.context.reduce.ReduceContextDelegate

// todo change emptyMap to global inputs, probably via SL
fun initialiseReduceContext(): ReduceContextDelegate = ReduceContextDelegate(emptyMap())
