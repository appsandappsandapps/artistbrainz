package com.example.swapcard.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlin.coroutines.EmptyCoroutineContext

@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun TestScope.launchAndWait(f: suspend CoroutineScope.() -> Unit) =
  launch(EmptyCoroutineContext, kotlinx.coroutines.CoroutineStart.DEFAULT, f).join()