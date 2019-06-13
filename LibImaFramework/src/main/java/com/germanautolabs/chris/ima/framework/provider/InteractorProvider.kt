package com.germanautolabs.chris.ima.framework.provider

import com.germanautolabs.chris.ima.framework.interactor.Interactor

interface InteractorProvider<T : Interactor> {

    fun provide(): T
}