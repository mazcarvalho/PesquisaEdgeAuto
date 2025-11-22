package com.example.autopesquisa

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class SearchWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val termos = listOf(
        "tecnologia","android","clima hoje","notícias",
        "curiosidades","carros elétricos","futebol",
        "treino de academia","alimentação saudável",
        "planetas","história do brasil","energia solar",
        "bicicletas","notícias do mundo","jogos online",
        "python","java","kotlin","marketing digital",
        "motos esportivas","filmes novos","séries famosas",
        "músicas mais tocadas","praias do brasil",
        "receitas fáceis","cachorros","gatos",
        "inteligência artificial","robôs","espaço sideral",
        "salvador bahia","curiosidades humanas"
    )

    override suspend fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("autopesquisa_prefs", Context.MODE_PRIVATE)
        val used = mutableSetOf<String>()
        val termsList = termos.shuffled()

        // We will run up to 30 searches (or less if termo list shorter)
        val max = minOf(30, termsList.size)

        for (i in 0 until max) {
            val termo = termsList[i]
            // save log
            appendLog("Abrindo Edge para pesquisar: " + termo)

            abrirNoEdge(termo)

            // wait 10 seconds between searches
            delay(10_000)
        }

        appendLog("Execução de ${max} pesquisas finalizada.")
        return Result.success()
    }

    private fun abrirNoEdge(termo: String) {
        val url = "microsoft-edge:https://www.bing.com/search?q=" + Uri.encode(termo)
        val edgeUri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, edgeUri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            applicationContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Edge não instalado; escreve log
            appendLog("Falha: Microsoft Edge não encontrado. Termo: $termo")
        } catch (t: Throwable) {
            appendLog("Erro abrindo Edge: ${t.localizedMessage}")
        }
    }

    private fun appendLog(message: String) {
        val prefs = applicationContext.getSharedPreferences("autopesquisa_prefs", Context.MODE_PRIVATE)
        val old = prefs.getString("logs", "") ?: ""
        val now = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())
        val new = old + "\n" + "[$now] " + message
        prefs.edit().putString("logs", new).apply()
    }
}
