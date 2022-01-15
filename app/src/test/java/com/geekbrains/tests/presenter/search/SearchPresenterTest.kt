package com.geekbrains.tests.presenter.search

import android.os.Build
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.view.search.MainActivity
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchPresenterTest {

    private lateinit var activity: MainActivity
    private lateinit var activityController: ActivityController<MainActivity>
    private lateinit var presenter: SearchPresenter
    @Mock private lateinit var repository: GitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this).close()
        presenter = SearchPresenter(null, repository)
        activityController = Robolectric.buildActivity(MainActivity::class.java)
            .create()
        activity = activityController.get()
        activity.presenter = presenter
    }

    @Test
    fun presenter_onAttach_response_before_attaching_view_to_presenter_and_single_toast_error_Test() {
        presenter.handleGitHubResponse(null)
        assertTrue(ShadowToast.shownToastCount() == 0)
        activityController
            .start()
            .resume()
        assertTrue(ShadowToast.shownToastCount() == 1)
        assertEquals("Response is null or unsuccessful", ShadowToast.getTextOfLatestToast())
        ShadowToast.reset()
        activityController
            .pause()
            .stop()
            .start()
            .resume()
        assertTrue(ShadowToast.shownToastCount() == 0)
    }

    @Test
    fun presenter_onAttach_response_after_attaching_view_to_presenter() {
        activityController
            .start()
            .resume()
        presenter.handleGitHubResponse(null)
        assertTrue(ShadowToast.shownToastCount() == 1)
        assertEquals("Response is null or unsuccessful", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun presenter_onDetach_response_after_detach_view_from_presenter() {
        activityController
            .start()
            .resume()
            .pause()
            .stop()
        presenter.handleGitHubResponse(null)
        assertTrue(ShadowToast.shownToastCount() == 0)
        activityController
            .start()
            .resume()
        assertTrue(ShadowToast.shownToastCount() == 1)
        assertEquals("Response is null or unsuccessful", ShadowToast.getTextOfLatestToast())
    }
}