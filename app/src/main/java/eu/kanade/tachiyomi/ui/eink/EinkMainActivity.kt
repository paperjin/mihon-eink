package eu.kanade.tachiyomi.ui.eink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Main activity for Mihon E-Ink frontend.
 * Uses page-based navigation instead of scrolling.
 */
class EinkMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Set content view with page-based UI
        setContentView(R.layout.activity_main)
    }
}
