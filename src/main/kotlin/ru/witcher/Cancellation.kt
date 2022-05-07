package ru.witcher

import kotlinx.coroutines.*

fun main() {
//    cancelScopeAndJob()
    cancelledScope()
    notStoppingWork()
    joinCancel()
    awaitCancel()
    cleanup()
//    suspendCancellableCoroutine and invokeOnCancellation
}

fun cancelScopeAndJob() {
    val scope = CoroutineScope(Job())

    val job1 = scope.launch {
        try {
            delay(500)
            println("First Job")
        } catch (e: CancellationException) {
            e.printStackTrace()
        }
    }
    val job2 = scope.launch {
        delay(500)
        println("Second Job")
    }
//    scope.cancel()
    job1.cancel()
    Thread.sleep(1000)
    println("End sleep")
}

fun cancelledScope() {
    val scope = CoroutineScope(Job())
    scope.launch { println("First") }
    scope.launch { println("Second") }
    scope.cancel()
    scope.launch { println("Third") }
    Thread.sleep(300)
}

fun notStoppingWork() = runBlocking {
    val count = 10
    val job = launch(Dispatchers.IO) {
        try {
            for (i in 1..count) {
//                if (!isActive) break
//                ensureActive()
//                yield()
                println("Step: $i")
                Thread.sleep(1000)
//                delay(1000)
            }
        } catch (e: CancellationException) {
            e.printStackTrace()
        }
    }
    delay(5000)
    job.cancel()
    println("End")
    println(job)
}

fun joinCancel() = runBlocking {
    val job = launch {
        delay(1000)
        println("Launch Start")
        delay(1000)
        println("Launch End")
    }
    println("Before cancel")
    job.cancel()
    println("After cancel")
    println("Before join")
    job.join()
    println("After join")
}

fun awaitCancel() = runBlocking {
    val job = async {
        delay(1000)
        println("Async Start")
        delay(1000)
        println("Async End")
        "Result"
    }
    println("Before cancel")
    job.cancel()
    println("After cancel")
    println("Before await")
    job.await()
    println("After await")
}

fun cleanup() = runBlocking {
    val job = launch {
        try {
            while (true) {
                println("I'm alive")
                delay(1000)
            }
        } catch (e: CancellationException) {
            println("Work cancelled!")
        } finally {
//            withContext(NonCancellable)
            launch {
                delay(1000)
                println("Cleanup done!")
            }
        }
    }
    delay(3000)
    job.cancel()
}
