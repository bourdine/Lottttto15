package com.example.lottttto11.mining

import android.content.Context
import com.example.lottttto11.data.Coin
import com.example.lottttto11.data.MergedMiningData
import com.example.lottttto11.utils.PowerManagerHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class MergedMiningEngine(
    private val context: Context,
    private val userId: Int,
    private val onBlockFound: (Coin, MergedMiningData) -> Unit,
    private val onStatsUpdate: (Coin, MiningStats) -> Unit
) {
    data class MiningStats(
        val hashrate: Double,
        val totalHashes: Long,
        val temperature: Float,
        val sharesSubmitted: Int
    )

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val clients = ConcurrentHashMap<Coin, StratumClient>()
    private val currentJobs = ConcurrentHashMap<Coin, StratumClient.MiningJob>()
    private val activeCoins = MutableStateFlow(setOf<Coin>())
    private val stats = ConcurrentHashMap<Coin, MiningStats>()
    private val isMining = AtomicBoolean(false)
    private val totalHashes = AtomicLong(0)
    private var lastHashCount = 0L
    private var lastTime = System.currentTimeMillis()
    private val sharesCounter = ConcurrentHashMap<Coin, Int>()
    private val poolIndexMap = ConcurrentHashMap<Coin, Int>()

    private var nativeThread: Thread? = null

    fun startMiningForCoin(coin: Coin, poolUrls: List<String>, wallet: String) {
        if (clients.containsKey(coin)) return
        poolIndexMap[coin] = 0
        createClientForCoin(coin, poolUrls, wallet, 0)
        activeCoins.value = activeCoins.value + coin
        sharesCounter[coin] = 0
        if (!isMining.getAndSet(true)) startNativeMining()
    }

    private fun createClientForCoin(coin: Coin, poolUrls: List<String>, wallet: String, index: Int) {
        val url = poolUrls[index]
        val client = StratumClient(
            coin = coin,
            poolUrl = url,
            wallet = wallet,
            onJobReceived = { job ->
                currentJobs[coin] = job
                if (job.cleanJobs) sharesCounter[coin] = 0
            },
            onDisconnected = {
                clients.remove(coin)
                currentJobs.remove(coin)
                val nextIndex = (index + 1) % poolUrls.size
                poolIndexMap[coin] = nextIndex
                createClientForCoin(coin, poolUrls, wallet, nextIndex)
            }
        )
        clients[coin] = client
        scope.launch { client.connect() }
    }

    fun stopMiningForCoin(coin: Coin) {
        clients[coin]?.disconnect()
        clients.remove(coin)
        currentJobs.remove(coin)
        sharesCounter.remove(coin)
        poolIndexMap.remove(coin)
        activeCoins.value = activeCoins.value - coin
        if (activeCoins.value.isEmpty()) stopNativeMining()
    }

    fun stopAll() {
        clients.values.forEach { it.disconnect() }
        clients.clear()
        currentJobs.clear()
        sharesCounter.clear()
        poolIndexMap.clear()
        activeCoins.value = emptySet()
        stopNativeMining()
    }

    private fun startNativeMining() {
        nativeThread = thread(start = true) {
            NativeMiner.startMining(object : NativeMiner.Callback {
                override fun onHashFound(nonce: ByteArray, hash: ByteArray) {
                    activeCoins.value.forEach { coin ->
                        val job = currentJobs[coin] ?: return@forEach
                        if (NativeMiner.checkShare(hash, job.nBits)) {
                            sharesCounter[coin] = sharesCounter[coin]?.plus(1) ?: 1
                            clients[coin]?.submitShare(job.jobId, job.nTime, nonce.toString())
                            if (isBlock(hash)) {
                                val reward = coin.blockReward
                                val usdValue = reward * coin.usdPrice
                                val blockData = MergedMiningData(
                                    id = "${coin.symbol}-${System.currentTimeMillis()}",
                                    coin = coin,
                                    blockHash = hash.joinToString("") { "%02x".format(it) },
                                    timestamp = System.currentTimeMillis(),
                                    reward = reward,
                                    usdValue = usdValue
                                )
                                onBlockFound(coin, blockData)
                            }
                        }
                    }
                    totalHashes.incrementAndGet()
                    updateStats()
                }
            })
        }
    }

    private fun stopNativeMining() {
        nativeThread?.interrupt()
        nativeThread = null
        isMining.set(false)
    }

    private fun isBlock(hash: ByteArray): Boolean {
        return hash[0] == 0.toByte() && hash[1] == 0.toByte() && hash[2] == 0.toByte()
    }

    private fun updateStats() {
        val now = System.currentTimeMillis()
        if (now - lastTime >= 5000) {
            val hashesDelta = totalHashes.get() - lastHashCount
            val timeDelta = now - lastTime
            val hashrate = hashesDelta * 1000.0 / timeDelta
            lastHashCount = totalHashes.get()
            lastTime = now
            val temp = PowerManagerHelper.getCpuTemperature()
            activeCoins.value.forEach { coin ->
                val shares = sharesCounter[coin] ?: 0
                val stat = MiningStats(hashrate, totalHashes.get(), temp, shares)
                onStatsUpdate(coin, stat)
            }
        }
    }

    object NativeMiner {
        init { System.loadLibrary("miningcore") }
        external fun startMining(callback: Callback)
        external fun stopMining()
        external fun checkShare(hash: ByteArray, target: String): Boolean
        interface Callback { fun onHashFound(nonce: ByteArray, hash: ByteArray) }
    }
}
