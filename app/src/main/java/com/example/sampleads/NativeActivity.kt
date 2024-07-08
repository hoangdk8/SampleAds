package com.example.sampleads

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleads.databinding.ActivityNativeBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class NativeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNativeBinding
    private lateinit var adManager: AdManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }
    private fun setupViews() {
        adManager = AdManager(this)
        adManager.loadNativeAd { nativeAd ->
            if (nativeAd != null) {
                displayNativeAd(nativeAd)
            }
        }
    }

    private fun displayNativeAd(nativeAd: NativeAd) {
        val adView = layoutInflater.inflate(R.layout.native_ads, null) as NativeAdView
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_des)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction
        adView.setNativeAd(nativeAd)
        binding.layoutAdsAb.removeAllViews()
        binding.layoutAdsAb.addView(adView)
    }
}