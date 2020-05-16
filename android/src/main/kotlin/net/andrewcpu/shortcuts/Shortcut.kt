package net.andrewcpu.shortcuts

import android.content.pm.ShortcutInfo
import android.graphics.drawable.Drawable

class Shortcut {
    var id: String = "Default ID"
    var packageName: String = "Default Package"
    var label: String = "Default Shortcut"
    var shortcutInfo: ShortcutInfo? = null
    lateinit var icon: Drawable

    constructor(id: String, packageName: String, label: String, shortcutInfo: ShortcutInfo?, icon: Drawable) {
        this.id = id
        this.packageName = packageName
        this.label = label
        this.shortcutInfo = shortcutInfo
        this.icon = icon
    }
}