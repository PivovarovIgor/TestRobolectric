package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.presenter.PresenterContract
import com.geekbrains.tests.view.search.ViewSearchContract

interface PresenterSearchContract : PresenterContract<ViewSearchContract> {
    fun searchGitHub(searchQuery: String)
    //onAttach
    //onDetach
}
