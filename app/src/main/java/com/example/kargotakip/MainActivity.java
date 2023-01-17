package com.example.kargotakip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button buttonGiris;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACT);
            }
        }
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }



        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        buttonGiris = findViewById(R.id.buttonGiris);
        sp=getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
            case MY_PERMISSIONS_REQUEST_READ_CONTACT:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"thx!!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "sorry!!", Toast.LENGTH_SHORT).show();
                }

            }
        }
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