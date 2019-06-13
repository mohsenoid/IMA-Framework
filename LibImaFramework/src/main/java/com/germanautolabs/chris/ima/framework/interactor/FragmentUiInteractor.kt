package com.germanautolabs.chris.ima.framework.interactor

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentUiInteractor : UiIncludedInteractor {

    fun getFragment(
        bundle: Bundle = Bundle(),
        onJobDoneListener: OnJobDoneListener? = null
    ): Fragment

    interface OnJobDoneListener {

        fun onJobDone(bundle: Bundle = Bundle())
    }
}