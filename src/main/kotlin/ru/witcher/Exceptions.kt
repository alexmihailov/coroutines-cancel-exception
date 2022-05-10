package ru.witcher

import kotlinx.coroutines.*

fun main() {
    propagateException()
    whoMyParent()
}

fun propagateException() {
    val scope = CoroutineScope(Job())
    val exceptionHandler = CoroutineExceptionHandler { ctx, ex -> println("Exception $ex in $ctx") }
//    val scope = CoroutineScope(SupervisorJob() + exceptionHandler)
    scope.launch {
//        supervisorScope {
//        }
        println("Start parent job")
        launch {
            try {
                println("Start first nested job")
                delay(1000)
                println("Finish first nested job")
            } catch (e: CancellationException) {
                println("Canceling first nested job")
            }
        }
        launch {
            try {
                println("Start second nested job")
                delay(1000)
                println("Finish second nested job")
            } catch (e: CancellationException) {
                println("Canceling second nested job")
            }
        }
        launch {
            println("Start third nested job")
            throw IllegalAccessError()
        }
        delay(100)
        println("Finish parent job")
    }
    scope.launch {
        try {
//            throw IllegalAccessError()
            println("Start other job")
            delay(1000)
            println("Finish other job")
        } catch (e: CancellationException) {
            println("Canceling other job")
        }
    }
    Thread.sleep(2000)
    println("Run after throws")
    scope.launch { println("Not run =(") }
    Thread.sleep(500)
}

fun whoMyParent() {
    val scope = CoroutineScope(Job())
    scope.launch(SupervisorJob()) {
        println("Start parent job")
        launch {
            println("Start child1 job")
            launch {
                throw IllegalAccessError()
            }
            delay(50)
            println("Finish child1 job")
        }
        delay(100)
        println("Finish parent job")
    }
    scope.launch {
        println("Start other job")
        delay(800)
        println("Finish other job")
    }
    Thread.sleep(1000)
}
