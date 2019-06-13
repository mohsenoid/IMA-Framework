# IMA Architecture and IMA Framework

Based on [Robert C. Martin (Uncle Bob)](https://sites.google.com/site/unclebobconsultingllc/) [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and [SOLID principles](https://en.wikipedia.org/wiki/SOLID) as our building blocks and long discussions about the architecture of Chris Android application we agreed on a brand new approach to keep project modules clean and well defined. We call this approach **Isolated Module Architecture (IMA)**.

## What is IMA?

IMA (Isolated Modules Architecture) is an architecture that every module is living in its own universe without any idea and knowledge about other modules or application that is using it. In other words, every module is working on its own way and define interfaces for consumers. In this architecture, there are no dependencies between modules and only the high-level policy (App) depends on all modules.

## What is IMA Framework?

IMA framework is a practical solution for implementing IMA architecture. This framework has some simple part that needs to be implemented by modules in some different cases. This framework will add as a dependency on every module include high-level policy. This module is the most so-called stable module in this architecture. The relation between modules and IMA framework has been figured out in the first diagram in this document.

## How to use IMA Framework?

### Intractor interface

A most important part of IMA framework is the interactor interface. This marker interface will use for other interactors in IMA and other modules that are using IMA.

```kotlin
interface Interactor
```

### ObservableInteractor abstract class

Many times we need to emit some events to high-level policy (App Module) to notify it about events in our module. For example imagine that we need to notify high-level policy in call module that phone is ringing, So in this situation we should have a mechanism to send this event to high-level policy. It's obvious that this our module as an isolated module doesn't have any idea about the receivers of these events. Let's see what is ObservableInteractor.

```kotlin
abstract class ObservableInteractor<T>(private val eventSubscriber: EventSubscriber<T>) : Interactor {

    protected fun sendEvent(event: T) {
        eventSubscriber.onEvent(event)
    }

    interface EventSubscriber<T> {

        fun onEvent(event: T)
    }
}
```

As you can see here this interactor is a child of Interactor marker interface. It needs to pass a parameter out to this interactor as event subscriber. An event subscriber is a simple interface to handle events that is arriving from an observable interactor. EventSubscriber should implement by high-level policy and high-level policy can get its events and send them to other modules which need to get these events. Also, there is a protected function in this abstract class which will send events to the subscriber, this function can use in every child of this interactor.

### UiIncludedInteractor

Some times modules contains UI and they have some business which needs to have an interaction with users. Sometimes we need to provide fragments and sometimes we need to just start an activity.Remember that if your UI contains fragment you need to implement a specific version of UiIncludedInteractor and if you want to start activity you need to implement another version of this interface. UiIncludedInteractor is a marker interface and other types of ui interactions extend this interface.

#### FragmentUiInteractor

Every business which includes a fragment in UI should implement an interactor that just returns one fragment. If your modules needs to provide more than one UI fragment you need to have more than one interactor to handle every scenario.

```kotlin
interface FragmentUiInteractor : UiIncludedInteractor {

    fun getFragment(
        bundle: Bundle = Bundle(),
        onJobDoneListener: OnJobDoneListener? = null
    ): Fragment

    interface OnJobDoneListener {

        fun onJobDone(bundle: Bundle = Bundle())
    }
}
```

There are 3 scenarios can happen in every UI cases.

- A fragment doesn't need to report its job to high-level policy and after finishing the job user will press the back button and come back to last page on stack.
- A fragment needs to report to high level policy that its job is done. In this case user needs to pass a parameter of OnJobDoneListener out to getFragment function.
- A fragment needs to load another fragment that is related to its business.

#### ActivityUiInteractor

In some cases we need to start a new activity in our business. So, in this case, we just implement ActivityUiInteractor interface.

```kotlin
interface ActivityUiInteractor : UiIncludedInteractor {

    fun startActivity(bundle: Bundle = Bundle())
}
```

You just pass a bundle to start the activity and this interactor start activity with its rules.

#### InteractorProvider

There is another important interface in IMA, that's called InteractorProvider. This interface is somehow a builder for interactors. Whereas, InteractorProvider needs to know the type of interactor that will return, you need to implement a provider for every interactor. Let's see how it works.

```kotlin
interface InteractorProvider<T : Interactor> {

    fun provide(): T
}
```

This interface has a generic type T of Interactor and has a function for building and returning interactor. Also this class can hold a weak reference of interactor to make it faster for building the interactor implementation class.

Finally, it's an obligation for every module to depend on this IMA framework library and follow the rules of this framework.