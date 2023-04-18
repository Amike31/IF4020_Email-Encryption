package com.fsck.k9.ui.cryptographic

class Keccak {
    private val keccakRoundConstants = longArrayOf(
        1, 32898, 8399994, 2147483649,
        32907, 2147483658, 2147483659, 32914,
        32923, 8399976, 8399943, 32932,
        8399930, 8399907, 2147483664, 2147483665,
        32953, 2147483674, 8399890, 2147483676,
        2147483677, 2147483678, 2147483679, 32971
//        0x0000000000000001U.toLong(), 0x0000000000008082U.toLong(), 0x800000000000808AU.toLong(),
//        0x8000000080008000U.toLong(), 0x000000000000808BU.toLong(), 0x0000000080000001U.toLong(),
//        0x8000000080008081U.toLong(), 0x8000000000008009U.toLong(), 0x000000000000008AU.toLong(),
//        0x0000000000000088U.toLong(), 0x0000000080008009U.toLong(), 0x000000008000000AU.toLong(),
//        0x000000008000808BU.toLong(), 0x800000000000008BU.toLong(), 0x8000000000008089U.toLong(),
//        0x8000000000008003U.toLong(), 0x8000000000008002U.toLong(), 0x8000000000000080U.toLong(),
//        0x000000000000800AU.toLong(), 0x800000008000000AU.toLong(), 0x8000000080008081U.toLong(),
//        0x8000000000008080U.toLong(), 0x0000000080000001U.toLong(), 0x8000000080008008U.toLong()
    )

    private val keccakRhoOffsets = arrayOf(
        intArrayOf(0, 1, 62, 28, 27),
        intArrayOf(36, 44, 6, 55, 20),
        intArrayOf(3, 10, 43, 25, 39),
        intArrayOf(41, 45, 15, 21, 8),
        intArrayOf(18, 2, 61, 56, 14)
    )

    private fun rotateLeft(value: Long, bits: Int): Long {
        return (value shl bits) or (value ushr (64 - bits))
    }

    private fun keccakF(state: Array<LongArray>) {
        for (round in 0 until 24) {
            val RC = keccakRoundConstants[round]
            // θ step
            val C = LongArray(5) { x ->
                state[x][0] xor state[x][1] xor state[x][2] xor state[x][3] xor state[x][4]
            }
            val D = LongArray(5) { x ->
                C[(x + 4) % 5] xor rotateLeft(C[(x + 1) % 5], 1)
            }
            for (x in 0 until 5) {
                for (y in 0 until 5) {
                    state[x][y] = state[x][y] xor D[x]
                }
            }
            // ρ and π steps
            val B = Array(5) { LongArray(5) }
            for (x in 0 until 5) {
                for (y in 0 until 5) {
                    B[y][(2 * x + 3 * y) % 5] = rotateLeft(state[x][y], keccakRhoOffsets[x][y])
                }
            }
            // χ step
            for (x in 0 until 5) {
                for (y in 0 until 5) {
                    state[x][y] = B[x][y] xor (B[(x + 1) % 5][y].inv() and B[(x + 2) % 5][y])
                }
            }
            // ι step
            state[0][0] = state[0][0] xor RC
        }
    }

    fun keccak(r: Int, c: Int, input: String): ByteArray {
        // Remake the message to byteArray
        val inputBytes = input.toByteArray()

        // Calculate the length of the input message in bits
        val messageLengthBits = inputBytes.size * 8
        // Create an array to store the bit values
        val mbits = ByteArray(messageLengthBits)
        // Iterate over the bytes in the input byte array
        for (i in inputBytes.indices) {
            val byte = inputBytes[i]
            // Extract the individual bits from the byte
            for (j in 0 until 8) {
                mbits[i * 8 + j] = ((byte.toInt() shr (7 - j)) and 0x01).toByte()
            }
        }

        // Padding based on mbits
        val d = (1L shl mbits.size) + mbits.foldIndexed(0L) { i, acc, bit -> acc + (bit.toLong() shl i) }
        val P = inputBytes + byteArrayOf((d ushr 8).toByte(), (d and 0xFF).toByte()) + 0x80.toByte()

        // Initialization
        val S = Array(5) { LongArray(5) }

        // Absorbing phase
        for (i in P.indices step (r * c / 8)) {
            for (x in 0 until 5) {
                for (y in 0 until 5) {
                    if (x + 5 * y < r / 8 && i + x + 5 * y < P.size) {
                        S[x][y] = S[x][y] xor P[i + x + 5 * y].toLong()
                    }
                }
            }
            keccakF(S)
        }

        // Squeezing phase
        val Z = mutableListOf<Byte>()
        while (Z.size < r / 8) {
            for (x in 0 until 5) {
                for (y in 0 until 5) {
                    if (x + 5 * y < r / 8) {
                        Z.add(S[x][y].toByte())
                    }
                }
            }
            keccakF(S)
        }

        return Z.toByteArray()
    }

    // Main program Example ::
//    val inputString = "Hello, Keccak!"
//    val keccak = Keccak()
//    // Call keccak function with appropriate parameters
//    val result = keccak.keccak(256, 512, inputString)
//
//    // Print result as hexadecimal string
//    println("Keccak[r,c] result: " + result.joinToString("") { it.toUByte().toString(16).padStart(2, '0') })
}
