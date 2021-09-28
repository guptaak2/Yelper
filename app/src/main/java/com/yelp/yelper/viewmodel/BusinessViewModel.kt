package com.yelp.yelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelp_td.R
import com.yelp.yelper.StringResourceProvider
import com.yelp.yelper.model.BusinessUiModel
import com.yelp.yelper.model.NO_ERROR
import com.yelp.yelper.repository.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val listOfBusinesses: MutableLiveData<BusinessUiModel> = MutableLiveData()

    val businesses: LiveData<BusinessUiModel>
        get() = listOfBusinesses

    fun getBusinesses(location: String) {
        viewModelScope.launch {
            if (location.isEmpty()) {
                listOfBusinesses.value =
                    BusinessUiModel.Error(stringResourceProvider.getString(R.string.empty_address_description))
                return@launch
            }
            val data = businessRepository.getBusinessByLocation(location)
            if (data.error.code == NO_ERROR) {
                listOfBusinesses.value = BusinessUiModel.Businesses(data.businesses)
            } else {
                listOfBusinesses.value =
                    BusinessUiModel.Error(data.error.code + " \n" + data.error.description)
            }
        }
    }
}