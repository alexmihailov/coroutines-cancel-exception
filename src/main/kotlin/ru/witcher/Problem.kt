package ru.witcher

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class DocSubscriptionException : Exception()
suspend fun getSubscription(): String = "Subscription"
// suspend fun getSubscription(): String = throw DocSubscriptionException()
suspend fun getSubscriptionDocuments(): String = throw DocSubscriptionException()

suspend fun getSubscriptionAndDocuments(): String = coroutineScope {
    try {
        val subscriptionJob = async { getSubscription() }
        val documentsJob = async { getSubscriptionDocuments() }

        val documents = try {
            documentsJob.await()
        } catch (e: DocSubscriptionException) {
            null
        }
        subscriptionJob.await() + documents.orEmpty()
    } catch (e: DocSubscriptionException) {
        "Empty subscription"
    }
}

fun main() = runBlocking {
    println(getSubscriptionAndDocuments())
}
