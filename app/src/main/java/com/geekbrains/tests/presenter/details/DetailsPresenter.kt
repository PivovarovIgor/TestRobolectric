package com.geekbrains.tests.presenter.details

import com.geekbrains.tests.view.details.ViewDetailsContract

internal class DetailsPresenter internal constructor(
    private var viewContract: ViewDetailsContract?,
    private var count: Int = 0
) : PresenterDetailsContract {

    override fun setCounter(count: Int) {
        this.count = count
        viewContract?.setCount(count)
    }

    override fun onIncrement() {
        count++
        viewContract?.setCount(count)
    }

    override fun onDecrement() {
        count--
        viewContract?.setCount(count)
    }

    override fun onAttach(fragmentActivity: ViewDetailsContract) {
        viewContract = fragmentActivity
    }

    override fun onDetach(fragmentActivity: ViewDetailsContract) {
        if (fragmentActivity == viewContract) {
            viewContract = null
        }
    }
}
