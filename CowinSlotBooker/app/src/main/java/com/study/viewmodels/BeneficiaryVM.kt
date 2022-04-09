package com.study.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.study.models.beneficiaries.BeneficiaryResponse
import com.study.retrofit.ApiClient
import com.study.retrofit.Constants
import com.study.retrofit.CowinAPIInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BeneficiaryVM(application: Application) : AndroidViewModel(application) {
    private val scopeIO = CoroutineScope(Dispatchers.IO)
    private var mCowinAPIInterface =
        ApiClient.getClient(application).create(CowinAPIInterface::class.java)

    val mBeneficiaryResponseLiveData = MutableLiveData<BeneficiaryResponse>()
    val mErrorLiveData = MutableLiveData<Boolean>()


    fun getBeneficiaries() {
        scopeIO.launch {
            val cowinCenters = mCowinAPIInterface.getBeneficiaries(
                Constants.BEARER_TOKEN
            )
            if (cowinCenters.isSuccessful) {
                val cowinCenterResponse = cowinCenters.body()
                mBeneficiaryResponseLiveData.postValue(cowinCenterResponse)
            } else {
                mErrorLiveData.postValue(true)
            }
        }
    }
}