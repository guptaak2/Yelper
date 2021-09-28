package com.example.yelp_td.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.yelp_td.MainCoroutineRule
import com.example.yelp_td.MockBusinessResponseGenerator
import com.yelp.yelper.model.Business
import com.yelp.yelper.model.BusinessUiModel
import com.yelp.yelper.model.Error
import com.yelp.yelper.model.NO_ERROR
import com.yelp.yelper.repository.BusinessRepository
import com.yelp.yelper.viewmodel.BusinessViewModel
import com.google.common.truth.Truth.assertThat
import com.yelp.yelper.StringResourceProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class BusinessViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val businessRepository = mockk<BusinessRepository>()
    private val stringResourceProvider = mockk<StringResourceProvider>()

    private lateinit var viewModel: BusinessViewModel

    @Before
    fun setup() {
        viewModel = BusinessViewModel(businessRepository, stringResourceProvider)

        every {
            runBlocking {
                businessRepository.getBusinessByLocation("")
            }
        } returns MockBusinessResponseGenerator.getBusinessStateError()

        every {
            runBlocking {
                businessRepository.getBusinessByLocation("TD Towers Toronto")
            }
        } returns MockBusinessResponseGenerator.getBusinessStateNoError()

        every {
            stringResourceProvider.getString(any())
        } returns "This is a mock string"
    }

    @Test
    fun `get business with empty location - show error`() {
        val location = ""
        viewModel.getBusinesses(location)
        when (val value = viewModel.businesses.getOrAwaitValue()) {
            is BusinessUiModel.Businesses -> assertThat(value).isEqualTo(emptyList<Business>())
            is BusinessUiModel.Error -> assertThat(value).isNotNull()
        }
    }

    @Test
    fun `get business with valid location - show businesses`() {
        val location = "TD Towers Toronto"
        viewModel.getBusinesses(location)
        when (val value = viewModel.businesses.getOrAwaitValue()) {
            is BusinessUiModel.Businesses -> {
                assertThat(value).isNotNull()
                assertThat(value).isNotEqualTo(emptyList<Business>())
            }
            is BusinessUiModel.Error -> assertThat(value).isEqualTo(Error(NO_ERROR, ""))
        }
    }

    private fun <T> LiveData<T>.getOrAwaitValue(): T? {
        var data: T? = null
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {
            override fun onChanged(t: T) {
                data = t
                this@getOrAwaitValue.removeObserver(this)
                latch.countDown()
            }
        }

        this.observeForever(observer)

        try {
            if (!latch.await(3, TimeUnit.SECONDS)) {
                throw TimeoutException("LiveData value not set")
            }
        } finally {
            this.removeObserver(observer)
        }

        return data
    }
}