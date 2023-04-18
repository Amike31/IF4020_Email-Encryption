package com.fsck.k9.ui.cryptographic

import java.math.BigInteger

data class Curve(val p: BigInteger, val a: BigInteger, val b: BigInteger, val n:BigInteger) {
    // Check if a point is on the curve
    fun isOnCurve(point: Point): Boolean {
        if (point.isInfinity()) {
            return true
        }
        val ySquare = point.y * point.y
        val xCube = point.x * point.x * point.x
        return ySquare.mod(p) == (xCube + a * point.x + b).mod(p)
    }
}
