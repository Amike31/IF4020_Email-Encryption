package com.fsck.k9.ui.cryptographic

import java.math.BigInteger

data class Point(val x: BigInteger, val y: BigInteger, val curve: Curve) {

    // Check if the point is the infinity point (0, 0)
    fun isInfinity(): Boolean = x == BigInteger.ZERO && y == BigInteger.ZERO

    // Check if two points are equal
    fun equals(other: Point): Boolean {
        return x == other.x && y == other.y && curve == other.curve
    }

    // Add two points on the elliptic curve
    operator fun plus(other: Point): Point {
        if (this.isInfinity()) {
            return other
        }
        if (other.isInfinity()) {
            return this
        }
        if (this.equals(other)) {
            return this.double()
        }
        val s = (other.y - y) * (other.x - x).modInverse(curve.p)
        val xr = (s * s - x - other.x).mod(curve.p)
        val yr = (s * (x - xr) - y).mod(curve.p)
        return Point(xr, yr, curve)
    }

    // Double a point on the elliptic curve
    private fun double(): Point {
        if (this.isInfinity()) {
            return this
        }
        val s = (3.toBigInteger() * x * x + curve.a) * (2.toBigInteger() * y).modInverse(curve.p)
        val xr = (s * s - 2.toBigInteger() * x).mod(curve.p)
        val yr = (s * (x - xr) - y).mod(curve.p)
        return Point(xr, yr, curve)
    }

    // Multiply a point by a scalar
    operator fun times(scalar: BigInteger): Point {
        var result = Point(BigInteger.ZERO, BigInteger.ZERO, curve)
        var current = this
        for (i in 0 until scalar.bitLength()) {
            if (scalar.testBit(i)) {
                result += current
            }
            current = current.double()
        }
        return result
    }
}
