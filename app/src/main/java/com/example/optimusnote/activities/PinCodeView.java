package com.example.optimusnote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.optimusnote.R;
import com.hanks.passcodeview.PasscodeView;

public class PinCodeView extends AppCompatActivity {

    PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_view);

        //assign variable

        passcodeView = findViewById(R.id.passcode_view);

        passcodeView.setPasscodeLength(4).setLocalPasscode("0000")


                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        //Pin code wrong
                        Toast.makeText(getApplicationContext(),"Password is wrong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {

                        //Pin code correct

                        Intent intent = new Intent(PinCodeView.this, NotMainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}