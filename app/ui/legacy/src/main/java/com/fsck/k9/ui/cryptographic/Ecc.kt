package com.fsck.k9.ui.cryptographic
import java.math.BigInteger
import java.security.SecureRandom

class Ecc {
    // Base Point
    private val gx = BigInteger("188DA80EB03090F67CBF20EB43A18800F4FF0AFD82FF1012", 16)
    private val gy = BigInteger("07192B95FFC8DA78631011ED6B24CDD573F977A11E794811", 16)
    private val g = Point(gx, gy)
    // Modulos
    private val n = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFF99DEF836146BC9B1B4D22831", 16)

    // Generate ECC key pair with a given private key value (d)
    private fun generateKeyPair(d: BigInteger): EccKeyPair {
        val publicKey = g * d // Compute public key as G * d
        return EccKeyPair(publicKey, d)
    }

    // Encrypt a message using ECC with a given private key value (d)
    fun encrypt(message: String, publicKey: Point): List<Pair<BigInteger, BigInteger>> {
        val plaintextBytes = message.toByteArray() // Convert plaintext to bytes
        val encrypted = plaintextBytes.map {
            val k = generateRandomK() // Generate a random k value
            val c1 = g * k // Compute C1 = k * G
            val sharedSecret = publicKey * k // Compute shared secret = k * publicKey
            val c2 = sharedSecret.y.xor(BigInteger(it.toString())) // Compute C2 = plaintext XOR sharedSecret
            Pair(c1.x, c2)
        }
        return encrypted
    }

    // Generate Random K for mapping the byte of message to an Elliptic Curve Point
    private fun generateRandomK(): BigInteger {
        val random = SecureRandom()
        val modulo = n
        var k: BigInteger

        // Loop until a valid k value is generated
        do {
            // Generate a random BigInteger within the range [1, modulo)
            k = BigInteger(modulo.bitLength(), random)
        } while (k >= modulo || k <= BigInteger.ZERO)

        return k
    }

    // Decrypt a message using ECC with a given private key value (d)
    fun decrypt(encrypted: List<Pair<BigInteger, BigInteger>>, d: BigInteger): String {
        val decryptedBytes = encrypted.map {
            val c1 = Point(it.first, g.y) // C1 is a point on the curve
            val sharedSecret = c1 * d // Compute shared secret = C1 * privateKey
            val plaintextByte = it.second.xor(sharedSecret.y).toByte() // Compute plaintext byte
            plaintextByte
        }.toByteArray()
        return String(decryptedBytes)
    }
}
