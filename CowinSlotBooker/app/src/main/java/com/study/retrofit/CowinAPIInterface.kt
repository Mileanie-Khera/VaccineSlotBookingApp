package com.study.retrofit

import com.study.models.*
import com.study.models.beneficiaries.BeneficiaryResponse
import retrofit2.Response
import retrofit2.http.*

interface CowinAPIInterface {

    @Headers(
        "Accept: application/json",
        "User-Agent: PostmanRuntime/7.26.8"
    )
    @GET("v2/appointment/sessions/calendarByPin")
    suspend fun getCowinCenters(
        @Header("authorization") authorization: String,
        @Query("pincode") pinCode: String,
        @Query("date") date: String
    ): Response<CowinCenterResponse>

    @Headers(
        "Accept: application/json",
        "User-Agent: PostmanRuntime/7.26.8"
    )
    @GET("v2/appointment/sessions/calendarByDistrict")
    suspend fun getCowinCentersByDistrict(
        @Header("authorization") authorization: String,
        @Query("district_id") districtID: String,
        @Query("date") date: String
    ): Response<CowinCenterResponse>

    @Headers(
        "Accept: application/json",
        "User-Agent: PostmanRuntime/7.26.8"
    )
    @POST("v2/appointment/schedule")
    suspend fun scheduleAppointment(
        @Body scheduleModel: ScheduleModel,
        @Header("authorization") authorization: String
    ): Response<ScheduleResponse>

    @Headers(
        "Accept: application/json",
        "User-Agent: PostmanRuntime/7.26.8"
    )
    @GET("v2/appointment/beneficiaries")
    suspend fun getBeneficiaries(
        @Header("authorization") authorization: String
    ): Response<BeneficiaryResponse>

    @Headers(
        "Accept: application/json",
        "User-Agent: PostmanRuntime/7.26.8"
    )
    @POST("v2/auth/generateMobileOTP")
    suspend fun generateMobileOTP(@Body generateOTPModel: GenerateOTPModel): Response<GenerateOTPResponse>

    @Headers(
        "Accept: application/json",
        "User-Agent: PostmanRuntime/7.26.8"
    )
    @POST("v2/auth/validateMobileOtp")
    suspend fun validateMobileOTP(@Body validateOTPModel: ValidateOTPModel): Response<ValidateOTPResponse>

}