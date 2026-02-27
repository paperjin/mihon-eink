package eu.kanade.tachiyomi.ui.eink

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class EinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Set light theme for better e-ink contrast
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
