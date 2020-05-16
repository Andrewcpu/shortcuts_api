package net.andrewcpu.shortcuts

import android.content.Context
import android.content.pm.LauncherApps
import android.content.pm.LauncherApps.ShortcutQuery.*
import android.content.pm.ShortcutInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process.myUserHandle
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.util.*


class ShortcutAPI {
    lateinit var launcherApps: LauncherApps

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) {
        launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getShortcutFromApp(packageName: String): List<Shortcut> {
        val shortcutQuery = LauncherApps.ShortcutQuery()
        shortcutQuery.setQueryFlags(FLAG_MATCH_DYNAMIC or FLAG_MATCH_MANIFEST or FLAG_MATCH_PINNED)
        shortcutQuery.setPackage(packageName)
        return try {
            launcherApps.getShortcuts(shortcutQuery, myUserHandle())!!.map {
                Shortcut(it.id, it.`package`, it.shortLabel.toString(), it, launcherApps.getShortcutIconDrawable(it, 30))
            }
        } catch (e: SecurityException) {
            Collections.emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun getShortcutFromID(packageName: String, id: String): ShortcutInfo? {
        val shortcutQuery = LauncherApps.ShortcutQuery()
        shortcutQuery.setQueryFlags(FLAG_MATCH_DYNAMIC or FLAG_MATCH_MANIFEST or FLAG_MATCH_PINNED)
        shortcutQuery.setPackage(packageName)
        val shortcut = launcherApps.getShortcuts(shortcutQuery, myUserHandle())!!.asSequence()
                .filter { n -> n?.id == id }
                .first()
        return shortcut
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getShortcutIconFromApp(packageName: String, id: String): ByteArray {
        val shortcutInfo: ShortcutInfo = getShortcutFromID(packageName, id)!!
        val drw: Drawable = launcherApps.getShortcutIconDrawable(shortcutInfo, 100)
        val myLogo: Bitmap = getBitmapFromDrawable(drw)
        val outStream: ByteArrayOutputStream = ByteArrayOutputStream()
        myLogo.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        return outStream.toByteArray()
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, 100, 100)
        drawable.draw(canvas)
        return bmp
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun startShortcut(packageName: String, id: String) {
        launcherApps.startShortcut(packageName, id, null, null, myUserHandle())
    }
}