package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.repository.GitHubRepository.GitHubRepositoryCallback
import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.search.ViewSearchContract
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter internal constructor(
    private var viewContract: ViewSearchContract?,
    private val repository: GitHubRepository
) : PresenterSearchContract, GitHubRepositoryCallback {

    private var handleGitHubResponseCommand: List<Response<SearchResponse?>?>? = null

    override fun searchGitHub(searchQuery: String) {
        viewContract?.let {
            it.displayLoading(true)
            repository.searchGithub(searchQuery, this)
        }
    }

    override fun onAttach(fragmentActivity: ViewSearchContract) {
        viewContract = fragmentActivity
        handleGitHubResponseCommand?.let {
            if (it.isNotEmpty()) {
                applyToViewResponse(it.first() )
            }
        }
    }

    override fun onDetach(fragmentActivity: ViewSearchContract) {
        if (fragmentActivity == viewContract) {
            viewContract = null
        }
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        applyToViewResponse(response)
    }

    private fun applyToViewResponse(response: Response<SearchResponse?>?) {
        viewContract?.let {
            handleGitHubResponseCommand = null
            it.displayLoading(false)
            if (response != null && response.isSuccessful) {
                val searchResponse = response.body()
                val searchResults = searchResponse?.searchResults
                val totalCount = searchResponse?.totalCount
                if (searchResults != null && totalCount != null) {
                    it.displaySearchResults(
                        searchResults,
                        totalCount
                    )
                } else {
                    it.displayError("Search results or total count are null")
                }
            } else {
                it.displayError("Response is null or unsuccessful")
            }
        } ?: let {
            handleGitHubResponseCommand = listOf(response)
        }
    }

    override fun handleGitHubError() {
        viewContract?.let{
            it.displayLoading(false)
            it.displayError()
        }
    }
}
