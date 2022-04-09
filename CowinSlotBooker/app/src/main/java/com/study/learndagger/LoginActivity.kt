package com.study.learndagger

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.study.preferrences.CowinSharedPrefs
import com.study.retrofit.Constants.BEARER_TOKEN
import com.study.utils.sha256
import com.study.viewmodels.LoginVM
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private var mTxnId: String = ""
    private var mTimer: CountDownTimer? = null
    private lateinit var loginVM: LoginVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.login)

        loginVM = ViewModelProviders.of(this).get(LoginVM::class.java)

        loginVM.mGenerateOTPLiveData.observe(this, {
            if (it.txnId != null) {
                mTxnId = it.txnId
                startTimer()
                val lastFourMobileNum = mobileNum_Ed.text.toString().takeLast(4)
                enter_otp.text = "An OTP has been sent to XXX XXX $lastFourMobileNum"
                otp_lv.visibility = View.VISIBLE
                mobileNum_Ed.visibility = View.GONE

                generateOtp.text = getString(R.string.verify_otp)
            }
        })

        loginVM.mValidateOTPLiveData.observe(this, {
            if (it != null) {
                BEARER_TOKEN = "Bearer ${it.token}"
                CowinSharedPrefs.getIntstance(this)
                    .storeStringPref(CowinSharedPrefs.KEY_BEARER_TOKEN, "Bearer ${it.token}")
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        loginVM.mErrorLiveData.observe(this, {
            if (it != null && it.error != null) {
                Snackbar.make(generateOtp, it.error, Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                Snackbar.make(
                    otp_lv,
                    getString(R.string.something_went_wrong),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        })

        otp_view.setOtpCompletionListener { otp -> // do Stuff
            Log.d("onOtpCompleted=>", otp)
        }
        otp_timer.setOnClickListener {
            if (otp_timer.text == getString(R.string.resend_otp)) {
                val mobileNum = mobileNum_Ed.text.toString()
                loginVM.generateOTP(mobileNum)
            }
        }
        edit_mobile.setOnClickListener {
            otp_lv.visibility = View.GONE
            mobileNum_Ed.visibility = View.VISIBLE
            otp_timer.visibility = View.GONE
            generateOtp.text = getString(R.string.generate_otp)
            mTimer?.cancel()
        }

        generateOtp.setOnClickListener {
            if (generateOtp.text == getString(R.string.verify_otp)) {
                loginVM.verifyOTP(otp_view.text.toString().sha256(), mTxnId)
            } else {
                val mobileNum = mobileNum_Ed.text.toString()
                if (mobileNum.isBlank() || mobileNum.length < 10) {
                    Snackbar.make(it, "Please enter 10 digit mobile number", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    loginVM.generateOTP(mobileNum)
                }
            }
        }
    }

    private fun startTimer() {
        mTimer?.cancel()
        mTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                otp_timer.text = "Resend OTP in ${millisUntilFinished / 1000} seconds"
                otp_timer.visibility = View.VISIBLE
            }

            override fun onFinish() {
                otp_timer.text = getString(R.string.resend_otp)
                otp_timer.visibility = View.VISIBLE
            }
        }
        mTimer?.start()
    }

}