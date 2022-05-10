package ru.witcher

import kotlinx.coroutines.*
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

fun asyncCall(): CompletableFuture<String> =
    CoroutineScope(Dispatchers.Default).future {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            throwException("Inside future block")
        }
//        async { throwException("Inside future block (async)") }.await()
        "result"
    }

fun main() {
    println(asyncCall().join())
    Thread.sleep(3000)
}
