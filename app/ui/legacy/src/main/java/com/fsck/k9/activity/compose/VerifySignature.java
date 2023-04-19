package com.fsck.k9.activity.compose;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fsck.k9.activity.MessageCompose;
import com.fsck.k9.ui.R;

public class VerifySignature extends AppCompatActivity {

    EditText publicKeyText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_verify_key);

        publicKeyText = findViewById(R.id.signature_verify_key_input);
        submitButton = findViewById(R.id.signature_submit_verify_button);

        submitButton.setOnClickListener(v -> {
            String userSignatureKey = publicKeyText.getText().toString();

            Intent intent = new Intent(VerifySignature.this, MessageCompose.class);
            intent.putExtra("user_signature_key", userSignatureKey);
            startActivity(intent);
        });
    }
}
