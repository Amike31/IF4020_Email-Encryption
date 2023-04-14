import hashlib
from constant import *

def string_2_bit_string(str, n_bits=8):
    return ''.join(format(ord(c), 'b').zfill(n_bits) for c in str)

def bit_string_2_string(bit_str, n_bits=8):
    return ''.join(chr(int(bit_str[i:i+n_bits], 2)) for i in range(0, len(bit_str), n_bits))

def hash_string(str):
    hash_object = hashlib.sha256()
    hash_object.update(str.encode())
    hex_dig = hash_object.hexdigest()
    return hex_dig

def subkey_generator(external_key):
    # Hash the external key for security, the result has 64 characters
    hashed_key = hash_string(external_key)
    subkey_len = len(hashed_key) // 16  # 64/16 = 4
    subkeys = []
    x = 0
    for i in range(0, len(hashed_key)//subkey_len):
        partition_sum = 0
        for j in range(0, subkey_len):
            partition_sum = (partition_sum + ord(hashed_key[i*subkey_len+j])) % 256
        x = (x + partition_sum) % 256
        subkey = ''.join(format(x, 'b').zfill(8))
        subkeys.append(subkey*16)
    return subkeys

def LR_block_change(block):
    leng = len(block)//2
    result = block[leng:] + block[:leng]
    return result

def xor_two_block(block1, block2):
    result = ""
    for i in range(len(block1)):
        result += str(int(block1[i]) ^ int(block2[i]))
    return result

def substract_each_4bits_of_block_by_x(block, x=8, addition=False):
    if addition:
        x *= -1
        
    result = ""
    for i in range(0, len(block)//4):
        four_bit = block[i*4:i*4+4]
        four_bit_int = int(four_bit, 2)
        four_bit_int = (four_bit_int - x) % 16
        four_bit = ''.join(format(four_bit_int, 'b').zfill(4))
        result += four_bit
    return result

def block_substitution_by_sBox(block):
    result = ""
    for i in range(0, len(block)//8):
        eight_bit = block[i*8:i*8+8]
        the_row = eight_bit[:4]
        the_col = eight_bit[4:]
        row_idx = int(the_row, 2)
        col_idx = int(the_col, 2)
        res_int = S_BOX[row_idx*BOX_DIM[1] + col_idx]
        res_bit = ''.join(format(res_int, 'b').zfill(8))
        result += res_bit
    return result

def reverse_block_substitution(block):
    result = ""
    for i in range(0, len(block)//8):
        eight_bit = block[i*8:i*8+8]
        el = int(eight_bit, 2)
        el_idx = S_BOX.index(el)
        row_idx = el_idx // BOX_DIM[1]
        col_idx = el_idx % BOX_DIM[1]
        the_row = format(row_idx, 'b').zfill(4)
        the_col = format(col_idx, 'b').zfill(4)
        res_bit = the_row + the_col
        result += res_bit
    return result

def shift_arr_by_x(arr, x=0):
    result_arr = arr
    if x > 0:
        arr_before = arr[:x]
        arr_after = arr[x:]
        result_arr = arr_after + arr_before
    elif x < 0:
        arr_before = arr[:len(arr)+x]
        arr_after = arr[len(arr)+x:]
        result_arr = arr_after + arr_before
    return result_arr

def arr_2_matrix_of_string(arr, n_cols=16):
    matrix = []
    matrix_temp = []
    for i in range(0, len(arr)):
        matrix_temp.append(arr[i])
        if ((i+1)%n_cols == 0):
            matrix.append(matrix_temp)
            matrix_temp = []
    return matrix

def block_shifting(block, right=False):
    shifted_block = ""
    temp = ""
    for i in range(0, len(block)):
        temp += block[i]
        if (((i+1)%16) == 0):
            dist = (i+1)//16-1
            if (right):
                dist *= -1
            temp = shift_arr_by_x(temp, dist)
            shifted_block += temp
            temp = ""
    return shifted_block