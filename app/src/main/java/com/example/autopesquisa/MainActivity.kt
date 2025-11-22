package com.example.autopesquisa

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartNow: Button
    private lateinit var btnScheduleDaily: Button
    private lateinit var btnStop: Button
    private lateinit var txtLog: TextView
    private lateinit var scroll: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartNow = findViewById(R.id.btnStartNow)
        btnScheduleDaily = findViewById(R.id.btnScheduleDaily)
        btnStop = findViewById(R.id.btnStop)
        txtLog = findViewById(R.id.txtLog)
        scroll = findViewById(R.id.scrollViewLogs)

        btnStartNow.setOnClickListener {
            appendLog("Iniciando execução imediata...")
            val work = OneTimeWorkRequestBuilder<SearchWorker>().build()
            WorkManager.getInstance(applicationContext).enqueue(work)
        }

        btnScheduleDaily.setOnClickListener {
            appendLog("Agendando execução diária (intervalo 24h)...")
            // PeriodicWorkRequest with 24 hours interval
            val periodic = PeriodicWorkRequestBuilder<SearchWorker>(24, TimeUnit.HOURS).build()
            WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork("daily_search", ExistingPeriodicWorkPolicy.REPLACE, periodic)
            appendLog("Agendamento salvo como 'daily_search'.")
        }

        btnStop.setOnClickListener {
            appendLog("Parando/agora cancelando trabalhos programados...")
            WorkManager.getInstance(applicationContext).cancelUniqueWork("daily_search")
            appendLog("Trabalhos diários cancelados.")
        }
    }

    override fun onResume() {
        super.onResume()
        // Load last logs (SearchWorker writes to prefs)
        val prefs = getSharedPreferences("autopesquisa_prefs", MODE_PRIVATE)
        val logs = prefs.getString("logs", "")
        txtLog.text = logs
        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    fun appendLog(text: String) {
        val prefs = getSharedPreferences("autopesquisa_prefs", MODE_PRIVATE)
        val old = prefs.getString("logs", "") ?: ""
        val now = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())
        val new = old + "\n" + "[$now] " + text
        prefs.edit().putString("logs", new).apply()

        txtLog.text = new
        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }
}
