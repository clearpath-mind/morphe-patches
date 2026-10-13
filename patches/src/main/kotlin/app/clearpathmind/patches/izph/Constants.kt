package app.clearpathmind.patches.izph

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_IZPH_VPN_PRO = Compatibility(
        name = "IZPH VPN PRO",
        packageName = "com.izphprov2.net",
        // The app is distributed as XAPK on APKPure, but Manager patches a plain
        // APK supplied by the user, so APK is declared here.
        apkFileType = ApkFileType.APK,
        appIconColor = 0x0B57D0,
        targets = listOf(
            // Developed and verified against 5.9 (versionCode 59).
            AppTarget(
                version = "5.9"
            )
        )
    )
}
