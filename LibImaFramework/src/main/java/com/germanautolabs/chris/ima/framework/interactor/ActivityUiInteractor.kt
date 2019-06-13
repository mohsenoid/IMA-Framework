package com.germanautolabs.chris.ima.framework.interactor

import android.os.Bundle

interface ActivityUiInteractor : UiIncludedInteractor {

    fun startActivity(bundle: Bundle = Bundle())
}