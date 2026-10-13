package app.clearpathmind.patches.izph

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

private const val MAIN = "Lrenz/javacodez/vpn/activities/IZPHPROMain;"
private const val EARN = "Lrenz/javacodez/vpn/activities/EarnTimeManagerActivity;"
private const val INTERSTITIAL_AD = "Lcom/google/android/libraries/ads/mobile/sdk/interstitial/InterstitialAd;"
private const val REWARDED_AD = "Lcom/google/android/libraries/ads/mobile/sdk/rewarded/RewardedAd;"
private const val REWARDED_INTERSTITIAL_AD =
    "Lcom/google/android/libraries/ads/mobile/sdk/rewardedinterstitial/RewardedInterstitialAd;"

// IZPHPROMain.k0: builds the banner request and calls AdView.loadAd.
object BannerLoadFingerprint : Fingerprint(
    definingClass = MAIN,
    name = "k0",
    returnType = "V",
    filters = listOf(
        methodCall(name = "loadAd")
    )
)

// IZPHPROMain.m0: calls InterstitialAd.load when ads are enabled.
object InterstitialLoadFingerprint : Fingerprint(
    definingClass = MAIN,
    name = "m0",
    returnType = "V",
    filters = listOf(
        methodCall(definingClass = INTERSTITIAL_AD, name = "load")
    )
)

// IZPHPROMain.o0: calls RewardedAd.load when ads are enabled.
object RewardedLoadFingerprint : Fingerprint(
    definingClass = MAIN,
    name = "o0",
    returnType = "V",
    filters = listOf(
        methodCall(definingClass = REWARDED_AD, name = "load")
    )
)

// IZPHPROMain.C0: shows the cached interstitial (L.show) on connect flows.
object InterstitialShowFingerprint : Fingerprint(
    definingClass = MAIN,
    name = "C0",
    returnType = "V",
    filters = listOf(
        methodCall(definingClass = INTERSTITIAL_AD, name = "show")
    )
)

// IZPHPROMain.P: static helper that shows the cached rewarded ad (Q.show).
object RewardedShowFingerprint : Fingerprint(
    definingClass = MAIN,
    name = "P",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    parameters = listOf(MAIN),
    filters = listOf(
        methodCall(definingClass = REWARDED_AD, name = "show")
    )
)

// EarnTimeManagerActivity.J: loads the rewarded ad used to earn time.
object EarnRewardedLoadFingerprint : Fingerprint(
    definingClass = EARN,
    name = "J",
    returnType = "V",
    filters = listOf(
        methodCall(definingClass = REWARDED_AD, name = "load")
    )
)

// EarnTimeManagerActivity.K: loads the rewarded interstitial used to earn time.
object EarnRewardedInterstitialLoadFingerprint : Fingerprint(
    definingClass = EARN,
    name = "K",
    returnType = "V",
    filters = listOf(
        methodCall(definingClass = REWARDED_INTERSTITIAL_AD, name = "load")
    )
)

// z50.a: main countdown finish callback, calls IZPHPROMain.N0 which
// stops the VPN service and resets the UI.
object MainTimerFinishFingerprint : Fingerprint(
    definingClass = "Ldefpackage/z50;",
    name = "a",
    returnType = "V",
    filters = listOf(
        methodCall(definingClass = MAIN, name = "N0")
    )
)

// TimeManagerService$a.a: service countdown finish callback, launches
// VPNDisconnect which tears down the VPN connection.
object ServiceTimerFinishFingerprint : Fingerprint(
    definingClass = "Lrenz/javacodez/vpn/service/TimeManagerService\$a;",
    name = "a",
    returnType = "V",
    filters = listOf(
        methodCall(name = "stopForeground")
    )
)

// EarnTimeManagerActivity$b.a: earn-screen countdown finish callback,
// stops RenzHttpService. Nopped so an expired earn timer cannot kill
// an active connection.
object EarnTimerFinishFingerprint : Fingerprint(
    definingClass = "Lrenz/javacodez/vpn/activities/EarnTimeManagerActivity\$b;",
    name = "a",
    returnType = "V",
    filters = listOf(
        methodCall(
            definingClass = "Lrenz/javacodez/vpn/service/RenzHttpService\$c;",
            name = "e"
        )
    )
)
