package dev.bonygod.gymroutine

import android.app.Application
import dev.bonygod.admob.kmp.AdMobKMP
import dev.bonygod.admob.kmp.config.AdMobConfig
import dev.bonygod.admob.kmp.initializeAds
import dev.bonygod.gymroutine.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class GymRoutineApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GymRoutineApp)
        }

        AdMobKMP.configure(
            AdMobConfig(
                androidBannerId = BuildConfig.ADMOB_ANDROID_BANNER,
                androidInterstitialId = BuildConfig.ADMOB_ANDROID_INTERSTITIAL,
                iosBannerId = BuildConfig.ADMOB_IOS_BANNER,
                iosInterstitialId = BuildConfig.ADMOB_IOS_INTERSTITIAL,
                useTestAds = BuildConfig.ADMOB_USE_TEST_ADS,
                interstitialEnabled = true,
            ),
        )
        AdMobKMP.initializeAds(this)
    }
}
