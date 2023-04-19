package com.fsck.k9.activity.key_generator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.fsck.k9.ui.R
import com.fsck.k9.ui.cryptographic.ECDSA

class KeyPassBcActivity : AppCompatActivity() {
    private lateinit  var button_keypass : Button;
    private lateinit  var textedit1 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_pass_bc)

        button_keypass = findViewById(R.id.button_keypass)
        textedit1 = findViewById(R.id.edittext_key_bc)

        button_keypass.setOnClickListener {
            // Code Pass
            val intent = Intent()
        }
    }
}
