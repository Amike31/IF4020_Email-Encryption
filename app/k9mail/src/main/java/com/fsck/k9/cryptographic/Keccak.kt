package com.fsck.k9.cryptographic

class Keccak {
    fun keccak256(message: ByteArray): ByteArray {
        val state = LongArray(25)
        val stateBytes = ByteArray(200)

        // Absorb phase
        for (i in message.indices) {
            stateBytes[i] = message[i]
        }

        keccakF(state, stateBytes)

        // Squeeze phase
        val hash = ByteArray(32)
        for (i in 0 until 32) {
            hash[i] = state[i ushr 3].ushr((i and 7) * 8).toByte()
        }

        return hash
    }

    private fun keccakF(state: LongArray, stateBytes: ByteArray) {
        for (round in 0 until 24) {
            theta(state)
            rho(state, stateBytes)
            pi(state)
            chi(state)
            iota(state, round)
        }
    }

    private fun theta(state: LongArray) {
        val c = LongArray(5)
        val d = LongArray(5)

        for (x in 0 until 5) {
            c[x] = state[x] xor state[x + 5] xor state[x + 10] xor state[x + 15] xor state[x + 20]
        }

        for (x in 0 until 5) {
            d[x] = c[(x + 4) % 5] xor rotateLeft(c[(x + 1) % 5], 1)
        }

        for (x in 0 until 5) {
            for (y in 0 until 25 step 5) {
                state[x + y] = state[x + y] xor d[x]
            }
        }
    }

    private fun rho(state: LongArray, stateBytes: ByteArray) {
        for (x in 1 until 25 step 2) {
            val temp = state[x]
            state[x] = rotateLeft(state[x], keccakRhoOffsets[x])
            stateBytes[x * 8 + 1] = state[x].toByte()
            state[x] = temp
        }
    }

    private fun pi(state: LongArray) {
        val temp = LongArray(25)
        for (x in 0 until 25) {
            temp[x] = state[keccakRhoPiOffsets[x]]
        }
        for (x in 0 until 25) {
            state[x] = temp[x]
        }
    }

    private fun chi(state: LongArray) {
        val chiTemp = LongArray(5)
        for (y in 0 until 25 step 5) {
            for (x in 0 until 5) {
                chiTemp[x] = state[x + y]
            }
            for (x in 0 until 5) {
                state[x + y] = chiTemp[x] xor (chiTemp[(x + 1) % 5] and chiTemp[(x + 2) % 5]).inv()
            }
        }
    }

    private fun iota(state: LongArray, round: Int) {
        state[0] = state[0] xor keccakRoundConstants[round]
    }

    private fun rotateLeft(value: Long, n: Int): Long {
        return value shl n or (value ushr (64 - n))
    }

    private val keccakRhoOffsets = intArrayOf(
        0, 1, 62, 28,
        27, 36, 44, 6,
        55, 20, 3, 10,
        43, 25, 39, 41,
        45, 15, 21, 8,
        18, 2, 61, 56
    )

    private val keccakRhoPiOffsets = intArrayOf(
        0, 1, 190, 28,
        91, 36, 300, 6,
        55, 276, 3, 10,
        171, 253, 39, 41,
        317, 15, 21, 89,
        18, 2, 120, 56
    )

    private val keccakRoundConstants = longArrayOf(
        1, 32898, 8399994, 2147483649,
        32907, 2147483658, 2147483659, 32914,
        32923, 8399976, 8399943, 32932,
        8399930, 8399907, 2147483664, 2147483665,
        32953, 2147483674, 8399890, 2147483676,
        2147483677, 2147483678, 2147483679, 32971
    )
}
