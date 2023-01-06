package com.example.kargotakip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button buttonGiris;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        buttonGiris = findViewById(R.id.buttonGiris);
        sp=getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
    }
    public void login(View view){
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("12345")){
            editor.putString("username",username.getText().toString());
            editor.putString("password",password.getText().toString());
            editor.commit();

            startActivity(new Intent(MainActivity.this, KargoList.class));

            Toast.makeText(getApplicationContext(), "Giriş Yaptınız",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(), "Hatalı Giriş Yaptınız",Toast.LENGTH_LONG).show();
        }
    }

}