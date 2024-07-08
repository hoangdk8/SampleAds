package com.example.sampleads

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sampleads.CommonAds.BANNER_AD_KEY
import com.example.sampleads.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adManager: AdManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adManager = AdManager(this)
        setupViews()

    }
    private val adSize: AdSize

        get() {
            val adContainerView = findViewById<FrameLayout>(R.id.layoutAds)
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = adContainerView.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }
    private fun setupViews() {
        binding.layoutAds.addView(adManager.loadBannerAd(this,adSize))
        adManager.loadRewardedAd()
        adManager.loadRewardInterAd()
        adManager.loadOpenAds()
        binding.btnNative.setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }
        binding.btnReward.setOnClickListener {
            adManager.showRewardedAd(this)
            adManager.loadRewardedAd()
        }
        binding.btnRewardInter.setOnClickListener {
            adManager.showRewardedInterstitialAd(this)
            adManager.loadRewardInterAd()
        }
        binding.btnOpenAds.setOnClickListener {
            adManager.showAppOpenAd(this)
            adManager.loadOpenAds()
        }
    }
}
