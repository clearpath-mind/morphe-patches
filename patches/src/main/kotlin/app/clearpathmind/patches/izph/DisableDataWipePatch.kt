package app.clearpathmind.patches.izph

import app.clearpathmind.patches.izph.Constants.COMPATIBILITY_IZPH_VPN_PRO
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val disableDataWipePatch = bytecodePatch(
    name = "Disable data wipe",
    description = "Stops the app from clearing its data when startup integrity checks fail on patched builds. Also disables the in-app reset option.",
    default = true
) {
    compatibleWith(COMPATIBILITY_IZPH_VPN_PRO)

    execute {
        // Any repackaged build trips the startup checks (repackaging adds
        // classes such as the patcher extension DEX, which fail the DEX
        // class allowlist), and the app responds with clearApplicationUserData
        // plus a restart, so no patched build can boot. Nopping the single
        // wipe routine disarms all of them at once.
        ResetAppFingerprint.method.addInstructions(0, "return-void")
    }
}
