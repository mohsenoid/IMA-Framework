package com.germanautolabs.chris.ima.framework.interactor

/**
 * T is a power enum that will define in target module
 */
abstract class ObservableInteractor<T>(private val eventSubscriber: EventSubscriber<T>) :
    Interactor {

    protected fun sendEvent(event: T) {
        eventSubscriber.onEvent(event)
    }

    interface EventSubscriber<T> {

        fun onEvent(event: T)
    }
}