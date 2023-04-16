package com.fsck.k9.ui.cryptographic
import java.math.BigInteger

data class EccKeyPair(val publicKey: Point, val privateKey: BigInteger)
