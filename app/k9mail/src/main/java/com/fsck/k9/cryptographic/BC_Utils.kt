package com.fsck.k9.cryptographic

import com.fsck.k9.cryptographic.Constants.BOX_DIM
import com.fsck.k9.cryptographic.Constants.S_BOX
import java.lang.Math.floor

object BC_Utils {

    fun ord(char: Char): Int{
        return char.code
    }

    fun chr(int: Int): Char{
        return int.toChar()
    }

    fun string_2_bit_string(str: String, n_bits: Int = 8): String{
        var res = ""
        for (c in str){
            val uni_string = ord(c).toString(2).padStart(n_bits,'0')
            res += uni_string
        }
        return res
    }

    fun bit_string_2_string(bit_str: String, n_bits: Int = 8): String{
        var res = ""
        for (i in 0..bit_str.length-1 step n_bits){
            val substring = chr(bit_str.substring(i, i+n_bits).toInt(2))
            res += substring
        }
        return res
    }

    fun subkey_generator(external_key: String): Array<String>{
        var external_key = external_key
        if (external_key.length.mod(16) != 0){
            var num_padding = (16 - external_key.length.mod(16))
            external_key += ".".repeat(num_padding)
        }
        val subkey_len = kotlin.math.floor((external_key.length / 16).toDouble()).toInt()
        var subkeys = emptyArray<String>()
        val range = kotlin.math.floor((external_key.length / subkey_len).toDouble()).toInt()
        var x = 0
        for (i in 0..range-1){
            var partition_sum = 0
            for (j in 0..subkey_len-1){
                partition_sum = (partition_sum + ord(external_key[i*subkey_len+j])).mod(256)
            }
            x = (x + partition_sum).mod(256)
            val subkey = "" + x.toString(2).padStart(8,'0')
            subkeys += subkey.repeat(16)
        }
        return subkeys
    }

    fun LR_block_change(block: String): String {
        val leng = kotlin.math.floor((block.length / 2).toDouble()).toInt()
        val result = block.substring(leng) + block.substring(0,leng)
        return result
    }


    fun xor_two_block(block1: String, block2: String): String {
        var result = ""
        for (i in 0..block1.length-1){
            result += (block1[i].toString().toInt() xor block2[i].toString().toInt()).toString()
        }
        return result
    }

    fun substract_each_4bits_of_block_by_x(block: String, x: Int = 8, addition: Boolean = true): String{
        var x = x
        if (addition) {
            x *= -1
        }
        var result = ""
        val range = kotlin.math.floor((block.length/4).toDouble()).toInt()
        for (i in 0..range-1){
            var four_bit = block.substring(i*4, i*4+4)
            var four_bit_int = four_bit.toInt(2)
            four_bit_int = (four_bit_int - x).mod(16)
            four_bit = four_bit_int.toString(2).padStart(4,'0')
            result += four_bit
        }
        return result
    }

    fun block_substitution_by_sBox(block: String): String {
        var result = ""
        val range = kotlin.math.floor((block.length / 8).toDouble()).toInt()
        for (i in 0..range-1){
            val eight_bit = block.substring(i*8, i*8+8)
            val the_row = eight_bit.substring(0,4)
            val the_col = eight_bit.substring(4)
            val row_idx = the_row.toInt(2)
            val col_idx = the_col.toInt(2)
            val res_int = Constants.S_BOX[row_idx* Constants.BOX_DIM[1] + col_idx]
            val res_bit = res_int.toString(2).padStart(8,'0')
            result += res_bit
        }
        return result
    }

    fun reverse_block_substitution(block: String): String{
        var result = ""
        val range = kotlin.math.floor((block.length / 8).toDouble()).toInt()
        for (i in 0..range-1){
            val eight_bit = block.substring(i*8, i*8+8)
            val el = eight_bit.toInt(2)
            val el_idx =  Constants.S_BOX.indexOf(el)
            val row_idx = kotlin.math.floor((el_idx / Constants.BOX_DIM[1]).toDouble()).toInt()
            val col_idx = el_idx.mod(Constants.BOX_DIM[1])
            val the_row = row_idx.toString(2).padStart(4,'0')
            val the_col = col_idx.toString(2).padStart(4,'0')
            val res_bit = the_row + the_col
            result += res_bit
        }
        return result
    }

    fun shift_arr_by_x(arr: String, x: Int = 0): String {
        var result_arr = arr
        if (x>0){
            val arr_before = arr.slice(0 until x)
            val arr_after = arr.slice(x until arr.length)
            result_arr = arr_after + arr_before
        }
        else if (x<0){
            val arr_before = arr.slice(0 until arr.length+x)
            val arr_after = arr.slice(arr.length+x until arr.length)
            result_arr = arr_after + arr_before
        }
        return result_arr.toString()
    }

    fun arr_2_matrix_of_string(arr: Array<String>, n_cols : Int = 16): Array<String>{
        var matrix = emptyArray<String>()
        var matrix_temp = emptyArray<String>()
        for (i in arr.indices){
            matrix_temp += arr[i]
            if ((i+1).mod(n_cols) == 0) {
                matrix += matrix_temp
                matrix_temp = emptyArray<String>()
            }
        }
        return matrix
    }

    fun block_shifting(block: String, right : Boolean = false): String{
        var shifted_block = ""
        var temp = ""
        for (i in block.indices){
            temp += block[i]
            if (((i+1).mod(16)) == 0){
                var dist = kotlin.math.floor(((i+1)/16-1).toDouble()).toInt()
                if (right){
                    dist *= -1
                }
                temp = shift_arr_by_x(temp, dist)
                shifted_block += temp
                temp = ""
            }
        }
        return shifted_block
    }
}
