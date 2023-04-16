package com.fsck.k9.ui.cryptographic

class BlockCipher {
    fun encrypt(plaintext: String, external_key: String): String{
        var plaintext = plaintext
        // 1. pad with "." if length is not a multiple of 16, because 128 bits contains of 16 letters
        var num_padding = (16 - plaintext.length.mod(16))
        if (num_padding > 0) {
            plaintext += " ".repeat(num_padding)
        }

        // 2. Get the plaintext as a bits
        var plain_bits = BC_Utils.string_2_bit_string(plaintext)

        // 3. Get 16 subkeys from external key
        var subkeys_list = BC_Utils.subkey_generator(external_key)

        // 4. Make blocks of block that contains 128 bits
        var blocks = emptyArray<String>()
        for (i in plain_bits.indices step 128){
            blocks += plain_bits.substring(i,i+128)
        }

        // 5. For each block from plaintext do Xml.Encoding by block cipher algorithm
        var ciphertext = ""
        for (block in blocks){
            var result_block = block
            // It be done for 16 iterations (16 subkeys)
            for (i in 0 until 16){
                // a) Do LR Change : exhange left and right 64bits
                val changed = BC_Utils.LR_block_change(result_block)
                // b) Do XOR with subkey
                val xor_str = BC_Utils.xor_two_block(changed, subkeys_list[i])
                // c) Do Substraction for each 4bits by x
                val substracted = BC_Utils.substract_each_4bits_of_block_by_x(xor_str, x = i, addition = false)
                // d) Do Substitution by S_BOX
                val subs_str = BC_Utils.block_substitution_by_sBox(substracted)
                // e) Do Block Shifting (permutation alt)
                val shifted_str = BC_Utils.block_shifting(subs_str, right = false)

                result_block = shifted_str
            }
            ciphertext += BC_Utils.bit_string_2_string(result_block)
        }

        // 6. Remove padding from the ciphertext result
        // If the num of padding is odd, add space after
        if (num_padding.mod(2) == 1) {
            num_padding -= 1
        }
        ciphertext = ciphertext.substring(0, ciphertext.length-num_padding)

        return ciphertext
    }

    fun decrypt(ciphertext: String, external_key: String): String {
        var ciphertext = ciphertext

        // 1. pad with "." if length ais not a multiple of 16
        var num_padding = (16 - ciphertext.length.mod(16))
        if (num_padding > 0) {
            ciphertext += " ".repeat(num_padding)
        }
        // 2. Get the ciphertext as a bits
        val cipher_bits = BC_Utils.string_2_bit_string(ciphertext)

        // 3. Get 16 subkeys from external key
        val subkeys_list = BC_Utils.subkey_generator(external_key)

        // 4. Make blocks of block that contains 128 bit
        var blocks = emptyArray<String>()
        for (i in cipher_bits.indices step 128){
            blocks += cipher_bits.substring(i,i+128)
        }

        // 5. For each block from ciphertext do Reverse-Encoding by block cipher algorithm
        var plaintext =  ""
        for (block in blocks){
            var result_block = block
            // It be done for 16 iterations (16 subkeys)
            for (i in 0 until 16){
                // Algorithm is the same as in encrypt.py, but in reverse order
                val reverse_shift = BC_Utils.block_shifting(result_block, right = true)
                val reverse_subs = BC_Utils.reverse_block_substitution(reverse_shift)
                val addited = BC_Utils.substract_each_4bits_of_block_by_x(reverse_subs, x = (15 - i), addition = true)
                val reXor_str = BC_Utils.xor_two_block(addited, subkeys_list[15 - i])
                val changed = BC_Utils.LR_block_change(reXor_str)
                result_block = changed
            }
            plaintext += BC_Utils.bit_string_2_string(result_block)
        }

        // 6. Remove padding from the plaintext result
        if (plaintext[plaintext.length-num_padding-1].compareTo(' ') == 0) { // == 0 -> 'Equals'
            num_padding += 1
        }
        plaintext = plaintext.substring(0,plaintext.length-num_padding)

        return plaintext
    }
}
