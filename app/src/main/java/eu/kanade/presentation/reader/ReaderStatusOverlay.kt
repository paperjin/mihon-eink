package eu.kanade.presentation.reader

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReaderStatusOverlay(
    currentPage: Int,
    totalPages: Int,
    currentChapter: String?,
    totalChapters: Int?,
    visible: Boolean,
) {
    val context = LocalContext.current
    var timeText by remember { mutableStateOf("") }
    var batteryText by remember { mutableStateOf("") }
    var wifiConnected by remember { mutableStateOf(false) }
    var batteryPct by remember { mutableStateOf(0) }
    var isCharging by remember { mutableStateOf(false) }

    // 12-hour format with AM/PM
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    
    LaunchedEffect(Unit) {
        while (true) {
            timeText = timeFormat.format(Date())
            
            // Update battery info
            val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            ContextCompat.registerReceiver(context, null, batteryFilter, ContextCompat.RECEIVER_NOT_EXPORTED)?.also { batteryStatus ->
                val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                batteryPct = if (scale > 0) (level * 100 / scale.toFloat()).toInt() else 0
                batteryText = "$batteryPct%"
                
                // Check if charging
                val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN)
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
            }
            
            // Update WiFi status
            val wifiFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
            ContextCompat.registerReceiver(context, null, wifiFilter, ContextCompat.RECEIVER_NOT_EXPORTED)?.also { wifiStatus ->
                val state = wifiStatus.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                wifiConnected = state == WifiManager.WIFI_STATE_ENABLED || state == WifiManager.WIFI_STATE_ENABLING
            }
            
            delay(60000) // Update every minute
        }
    }

    // Text style matching ReaderPageIndicator
    val timeStyle = TextStyle(
        color = Color(235, 235, 235),
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
    )
    val strokeStyle = timeStyle.copy(
        color = Color(45, 45, 45),
        drawStyle = Stroke(width = 4f),
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(0)),
        exit = fadeOut(animationSpec = tween(0)),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            
            // LEFT SIDE: Time, Battery, WiFi
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 2.dp, start = 8.dp),
            ) {
                Surface(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Time (stroke+text for outline)
                        Box {
                            Text(text = timeText, style = strokeStyle)
                            Text(text = timeText, style = timeStyle)
                        }
                        
                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(16.dp)
                                .background(Color(128, 128, 128, 64)),
                        )
                        
                        // Battery: text with outline
                        Box {
                            Text(text = batteryText, style = strokeStyle)
                            Text(text = batteryText, style = timeStyle)
                        }
                        
                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(16.dp)
                                .background(Color(128, 128, 128, 64)),
                        )
                        
                        // WiFi: text with outline
                        Box {
                            Text(text = if (wifiConnected) "WiFi" else "", style = strokeStyle)
                            Text(text = if (wifiConnected) "WiFi" else "", style = timeStyle)
                        }
                    }
                }
            }
            
            // RIGHT SIDE: Chapter + Page info
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 2.dp, end = 8.dp),
            ) {
                Surface(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Chapter info
                        if (currentChapter != null && totalChapters != null && totalChapters > 0) {
                            // Extract just the chapter number from strings like "Chapter002 (12MiB)" or "ch. 2"
                            val chapterNum = extractChapterNumber(currentChapter)
                            
                            val chapterText = "ch. $chapterNum/$totalChapters"
                            val pageText = "pg. $currentPage/$totalPages"
                            val combinedText = "$chapterText $pageText"
                            
                            Box {
                                Text(text = combinedText, style = strokeStyle)
                                Text(text = combinedText, style = timeStyle)
                            }
                        } else {
                            // Fallback to just page info
                            val pageText = "pg. $currentPage/$totalPages"
                            Box {
                                Text(text = pageText, style = strokeStyle)
                                Text(text = pageText, style = timeStyle)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Extract chapter number from various chapter title formats:
 * - "Chapter002 (12MiB)" → 2
 * - "ch. 2" → 2  
 * - "Chapter 2" → 2
 * - "002" → 2
 */
private fun extractChapterNumber(chapterTitle: String): Int {
    // Try to find digits after "Chapter" or "ch"
    val chapterRegex = Regex("(?:Chapter|ch)\\s*(\\d+)", RegexOption.IGNORE_CASE)
    val match = chapterRegex.find(chapterTitle)
    
    if (match != null) {
        return match.groupValues[1].toIntOrNull() ?: 0
    }
    
    // Fallback: try to get first number sequence
    val numberRegex = Regex("(\\d+)")
    val numberMatch = numberRegex.find(chapterTitle)
    if (numberMatch != null) {
        return numberMatch.groupValues[1].toIntOrNull() ?: 0
    }
    
    return 0
}
