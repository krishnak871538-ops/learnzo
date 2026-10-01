package com.example.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object RewardedAdManager {
    private const val TAG = "RewardedAdManager"
    // User requested rewarded test ad ID: ca-app-pub-3940256099942544/5224354917
    const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    private var rewardedAd: RewardedAd? = null
    private var isAdLoading = false
    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return
        try {
            MobileAds.initialize(context) {
                isInitialized = true
                Log.d(TAG, "Google Mobile Ads initialized successfully")
                loadAd(context)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing MobileAds: ${e.message}")
        }
    }

    fun loadAd(context: Context, onLoaded: (() -> Unit)? = null) {
        if (isAdLoading || rewardedAd != null) {
            onLoaded?.invoke()
            return
        }

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            TEST_REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isAdLoading = false
                    Log.d(TAG, "Rewarded test ad loaded successfully!")
                    onLoaded?.invoke()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                    isAdLoading = false
                    Log.w(TAG, "Failed to load rewarded ad: ${loadAdError.message}")
                }
            }
        )
    }

    fun isAdAvailable(): Boolean = rewardedAd != null

    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: (rewardAmount: Int) -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    Log.d(TAG, "Rewarded ad dismissed, preloading next ad...")
                    loadAd(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    Log.e(TAG, "Failed to show ad: ${adError.message}")
                    // Graceful fallback: Still reward child if ad failed to show
                    onRewardEarned(1)
                    onAdDismissed()
                }
            }

            ad.show(activity) { rewardItem ->
                val amount = rewardItem.amount.takeIf { it > 0 } ?: 1
                Log.d(TAG, "User earned reward: $amount")
                onRewardEarned(amount)
            }
        } else {
            Log.w(TAG, "Rewarded ad not loaded yet, triggering graceful reward fallback")
            // Immediate graceful fallback so the child's learning/surprise experience isn't blocked
            onRewardEarned(1)
            loadAd(activity)
            onAdDismissed()
        }
    }
}
