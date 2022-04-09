package com.study.learndagger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.study.adapters.BeneficiaryListAdapter
import com.study.preferrences.CowinSharedPrefs
import com.study.retrofit.Constants
import com.study.retrofit.Constants.TIME_DELAY
import com.study.utils.DD_MM_YYYY
import com.study.utils.getReadableDate
import com.study.utils.isDisplayAd
import com.study.viewmodels.BeneficiaryVM
import kotlinx.android.synthetic.main.activity_home_cowin.*

class CowinHomeActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private var beneficiaryListAdapter: BeneficiaryListAdapter? = null
    private lateinit var beneficiaryVM: BeneficiaryVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_cowin)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.user_information)

        MobileAds.initialize(this) {
//            Log.i("MobileAds", it.toString())
        }

        val db = Firebase.firestore

        val docRef = db.collection("Information").document("HomeScreen")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val topText = snapshot.get("TopText")?.toString()
                if (topText == null || topText.isEmpty()) {
                    firestore_text.visibility = View.GONE
                } else {
                    firestore_text.text = topText
                    firestore_text.visibility = View.VISIBLE
                }

            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.GENERAL_TOPIC)
            .addOnCompleteListener { }

        val mAdView: AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        Constants.BEARER_TOKEN = CowinSharedPrefs.getIntstance(this)
            .getStringPref(CowinSharedPrefs.KEY_BEARER_TOKEN)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val pinCode = findViewById<EditText>(R.id.pinCode)
        val vaccineTypeRadioGroup: RadioGroup = findViewById(R.id.vaccine_type_radio_group)
        val submit = findViewById<Button>(R.id.submit)

        submit.setOnClickListener {
            if (TextUtils.isEmpty(Constants.BEARER_TOKEN)) {
                startActivityForResult(Intent(this, LoginActivity::class.java), 123)
            } else {
                val beneficiary =
                    beneficiaryListAdapter?.beneficiaryList?.filter { it.isSelected }?.firstOrNull()
                if (beneficiary == null) {
                    Snackbar.make(
                        root_view,
                        getString(R.string.please_select_beneficiary),
                        Snackbar.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                if (pinCode.text.isEmpty()) {
                    Snackbar.make(
                        root_view,
                        getString(R.string.please_enter_pincode),
                        Snackbar.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                CowinSharedPrefs.getIntstance(this)
                    .storeStringPref(CowinSharedPrefs.KEY_PIN_CODE, pinCode.text.toString())

                val readableDate = getReadableDate(System.currentTimeMillis(), DD_MM_YYYY) ?: ""
                CowinSharedPrefs.getIntstance(this)
                    .storeStringPref(CowinSharedPrefs.KEY_DATE, readableDate)

                saveFeeType()
                saveAge()

                val checkedVaccineRadioText =
                    vaccineTypeRadioGroup.findViewById<RadioButton>(vaccineTypeRadioGroup.checkedRadioButtonId).text
                CowinSharedPrefs.getIntstance(this).storeStringPref(
                    CowinSharedPrefs.KEY_VACCINE_TYPE,
                    checkedVaccineRadioText.toString()
                )

                CowinSharedPrefs.getIntstance(this)
                    .storeLongPref(CowinSharedPrefs.KEY_TIME_DELAY, TIME_DELAY)

                Constants.SELECTED_BENEFICARY = beneficiary
                startActivityForResult(Intent(this, CowinActivity::class.java), 234)
            }
        }

        beneficiaryVM = ViewModelProviders.of(this).get(BeneficiaryVM::class.java)

        beneficiaryVM.mBeneficiaryResponseLiveData.observe(this, {
            beneficiaryListAdapter = BeneficiaryListAdapter(this, it.beneficiaries ?: ArrayList())
            recyclerView.adapter = beneficiaryListAdapter
        })

        beneficiaryVM.mErrorLiveData.observe(this, {
            if (it) {
                startActivityForResult(Intent(this, LoginActivity::class.java), 123)
                //Some error has occured. Refresh Token
            }
        })
        if (TextUtils.isEmpty(Constants.BEARER_TOKEN)) {
            startActivityForResult(Intent(this, LoginActivity::class.java), 123)
        } else {
            beneficiaryVM.getBeneficiaries()
            loadInterstitialAd()
        }
    }

    private fun saveFeeType() {
        val feeTypeRadioGroup: RadioGroup = findViewById(R.id.fee_type_radio_group)
        val checkedRadioText =
            feeTypeRadioGroup.findViewById<RadioButton>(feeTypeRadioGroup.checkedRadioButtonId).text
        CowinSharedPrefs.getIntstance(this)
            .storeStringPref(CowinSharedPrefs.KEY_FEE_TYPE, checkedRadioText.toString())
    }

    private fun saveAge() {
        val ageRadioGroup: RadioGroup = findViewById(R.id.age_radio_group)
        val checkedRadioText =
            ageRadioGroup.findViewById<RadioButton>(ageRadioGroup.checkedRadioButtonId).text
        CowinSharedPrefs.getIntstance(this)
            .storeStringPref(CowinSharedPrefs.KEY_AGE, checkedRadioText.toString())
    }

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
                        mInterstitialAd?.show(this@CowinHomeActivity)
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                Constants.BEARER_TOKEN = CowinSharedPrefs.getIntstance(this)
                    .getStringPref(CowinSharedPrefs.KEY_BEARER_TOKEN)
                beneficiaryVM.getBeneficiaries()
            } else {
                Constants.BEARER_TOKEN = ""
            }
        } else if (requestCode == 234) {
            if (resultCode == Activity.RESULT_OK) {
                beneficiaryVM.getBeneficiaries()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Constants.BEARER_TOKEN = CowinSharedPrefs.getIntstance(this)
            .getStringPref(CowinSharedPrefs.KEY_BEARER_TOKEN)
    }
}