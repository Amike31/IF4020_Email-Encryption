package com.fsck.k9.activity.key_generator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.fsck.k9.activity.MessageCompose
import com.fsck.k9.ui.R
import com.fsck.k9.ui.cryptographic.BlockCipher


class KeyPassBcActivity : AppCompatActivity() {
    private val blockCipher : BlockCipher = BlockCipher()
    private lateinit  var button_keypass : Button;
    private lateinit  var textedit1 : EditText
    private lateinit  var message2 : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_pass_bc)

        button_keypass = findViewById(R.id.button_keypass)
        textedit1 = findViewById(R.id.edittext_key_bc)

        button_keypass.setOnClickListener{

            val extras = intent.extras
            if (extras != null) {
                message2 = extras.getString("message2").toString()
                message2 = blockCipher.encrypt(message2, textedit1.text.toString())
                var intent = Intent(this, MessageCompose::class.java)
                intent.putExtra("message2", message2)
                setResult(
                    RESULT_OK,
                    Intent().putExtra("message2", message2)
                )
                finish()
            }
        }


    }
}
