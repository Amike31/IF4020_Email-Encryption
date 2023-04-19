package com.fsck.k9.ui.cryptographic

import java.math.BigInteger

data class KeyPair(val privateKey: BigInteger, val publicKey: Point)
