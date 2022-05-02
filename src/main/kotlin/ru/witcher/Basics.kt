package ru.witcher

import kotlinx.coroutines.*

fun main() {
    scopeAndContext()
    jobLifecycle()
}

fun scopeAndContext() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Exception in $coroutineContext, $throwable")
    }
    val context = Job() + Dispatchers.Default + CoroutineName("test-coroutine") + exceptionHandler
    val scope = CoroutineScope(context)
    println("Main context: ${scope.coroutineContext}")
    val job = scope.launch {
        // new coroutine (parent CoroutineScope)
        println("Launch context: ${this.coroutineContext}")
        launch {
            // new coroutine (parent scope.launch)
            println("Nested one launch context: ${this.coroutineContext}")
            println("Nested one launch children: ${this.coroutineContext.job.children.joinToString()}")
            delay(200)
        }
        launch(Dispatchers.IO) {
            // new coroutine (parent scope.launch)
            println("Nested two launch context: ${this.coroutineContext}")
            println("Nested two launch children: ${this.coroutineContext.job.children.joinToString()}")
            delay(200)
        }
        println("Launch children: ${this.coroutineContext.job.children.joinToString()}")
        delay(100)
    }
    Thread.sleep(150)
    println("Main children: ${job.children.joinToString()}")
}

fun jobLifecycle() {
    val scope = CoroutineScope(Job())
    val job = scope.async(start = CoroutineStart.LAZY) {
        launch {
            println("Run in launch!")
            delay(1000)
            println(this@async)
        }
        println("Run in async!")
        delay(500)
        "Result from async"
    }
    println(job)
    job.start()
    println(job)
    println(runBlocking { job.await() })
    job.cancel()
    println(job)
}
