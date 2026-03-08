package com.example.lottttto11.mining

import com.example.lottttto11.data.Coin
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class StratumClient(
    val coin: Coin,
    private val poolUrl: String,
    private val wallet: String,
    private val onJobReceived: (MiningJob) -> Unit,
    private val onDisconnected: () -> Unit
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private var socket: WebSocket? = null
    private val requestId = AtomicLong(1)
    private var extranonce1 = ""
    private var extranonce2Size = 4
    private var extranonce2 = 0

    data class MiningJob(
        val coin: Coin,
        val jobId: String,
        val prevHash: String,
        val coinbase1: String,
        val coinbase2: String,
        val merkleBranch: List<String>,
        val version: String,
        val nBits: String,
        val nTime: String,
        val cleanJobs: Boolean
    )

    suspend fun connect() = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(poolUrl).build()
        socket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                subscribe()
                authorize()
            }
            override fun onMessage(webSocket: WebSocket, text: String) = handleMessage(text)
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                t.printStackTrace()
                onDisconnected()
            }
        })
    }

    private fun subscribe() {
        val msg = JSONObject().apply {
            put("id", requestId.incrementAndGet())
            put("method", "mining.subscribe")
            put("params", listOf("Lottttto11Miner/1.0"))
        }.toString()
        socket?.send(msg)
    }

    private fun authorize() {
        val msg = JSONObject().apply {
            put("id", requestId.incrementAndGet())
            put("method", "mining.authorize")
            put("params", listOf("$wallet.worker", "x"))
        }.toString()
        socket?.send(msg)
    }

    private fun handleMessage(text: String) {
        try {
            val json = JSONObject(text)
            when {
                json.has("method") -> when (json.getString("method")) {
                    "mining.notify" -> handleNotify(json)
                    "mining.set_difficulty" -> Unit
                    "mining.set_extranonce" -> handleSetExtranonce(json)
                }
                json.has("result") -> Unit
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun handleNotify(json: JSONObject) {
        val params = json.getJSONArray("params")
        val job = MiningJob(
            coin = coin,
            jobId = params.getString(0),
            prevHash = params.getString(1),
            coinbase1 = params.getString(2),
            coinbase2 = params.getString(3),
            merkleBranch = List(params.getJSONArray(4).length()) { i -> params.getJSONArray(4).getString(i) },
            version = params.getString(5),
            nBits = params.getString(6),
            nTime = params.getString(7),
            cleanJobs = params.getBoolean(8)
        )
        if (job.cleanJobs) extranonce2 = 0
        onJobReceived(job)
    }

    private fun handleSetExtranonce(json: JSONObject) {
        val params = json.getJSONArray("params")
        extranonce1 = params.getString(0)
        extranonce2Size = params.getInt(1)
    }

    fun submitShare(jobId: String, nTime: String, nonce: String) {
        val msg = JSONObject().apply {
            put("id", requestId.incrementAndGet())
            put("method", "mining.submit")
            put("params", listOf(
                "$wallet.worker",
                jobId,
                extranonce2.toString(),
                nTime,
                nonce
            ))
        }.toString()
        socket?.send(msg)
    }

    fun disconnect() {
        socket?.close(1000, "Normal closure")
        socket = null
    }
}
