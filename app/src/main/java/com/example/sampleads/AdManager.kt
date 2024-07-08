package com.example.sampleads

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.example.sampleads.CommonAds.BANNER_AD_KEY
import com.example.sampleads.CommonAds.INTERSTITIAL_ADS_KEY
import com.example.sampleads.CommonAds.NATIVE_AD_KEY_COMMON
import com.example.sampleads.CommonAds.OPEN_AD_KEY
import com.example.sampleads.CommonAds.REWARDED_ADS_KEY
import com.example.sampleads.CommonAds.REWARDED_INTER_ADS_KEY
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import org.greenrobot.eventbus.EventBus

class AdManager(private val context: Context) {
    private var rewardedAd: RewardedAd? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    var interAds: InterstitialAd? = null
    var appOpenAd: AppOpenAd? = null

    fun initialize(context: Context) {
        MobileAds.initialize(context)
        loadInterAd(context)
    }

    private fun loadInterAd(context: Context) {
        InterstitialAd.load(context, INTERSTITIAL_ADS_KEY, buildAdRequest(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Toast.makeText(context, "Load ad Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    interAds = interstitialAd
                    loadOpenAds()
                }
            })

    }

    fun loadOpenAds() {
        AppOpenAd.load(
            context,
            OPEN_AD_KEY,
            buildAdRequest(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    loadRewardInterAd()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Toast.makeText(context, "Load ad Failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun loadRewardInterAd() {
        RewardedInterstitialAd.load(
            context,
            REWARDED_INTER_ADS_KEY,
            buildAdRequest(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    loadRewardedAd()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Toast.makeText(context, "Load ad Failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun loadRewardedAd() {
        RewardedAd.load(
            context,
            REWARDED_ADS_KEY,
            buildAdRequest(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    EventBus.getDefault().post(EventLoadedAds(true))
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Toast.makeText(context, "Load ad Failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun loadBannerAd(activity: Activity, adSize: AdSize) : AdView {
        val adView = AdView(activity)
        adView.setAdSize(adSize)
        adView.adUnitId = BANNER_AD_KEY
        adView.loadAd(buildAdRequest())
        return adView
    }

    fun loadNativeAd(adLoadCallback: (NativeAd?) -> Unit) {
        val builder = AdLoader.Builder(context, NATIVE_AD_KEY_COMMON)
        builder.forNativeAd { nativeAd ->
            adLoadCallback(nativeAd)
        }
        val adLoader = builder.withNativeAdOptions(NativeAdOptions.Builder().build()).build()
        adLoader.loadAd(buildAdRequest())
    }

    fun showInterAd(activity: Activity, callback: FullScreenContentCallback) {
        interAds?.fullScreenContentCallback = callback
        interAds?.show(activity)
    }

    fun showAppOpenAd(activity: Activity) {
        appOpenAd?.show(activity)
    }

    fun showRewardedInterstitialAd(activity: Activity) {
        rewardedInterstitialAd?.show(activity) {}
    }

    fun showRewardedAd(activity: Activity) {
        rewardedAd?.show(activity) {}
    }

    private fun buildAdRequest(): AdManagerAdRequest {
        val request = AdManagerAdRequest.Builder().build()
        return request
    }
}