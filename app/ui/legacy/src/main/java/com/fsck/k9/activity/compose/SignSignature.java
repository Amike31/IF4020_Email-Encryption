package com.fsck.k9.activity.compose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fsck.k9.activity.MessageCompose;
import com.fsck.k9.ui.R;

public class SignSignature extends AppCompatActivity {

    EditText privateKeyText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_sign_key);

        privateKeyText = findViewById(R.id.signature_private_key_input);
        submitButton = findViewById(R.id.signature_submit_sign_button);

        submitButton.setOnClickListener(v -> {
            // Get the private key from the input field
            String privateKeySign = privateKeyText.getText().toString();

            // Pass the privateKeySign to the MessageCompose activity
            Intent intent = new Intent(SignSignature.this, MessageCompose.class);
            intent.putExtra("privateKeySign", privateKeySign);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}
