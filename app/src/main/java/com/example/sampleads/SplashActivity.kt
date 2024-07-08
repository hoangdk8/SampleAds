package com.example.sampleads

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleads.databinding.ActivitySplashBinding
import com.google.android.gms.ads.FullScreenContentCallback
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var adManager: AdManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)
        setupViews()
    }

    private fun setupViews() {
        adManager = AdManager(this)
        adManager.initialize(this)
    }

    @Subscribe
    fun onShowAdInter(event: EventLoadedAds) {
        binding.progressBar.visibility = View.GONE
        adManager.showInterAd(this, object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}