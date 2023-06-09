package com.fsck.k9.ui.cryptographic

import android.util.Log
import java.math.BigInteger
import java.util.*
import kotlin.math.absoluteValue

class ECDSA {
    private val a = BigInteger("0", 16)
    private val b = BigInteger("7", 16)
    private val p = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16)
    private val n = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16)
    private val curve = Curve(p, a, b, n)

    // Define the base point (generator) of the elliptic curve
    private val gx = BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16)
    private val gy = BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16)
    val g = Point(gx, gy, curve)

    // Hasher
    private val hash = Keccak()

    fun generateKeyPair(privateKey: BigInteger): KeyPair {
        val publicKey = g * privateKey
        return KeyPair(privateKey, publicKey)
    }
    fun sign(privateKey: BigInteger, message: String): Pair<BigInteger,BigInteger> {
        // bitlength of curve.n
        val bitLength = curve.n.bitLength()
        var hashMessage = BigInteger(hash.keccak(256,512, message)).abs()
        // get the bitlength leftmost bits of the hashMessage
        hashMessage = hashMessage.shiftRight((hashMessage.bitLength() - bitLength).absoluteValue)
//        Log.d("", hashMessage.toString())
        // val hashMessage = message.hashCode().toBigInteger()
        var k : BigInteger
        var r : BigInteger
        var s : BigInteger
        // Compute the signature (r, s)
        do {
            do {
                // Generate a random nonce (k) for signing
               // k = BigInteger(256, Random())
                k = BigInteger("2")
                val c1 = g * k
                r = c1.x.mod(curve.n)
            } while (r == BigInteger.ZERO)
            s = ( (hashMessage + privateKey * r) * k.modInverse(curve.n) ).mod(curve.n)
        } while (s == BigInteger.ZERO)

//        // if r or s is negative, convert to positive using two's complement
//        if (r.signum() == -1) {
//            val bitRLength = r.bitLength()
//            r = r.add(BigInteger.ONE.shiftLeft(bitRLength)).abs()
//        }
//        if (s.signum() == -1) {
//            val bitSLength = s.bitLength()
//            s = s.add(BigInteger.ONE.shiftLeft(bitSLength)).abs()
//        }
        return Pair(r,s)
    }

    fun verify(publicKey: Point, message: String, signature: Pair<BigInteger,BigInteger>): Boolean {
        // Verify the signature
        val bitLength = curve.n.bitLength()
        var hashMessage = BigInteger(hash.keccak(256,512, message)).abs()
        // get the bitlength leftmost bits of the hashMessage
        hashMessage = hashMessage.shiftRight((hashMessage.bitLength() - bitLength).absoluteValue)
        // val hashMessage = message.hashCode().toBigInteger()
        val w = signature.second.modInverse(curve.n)
        val u1 = (hashMessage * w).mod(curve.n)
        val u2 = (signature.first * w).mod(curve.n)
        val coordinate = (g * u1 + publicKey * u2)
        val v = coordinate.x

        // if v is negative, convert to positive using two's complement
//        if (v.signum() == -1) {
//            val bitVLength = v.bitLength()
//            v.add(BigInteger.ONE.shiftLeft(bitVLength)).abs()
//        }
        return (v).mod(curve.n) == (signature.first).mod(curve.n)
    }

    // Main program Example ::
//    // Define the private key (random integer) and compute the public key (point)
//    val privateKey = BigInteger("1234567890")
//    val ecdsa = ECDSA()
//    val publicKey = ecdsa.g * privateKey
//
//    // Define the message to be signed
//    val message = "Hello, ECDSA!"
//
//    val signature = ecdsa.sign(privateKey,message)
//    val r = signature.first
//    val s = signature.second
//    println("Private Key: $privateKey")
//    println("Public Key (x, y): (${publicKey.x}, ${publicKey.y})")
//    println("Message: ($message)")
//    println("Signature (r, s): ($r, $s)")
//    if (ecdsa.verify(publicKey, message, signature)) {
//        println("It is valid")
//    } else {
//        println("It is not")
//    }
}
