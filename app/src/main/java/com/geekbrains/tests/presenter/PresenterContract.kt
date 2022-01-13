package com.geekbrains.tests.presenter


internal interface PresenterContract<T> {
    fun onAttach(fragmentActivity: T)
    fun onDetach(fragmentActivity: T)
}
