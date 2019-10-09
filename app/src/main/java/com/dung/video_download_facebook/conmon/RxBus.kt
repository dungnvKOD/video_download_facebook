package com.dung.video_download_facebook.conmon

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object RxBus {


    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {


        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

    /**
     * // Custom MessageEvent class
    data class MessageEvent(val action: Int,
    val message: String)

    // Listen for MessageEvents only
    RxBus.listen(MessageEvent::class.java).subscribe({
    println("Im a Message event ${it.action} ${it.message}")
    })

    // Listen for String events only
    RxBus.listen(String::class.java).subscribe({
    println("Im a String event $it")
    })

    // Listen for Int events only
    RxBus.listen(Int::class.java).subscribe({
    println("Im an Int event $it")
    })


    // Publish events
    RxBus.publish(MessageEvent(1, "Hello, World"))
    RxBus.publish("Testing")
    RxBus.publish(123)
     */

}

