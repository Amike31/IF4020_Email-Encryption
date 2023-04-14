# Block-Cipher

## Anggota Kelompok
1. 13520090 - Rahmat Rafid Akbar
2. 13520011 - M. Akyas David Al Aleey
3. 13519175 - Stefanus Jeremy Aslan

## Instruksi Eksekusi Program
1. Pastikan bahwa Python 3 terinstall pada perangkat
2. Buka terminal dengan path direktori 'Block-Cipher'
3. Jalankan perintah 'python src/main.py'
4. Pilih opsi proses cipher yang ingin dilakukan 
5. Pilih metode pembacaan masukan yang diinginkan
 - Apabila metode pembacaan menggunakan filename, masukan text file ke dalam folder 'Test_Files/Test'. Masukkan cukup menggunakan nama file tanpa jenis ekstensionnya.
 - Apabila metode pembacaan dari ketikan user, masukan cukup diketik pada Command Line
6. Hasil proses cipher akan ditampilkan di halaman terminal dan disimpan pada folder 'Test_Files/After_Crypt'.

## Notes:
- Beri penamaan filename dengan standar {{nomor}}c.txt untuk ciphertext, {{nomor}}p.txt untuk plaintext, dan {{nomor}}k.txt untuk key. Hal ini dilakukan agar memudahkan inputan pada Command Line
- Jika ingin melakukan dekripsi dari hasil encrypsi, jangan lakukan copy paste langsung dari Command Line. Hal ini dapat dilakukan dengan menyalin hasil enkripsi dari file '02_result ciphertext.txt' dan menuliskannya ke file '{{nomor}}c.txt' serta string kunci dari file '03_external key.txt' ke file '{{nomor}}c.txt'. Hal ini berlaku sebaliknya untuk hasil dekripsi plaintext.