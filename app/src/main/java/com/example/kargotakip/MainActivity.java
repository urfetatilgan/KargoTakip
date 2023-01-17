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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button buttonGiris;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
    });

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:5000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER),new Scope(Scopes.EMAIL),new Scope("https://www.googleapis.com/auth/gmail.readonly"))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }




        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        buttonGiris = findViewById(R.id.buttonGiris);
        sp=getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"thx!!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "sorry!!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    public void login(View view){
        //        if(username.getText().toString().equals("admin") && password.getText().toString().equals("12345")){
//            editor.putString("username",username.getText().toString());
//            editor.putString("password",password.getText().toString());
//            editor.commit();
//        Toast.makeText(getApplicationContext(), "Giriş Yaptınız",Toast.LENGTH_LONG).show();
//
//        }else{
//            Toast.makeText(getApplicationContext(), "Hatalı Giriş Yaptınız",Toast.LENGTH_LONG).show();
//        }
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startForResult.launch(signInIntent);
//        startActivityForResult(signInIntent, 1000);

//        finish();
//        startActivity(new Intent(MainActivity.this, KargoList.class));
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == 1000) {
////            mGoogleSignInClient.silentSignIn()
////                    .addOnCompleteListener(
////                            this,
////                            new OnCompleteListener<GoogleSignInAccount>() {
////                                @Override
////                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
////                                    handleSignInResult(task);
////                                }
////                            });
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
    public void logout(View view){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String authCode = account.getServerAuthCode();
            HashMap<String, String> map = new HashMap<>();
            map.put("idToken", idToken);
            map.put("authCode",authCode);
            Call<Void> call = retrofitInterface.executeLogin(map);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        startActivity(new Intent(MainActivity.this, KargoList.class));
                        Toast.makeText(MainActivity.this,
                                "Signed up successfully", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(MainActivity.this,
                                "Already registered", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
//
        }catch (ApiException e) {
            Log.w("tag", "handleSignInResult:error", e);
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}