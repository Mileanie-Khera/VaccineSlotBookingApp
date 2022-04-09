package com.study.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.study.database.CowinDao
import com.study.database.CowinDaoModel
import com.study.learndagger.CSBApp
import com.study.learndagger.R
import com.study.models.Center
import com.study.models.ScheduleModel
import com.study.models.ScheduleResponse
import com.study.models.Session
import com.study.preferrences.CowinSharedPrefs
import com.study.retrofit.ApiClient
import com.study.retrofit.Constants
import com.study.retrofit.Constants.*
import com.study.retrofit.CowinAPIInterface
import com.study.utils.MMM_DD_YYYY_HH_MM_SS
import com.study.utils.getReadableDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CowinVM(application: Application) : AndroidViewModel(application) {
    private val scopeIO = CoroutineScope(Dispatchers.IO)
    private var mCowinAPIInterface =
        ApiClient.getClient(application).create(CowinAPIInterface::class.java)

    private val mCowinLiveData = MutableLiveData<CowinDaoModel>()
    private val mCowinScheduleLiveData = MutableLiveData<CowinDaoModel>()
    private var mAvailableSector: Center? = null
    private var mAvailableSession: Session? = null

    fun getCowinLiveData() = mCowinLiveData
    fun getCowinScheduleLiveData() = mCowinScheduleLiveData

    fun checkSlotAvailability(delayTime: Long) {

        viewModelScope.launch {
            delay(delayTime)
            val cowinCenters = mCowinAPIInterface.getCowinCenters(
                BEARER_TOKEN, PIN_CODE,
                CowinSharedPrefs.getIntstance(CSBApp.mCSBApp)
                    .getStringPref(CowinSharedPrefs.KEY_DATE)
            )
            mAvailableSession = null
            mAvailableSector = null

            val vaccineType = CowinSharedPrefs.getIntstance(CSBApp.mCSBApp)
                .getStringPref(CowinSharedPrefs.KEY_VACCINE_TYPE)

            val agePref = CowinSharedPrefs.getIntstance(CSBApp.mCSBApp)
                .getStringPref(CowinSharedPrefs.KEY_AGE)
            val age = if (agePref.contains("18")) {
                AGE_18
            } else {
                AGE_45
            }
            if (cowinCenters.isSuccessful) {
                val cowinCenterResponse = cowinCenters.body()

                cowinCenterResponse?.centers?.let { centreList ->
                    for (center in centreList) {
                        if (center.feeType.equals(FEE_TYPE, ignoreCase = true)) {
                            mAvailableSession =
                                if (SELECTED_BENEFICARY?.dose1Date.isNullOrEmpty()) {
                                    //Book for dose 1
                                    if (vaccineType.isEmpty()
                                            .not() && vaccineType != Constants.BOTH_VACCINE
                                    ) {
                                        center?.sessions?.firstOrNull {
                                            it.availableCapacityDose1 > 0 && it.minAgeLimit == age && it.vaccine.equals(
                                                vaccineType,
                                                ignoreCase = true
                                            )
                                        }
                                    } else {
                                        center?.sessions?.firstOrNull { it.availableCapacityDose1 > 0 && it.minAgeLimit == age }
                                    }
                                } else {
                                    if (vaccineType.isEmpty()
                                            .not() && vaccineType != Constants.BOTH_VACCINE
                                    ) {
                                        center?.sessions?.firstOrNull {
                                            it.availableCapacityDose2 > 0 && it.minAgeLimit == age && it.vaccine.equals(
                                                vaccineType,
                                                ignoreCase = true
                                            )
                                        }
                                    } else {
                                        center?.sessions?.firstOrNull { it.availableCapacityDose2 > 0 && it.minAgeLimit == age }
                                    }
                                }

                            if (mAvailableSession != null) {
                                mAvailableSector = center
                                break
                            }
                        }
                    }
                }
                if (mAvailableSession != null) {
                    val cowinDaoModel = CowinDaoModel()
                    cowinDaoModel.timestamp =
                        getReadableDate(System.currentTimeMillis(), MMM_DD_YYYY_HH_MM_SS)
                    cowinDaoModel.code = 200
                    mCowinLiveData.postValue(cowinDaoModel)

                } else {
                    val cowinDaoModel = CowinDaoModel()
                    cowinDaoModel.timestamp =
                        getReadableDate(System.currentTimeMillis(), MMM_DD_YYYY_HH_MM_SS)
                    cowinDaoModel.error = CSBApp.mCSBApp.getString(R.string.no_slot_available)
                    cowinDaoModel.code = cowinCenters.code()
                    CowinDao.insertCowinData(cowinDaoModel)

                    mCowinLiveData.postValue(cowinDaoModel)
                }
            } else {
                val cowinDaoModel = CowinDaoModel()
                cowinDaoModel.timestamp =
                    getReadableDate(System.currentTimeMillis(), MMM_DD_YYYY_HH_MM_SS)
                cowinDaoModel.error = CSBApp.mCSBApp.getString(R.string.unauthorised)
                cowinDaoModel.code = cowinCenters.code()
                BEARER_TOKEN = ""
                CowinSharedPrefs.getIntstance(CSBApp.mCSBApp)
                    .storeStringPref(CowinSharedPrefs.KEY_BEARER_TOKEN, "")
                CowinDao.insertCowinData(cowinDaoModel)

                mCowinLiveData.postValue(cowinDaoModel)
            }
        }
    }

    fun scheduleAppointment() {
        viewModelScope.launch {
            val scheduleModel = ScheduleModel()
            scheduleModel.center_id = mAvailableSector?.centerId ?: 0L
            scheduleModel.session_id = mAvailableSession?.sessionId
            scheduleModel.slot = mAvailableSession?.slots?.last()

            if (SELECTED_BENEFICARY?.dose1Date.isNullOrEmpty()) {
                //Book for dose 1
                scheduleModel.dose = 1
            } else {
                //Book for dose 2
                scheduleModel.dose = 2
            }

            scheduleModel.beneficiaries = arrayOf(SELECTED_BENEFICARY?.beneficiaryReferenceId)

            val cowinCenters =
                mCowinAPIInterface.scheduleAppointment(scheduleModel, BEARER_TOKEN)
            if (cowinCenters.isSuccessful) {
                val cowinCenterResponse = cowinCenters.body()

                val cowinDaoModel = CowinDaoModel()
                cowinDaoModel.timestamp =
                    getReadableDate(System.currentTimeMillis(), MMM_DD_YYYY_HH_MM_SS)

                cowinDaoModel.confirmationNo =
                    "${mAvailableSession?.vaccine}\n${mAvailableSector?.address}\n${scheduleModel.slot}"
                CowinDao.insertCowinData(cowinDaoModel)

                mCowinScheduleLiveData.postValue(cowinDaoModel)
            } else {
                val string = Gson().fromJson(
                    cowinCenters.errorBody()?.string(),
                    ScheduleResponse::class.java
                )
                val cowinDaoModel = CowinDaoModel()
                cowinDaoModel.timestamp =
                    getReadableDate(System.currentTimeMillis(), MMM_DD_YYYY_HH_MM_SS)
                cowinDaoModel.error = string?.error
                CowinDao.insertCowinData(cowinDaoModel)

                mCowinScheduleLiveData.postValue(cowinDaoModel)
            }
        }
    }
}