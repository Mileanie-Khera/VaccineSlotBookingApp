package com.study.learndagger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.study.database.CowinDao
import com.study.preferrences.CowinSharedPrefs
import com.study.retrofit.Constants.*
import com.study.utils.*
import com.study.viewmodels.CowinVM
import java.util.*
import kotlin.collections.ArrayList

//Directed Acyclic Graph - Dagger
//JSR 330 Java Specification Request
class CowinActivity : AppCompatActivity() {

    private val REQUEST_LOGIN = 123
    private lateinit var cowinVM: CowinVM
    var counter: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cowin)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.book_slot)

        val mAdView: AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        PIN_CODE = CowinSharedPrefs.getIntstance(this).getStringPref(CowinSharedPrefs.KEY_PIN_CODE)
        val readableDate = parseStringToFormattedStringDate(
            CowinSharedPrefs.getIntstance(this).getStringPref(CowinSharedPrefs.KEY_DATE),
            DD_MM_YYYY,
            TARGET_FORMAT
        )
        FEE_TYPE = CowinSharedPrefs.getIntstance(this).getStringPref(CowinSharedPrefs.KEY_FEE_TYPE)
        TIME_DELAY =
            CowinSharedPrefs.getIntstance(this).getLongPref(CowinSharedPrefs.KEY_TIME_DELAY)

        counter = findViewById(R.id.counter)

        val dose = if (SELECTED_BENEFICARY?.dose1Date.isNullOrEmpty()) {
            //Book for dose 1
            1
        } else {
            //Book for dose 2
            2
        }

        val parameterList = findViewById<TextView>(R.id.parameterList)
        parameterList.text = "" +
                "Pin Code: " + PIN_CODE + "\n" +
                "Beneficiary Name: " + SELECTED_BENEFICARY?.name + "\n" +
                "Dose: " + dose + "\n" +
                "Fee Type: " + FEE_TYPE

        cowinVM = ViewModelProviders.of(this).get(CowinVM::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        cowinVM.getCowinLiveData().observe(this, Observer {

            val cowinListAdapter = CowinListAdapter(CowinDao.getCowinData() ?: ArrayList())
            recyclerView.adapter = cowinListAdapter

            if (it.error == getString(R.string.no_slot_available)) {
                cowinVM.checkSlotAvailability(TIME_DELAY)
                startTimer()
            } else if (it.error == getString(R.string.unauthorised)) {
                startActivityForResult(
                    Intent(applicationContext, LoginActivity::class.java),
                    REQUEST_LOGIN
                )
            } else {
                //Log.e("Status", "Found centers : Booking")
                cowinVM.scheduleAppointment()
            }
        })

        cowinVM.getCowinScheduleLiveData().observe(this, Observer {
            val cowinListAdapter = CowinListAdapter(CowinDao.getCowinData() ?: ArrayList())
            recyclerView.adapter = cowinListAdapter
            if (TextUtils.isEmpty(it.error).not()) {
                cowinVM.checkSlotAvailability(2000)
                startTimer()
            } else {
                //Appointment Booked
                sendResult = true
            }
        })

        if (BEARER_TOKEN.isNotEmpty()) {
            cowinVM.checkSlotAvailability(1000)
            startTimer()
        }
    }

    private var sendResult = false
    override fun onBackPressed() {
        if (sendResult) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_LOGIN) {
            cowinVM.checkSlotAvailability(1000)
            startTimer()
        }
    }

    private var mTimer = Timer()
    private var mTimerAdCount = 0
    private fun startTimer() {
        mTimer.cancel()
        mTimer = Timer()
        val delayTime =
            CowinSharedPrefs.getIntstance(this).getLongPref(CowinSharedPrefs.KEY_TIME_DELAY)
        var remainingSeconds = delayTime / 1000
        mTimerAdCount += 1
        mTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    //Log.e("CA", "remainingSeconds:$remainingSeconds")
                    if (remainingSeconds < 1)
                        remainingSeconds = delayTime / 1000
                    counter?.text = "Refreshing slots in $remainingSeconds seconds"
                    remainingSeconds -= 1
                }
            }

        }, 0, 1000)

        if (mTimerAdCount > 3) {
            mTimerAdCount = 0
            loadInterstitialAd()
        }
    }

    private var mInterstitialAd: InterstitialAd? = null
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        val adUnitId = if (BuildConfig.DEBUG) {
            getString(R.string.google_test_interstital_ad)
        } else {
            getString(R.string.google_interstital_ad)
        }

        InterstitialAd.load(
            this,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    if (isDisplayAd()) {
                        mInterstitialAd?.show(this@CowinActivity)
                    }
                }
            })
    }
}