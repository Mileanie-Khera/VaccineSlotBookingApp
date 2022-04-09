package com.study.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.study.learndagger.CSBApp
import com.study.learndagger.R
import com.study.models.*
import com.study.retrofit.ApiClient
import com.study.retrofit.Constants
import com.study.retrofit.CowinAPIInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginVM(application: Application) : AndroidViewModel(application) {
    private val scopeIO = CoroutineScope(Dispatchers.IO)
    private var mCowinAPIInterface =
        ApiClient.getClient(application).create(CowinAPIInterface::class.java)

    val mGenerateOTPLiveData = MutableLiveData<GenerateOTPResponse>()
    val mValidateOTPLiveData = MutableLiveData<ValidateOTPResponse>()
    val mErrorLiveData = MutableLiveData<ErrorBody>()

    fun generateOTP(mobileNum: String) {
        scopeIO.launch {
            val generateOTPModel = GenerateOTPModel()
            generateOTPModel.mobile = mobileNum
            generateOTPModel.secret = Constants.SECRET_KEY
            val cowinCenters = mCowinAPIInterface.generateMobileOTP(generateOTPModel)
            if (cowinCenters.isSuccessful) {
                val cowinCenterResponse = cowinCenters.body()
                mGenerateOTPLiveData.postValue(cowinCenterResponse)
            } else {
                try {
                    val errorBody =
                        Gson().fromJson(cowinCenters.errorBody()?.string(), ErrorBody::class.java)
                    mErrorLiveData.postValue(errorBody)
                } catch (e: Exception) {
                    e.printStackTrace()
                    val errorBody = ErrorBody()
                    errorBody.error = CSBApp.mCSBApp.getString(R.string.something_went_wrong)
                    mErrorLiveData.postValue(errorBody)
                }
            }
        }
    }

    fun verifyOTP(otpHash: String, mTxnId: String) {
        scopeIO.launch {
            val validateOTPModel = ValidateOTPModel()
            validateOTPModel.otp = otpHash
            validateOTPModel.txnId = mTxnId
            val cowinCenters = mCowinAPIInterface.validateMobileOTP(validateOTPModel)
            if (cowinCenters.isSuccessful) {
                val cowinCenterResponse = cowinCenters.body()
                mValidateOTPLiveData.postValue(cowinCenterResponse)
            } else {
                try {
                    val errorBody =
                        Gson().fromJson(cowinCenters.errorBody()?.string(), ErrorBody::class.java)
                    mErrorLiveData.postValue(errorBody)
                } catch (e: Exception) {
                    e.printStackTrace()
                    val errorBody = ErrorBody()
                    errorBody.error = CSBApp.mCSBApp.getString(R.string.something_went_wrong)
                    mErrorLiveData.postValue(errorBody)
                }

            }
        }
    }
}