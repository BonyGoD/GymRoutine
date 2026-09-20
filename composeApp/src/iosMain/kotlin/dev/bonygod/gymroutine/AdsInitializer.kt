package dev.bonygod.gymroutine

import dev.bonygod.admob.kmp.AdMobKMP
import dev.bonygod.admob.kmp.config.AdMobConfig
import dev.bonygod.admob.kmp.initializeAds

fun configureAds() {
    AdMobKMP.configure(
        AdMobConfig(
            iosBannerId = BuildConfig.ADMOB_IOS_BANNER,
            iosInterstitialId = BuildConfig.ADMOB_IOS_INTERSTITIAL,
            useTestAds = getPlatform().isDebugBuild || BuildConfig.ADMOB_USE_TEST_ADS,
            interstitialEnabled = true,
        ),
    )
    AdMobKMP.initializeAds()
}
