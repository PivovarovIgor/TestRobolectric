package com.geekbrains.tests.presenter


interface PresenterContract<T> {
    fun onAttach(fragmentActivity: T)
    fun onDetach(fragmentActivity: T)
}
