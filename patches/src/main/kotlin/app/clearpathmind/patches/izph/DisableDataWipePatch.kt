package app.clearpathmind.patches.izph

import app.clearpathmind.patches.izph.Constants.COMPATIBILITY_IZPH_VPN_PRO
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
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

        // The trojanized ads init provider wipes data in its static
        // initializer (runs before Application startup) when the APKPure
        // signature files are absent. Remove only the wipe call and keep
        // the rest of provider init intact.
        ProviderInitFingerprint.apply {
            val wipeIndex = this.instructionMatches.first().index
            this.method.removeInstruction(wipeIndex)
        }

        // The native RenzUtils.initializeApp check kills re-signed builds
        // during Application startup. Its reflective call is the last
        // Method.invoke in IZPHPROApplication.onCreate; remove only it so
        // the native library still loads and the other checks still run.
        NativeInitCallFingerprint.apply {
            val initIndex = this.instructionMatches.last().index
            this.method.removeInstruction(initIndex)
        }
    }
}
