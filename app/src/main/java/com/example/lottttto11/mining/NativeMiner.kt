package com.example.lottttto11.mining

class NativeMiner {
    companion object {
        init {
            System.loadLibrary("miningcore")
        }
        external fun initialize(callback: Callback)
        external fun submitJob(algorithm: Int, header: ByteArray, target: ByteArray, startNonce: Int, endNonce: Int, jobId: Long)
        external fun stop()
        external fun checkShare(hash: ByteArray, target: String): Boolean

        interface Callback {
            fun onResult(jobId: Long, nonce: Int, hash: ByteArray)
        }

        const val ALGO_SHA256 = 0
        const val ALGO_SCRYPT = 1
        const val ALGO_RANDOM_X = 2
    }
}
