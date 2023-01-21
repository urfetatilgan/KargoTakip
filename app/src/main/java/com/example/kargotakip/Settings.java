package com.example.kargotakip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    Button btn_change;
    EditText oldName, newName, oldPassword, newPassword;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String username, password;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp=getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
        count=0;
        btn_change = findViewById(R.id.buttonSave);
        oldName = findViewById(R.id.editTextTextPersonName);
        newName = findViewById(R.id.editTextTextNewPersonName);
        oldPassword = findViewById(R.id.editTextTextPassword);
        newPassword = findViewById(R.id.editTextTextNewPassword);
        username = sp.getString("username", "kullanıcı adı yok");
        password = sp.getString("password","şifre yok");

    }
    public void change(View view){
        if (username.equals(oldName.getText().toString()) && password.equals(oldPassword.getText().toString())){
            Toast.makeText(this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show();
            count = 0;
//            editor.putString("username",newName.getText().toString());
//            editor.putString("password",newPassword.getText().toString());
            startActivity(new Intent(Settings.this,KargoList.class));
        }else{
            count++;
            if(count<=3){
                Toast.makeText(this, "Kayıt Başarısız!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.this,KargoList.class));
            }else{
                Toast.makeText(this, "Kayıt Başarısız, Deneme Hakkınız dolmuştur!", Toast.LENGTH_SHORT).show();

            }
        }
    }

}