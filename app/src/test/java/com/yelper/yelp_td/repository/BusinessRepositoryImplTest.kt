package com.example.yelp_td.repository

import com.yelp.yelper.ConnectivityChecker
import com.example.yelp_td.MockBusinessResponseGenerator
import com.yelp.yelper.StringResourceProvider
import com.yelp.yelper.model.NO_ERROR
import com.yelp.yelper.repository.retrofit.YelpApi
import com.yelp.yelper.repository.BusinessRepository
import com.yelp.yelper.repository.BusinessRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BusinessRepositoryImplTest {

    private val yelpApi = mockk<YelpApi>()
    private val connectivityChecker = mockk<ConnectivityChecker>()
    private val stringResourceProvider = mockk<StringResourceProvider>()

    private lateinit var businessRepository: BusinessRepository

    @Before
    fun setup() {
        businessRepository =
            BusinessRepositoryImpl(yelpApi, connectivityChecker, stringResourceProvider)
        every {
            connectivityChecker.isConnected()
        } returns true
        every {
            stringResourceProvider.getString(any())
        } returns "This is a mock string"
    }

    @Test
    fun `no internet connection returns error`() {
        runBlocking {
            every {
                connectivityChecker.isConnected()
            } returns false

            val list = businessRepository.getBusinessByLocation("")
            assert(list.businesses.isEmpty())
            assert(list.error.code.isNotEmpty())
            assert(list.error.description.isNotEmpty())
        }
    }

    @Test
    fun `empty location returns error`() {
        runBlocking {
            coEvery {
                yelpApi.getBusinessByLocation("")
            } returns Response.error(400, "ErrorResponse".toResponseBody())

            val list = businessRepository.getBusinessByLocation("")
            assert(list.businesses.isEmpty())
            assert(list.error.code.isNotEmpty())
        }
    }

    @Test
    fun `get locations returns exception`() {
        runBlocking {
            coEvery {
                yelpApi.getBusinessByLocation("Toronto")
            } throws IllegalArgumentException()

            val list = businessRepository.getBusinessByLocation("Toronto")
            assert(list.businesses.isEmpty())
            assert(list.error.code.isNotEmpty())
        }
    }

    @Test
    fun `get locations returns list of businesses`() {
        runBlocking {
            coEvery {
                yelpApi.getBusinessByLocation("TD Towers Toronto")
            } returns Response.success(MockBusinessResponseGenerator.getMockYelpResponse())

            val list = businessRepository.getBusinessByLocation("TD Towers Toronto")
            assert(list.businesses.isNotEmpty())
            assert(list.error.code == NO_ERROR)
            assert(list.error.description.isEmpty())
        }
    }
}