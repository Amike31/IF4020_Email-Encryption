package com.fsck.k9.ui.cryptographic
import java.math.BigInteger

data class Point(val x: BigInteger, val y: BigInteger) {
    // Constant value for [y^2 = x^3 + ax + b]
    val p = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFF", 16)
    val a = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFC", 16)
    val b = BigInteger("64210519E59C80E70FA7E9AB72243049FEB8DEECC146B9B1", 16)
    companion object {
        val INF = Point(BigInteger.ZERO, BigInteger.ZERO) // Point at infinity
    }

    operator fun plus(other: Point): Point {
        // Point addition
        if (this == INF) {
            return other
        }
        if (other == INF) {
            return this
        }
        return if (this == other) {
            val three = BigInteger.valueOf((3).toLong())
            val two = BigInteger.valueOf((2).toLong())
            val lambda = ((three * x * x + a) * (two * y).modInverse(p)).mod(p)
            val xr = (lambda * lambda - two * x).mod(p)
            val yr = (lambda * (x - xr) - y).mod(p)
            Point(xr, yr)
        } else {
            val lambda = ((other.y - y) * (other.x - x).modInverse(p)).mod(p)
            val xr = (lambda * lambda - x - other.x).mod(p)
            val yr = (lambda * (x - xr) - y).mod(p)
            Point(xr, yr)
        }
    }

    operator fun times(n: BigInteger): Point {
        // Scalar multiplication
        var result = INF
        var k = n
        var q = this
        while (k > BigInteger.ZERO) {
            if (k.testBit(0)) {
                result += q
            }
            k = k.shiftRight(1)
            q += q
        }
        return result
    }
}
