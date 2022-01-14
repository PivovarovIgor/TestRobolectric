package com.geekbrains.tests.presenter.details

import android.os.Build
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.view.details.DetailsActivity
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsPresenterTest {

    private lateinit var scenario: ActivityScenario<DetailsActivity>
    private lateinit var presenter: DetailsPresenter

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
        scenario.onActivity {
            presenter = DetailsPresenter(it)
        }
    }

    @Test
    fun presenter_setCounter_Test() {
        presenter.setCounter(15)
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals("Number of results: 15", totalCountTextView.text)
        }
    }

    @Test
    fun presenter_onIncrement_Test() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals("Number of results: 0", totalCountTextView.text)
            presenter.onIncrement()
            assertEquals("Number of results: 1", totalCountTextView.text)
            presenter.onIncrement()
            assertEquals("Number of results: 2", totalCountTextView.text)
        }
    }

    @Test
    fun presenter_onDecrement_Test() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals("Number of results: 0", totalCountTextView.text)
            presenter.onDecrement()
            assertEquals("Number of results: -1", totalCountTextView.text)
            presenter.onDecrement()
            assertEquals("Number of results: -2", totalCountTextView.text)
        }
    }

    @Test
    fun presenter_onAttach_Test() {
        val activity = mock<DetailsActivity>()
        presenter.onAttach(activity)
        presenter.onIncrement()
        verify(activity, times(1)).setCount(1)
    }

    @Test
    fun presenter_onDetach_Test() {
        val activity = mock<DetailsActivity>()
        presenter.onAttach(activity)
        presenter.onDetach(activity)
        presenter.onDecrement()
        verify(activity, never()).setCount(anyInt())
    }

    @Test
    fun presenter_onDetach_try_detaching_fake_activity() {
        val activity = mock<DetailsActivity>()
        val fakeActivity = mock<DetailsActivity>()
        presenter.onAttach(activity)
        presenter.onIncrement()
        presenter.onDetach(fakeActivity)

        verify(activity, times(1)).setCount(anyInt())
        verify(fakeActivity, never()).setCount(anyInt())
    }

    @After
    fun tearDown() {
        scenario.close()
    }
}