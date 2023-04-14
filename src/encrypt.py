from utils import *

def encrypt(plaintext, external_key):
    # 1. pad with "." if length is not a multiple of 16, because 128 bits contains of 16 letters
    num_padding = (16 - len(plaintext) % 16)
    if (num_padding > 0):
        plaintext += " " * num_padding

    # 2. Get the plaintext as a bits
    plain_bits = string_2_bit_string(plaintext)
    
    # 3. Get 16 subkeys from external key
    subkeys_list = subkey_generator(external_key)

    # 4. Make blocks of block that contains 128 bits
    blocks = []
    for i in range(0, len(plain_bits), 128):
        blocks.append(plain_bits[i:i+128])
    
    # 5. For each block from plaintext do Encoding by block cipher algorithm
    ciphertext = ""
    for block in blocks:
        result_block = block
        ### It be done for 16 iterations (16 subkeys)
        for i in range(16):
            # a) Do LR Change : exhange left and right 64bits
            changed = LR_block_change(result_block)
            # b) Do XOR with subkey
            xor_str = xor_two_block(changed, subkeys_list[i])
            # c) Do Substraction for each 4bits by x
            substracted = substract_each_4bits_of_block_by_x(xor_str, x=i, addition=False)
            # d) Do Substitution by S_BOX
            subs_str = block_substitution_by_sBox(substracted)
            # e) Do Block Shifting (permutation alt)
            shifted_str = block_shifting(subs_str, right=False)
            
            result_block = shifted_str
        ciphertext += bit_string_2_string(result_block)
        
    # 6. Remove padding from the ciphertext result
    # If the num of padding is odd, add space after
    if num_padding%2 == 1:
        num_padding -= 1
    ciphertext = ciphertext[:len(ciphertext)-num_padding]
    
    return ciphertext