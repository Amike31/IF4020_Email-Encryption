package com.fsck.k9.activity.key_generator

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fsck.k9.ui.R
import android.widget.*;
import com.fsck.k9.ui.cryptographic.ECDSA


class KeyGenActivity : AppCompatActivity() {
    private lateinit  var button_keygen : Button;
    private lateinit  var textedit1 : EditText
    private lateinit  var textedit2 : EditText;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_gen)

        button_keygen = findViewById(R.id.button_keygen)
        textedit1 = findViewById(R.id.edittext_placeprivatekey)
        textedit2 = findViewById(R.id.edittext_keypairoutput)

        button_keygen.setOnClickListener {
            // your code to perform when the user clicks on the button
            val ecdsa = ECDSA()
            val keypair = ecdsa.generateKeyPair(textedit1.text.toString().toBigInteger())
            val string_keypair = "Private Key: "+ keypair.privateKey.toString() + "\n" + "Public Key: ("+ keypair.publicKey.x.toString()+","+ keypair.publicKey.y.toString()+")"
            textedit2.setText(string_keypair);
        }

    }
}
