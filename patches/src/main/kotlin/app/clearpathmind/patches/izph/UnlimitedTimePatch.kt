package app.clearpathmind.patches.izph

import app.clearpathmind.patches.izph.Constants.COMPATIBILITY_IZPH_VPN_PRO
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val unlimitedTimePatch = bytecodePatch(
    name = "Unlimited connection time",
    description = "Prevents the VPN from disconnecting when the connection timer runs out.",
    default = true
) {
    compatibleWith(COMPATIBILITY_IZPH_VPN_PRO)

    dependsOn(disableDataWipePatch)

    execute {
        // Connection time is enforced by defpackage.j3 countdown timers whose
        // on-finish callbacks tear down the VPN. Nopping the three callbacks
        // keeps the connection up after the timer reaches zero:
        // - z50.a calls IZPHPROMain.N0 (main countdown expiry disconnect).
        // - TimeManagerService$a.a launches VPNDisconnect (service expiry).
        // - EarnTimeManagerActivity$b.a stops RenzHttpService (earn timer).
        MainTimerFinishFingerprint.method.addInstructions(0, "return-void")
        ServiceTimerFinishFingerprint.method.addInstructions(0, "return-void")
        EarnTimerFinishFingerprint.method.addInstructions(0, "return-void")
    }
}
