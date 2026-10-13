package app.clearpathmind.patches.izph

import app.clearpathmind.patches.izph.Constants.COMPATIBILITY_IZPH_VPN_PRO
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove ads",
    default = true
) {
    compatibleWith(COMPATIBILITY_IZPH_VPN_PRO)

    execute {
        // Every target below only loads or shows an ad, so returning early is
        // safe. With no ad object ever loaded, the null-guarded show paths in
        // the app stay dormant and tapping earn-time buttons becomes a no-op.
        BannerLoadFingerprint.method.addInstructions(0, "return-void")
        InterstitialLoadFingerprint.method.addInstructions(0, "return-void")
        RewardedLoadFingerprint.method.addInstructions(0, "return-void")
        InterstitialShowFingerprint.method.addInstructions(0, "return-void")
        RewardedShowFingerprint.method.addInstructions(0, "return-void")
        EarnRewardedLoadFingerprint.method.addInstructions(0, "return-void")
        EarnRewardedInterstitialLoadFingerprint.method.addInstructions(0, "return-void")
    }
}
