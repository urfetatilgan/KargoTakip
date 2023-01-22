package com.example.kargotakip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CargoPath extends AppCompatActivity {

    RecyclerView rvCargo;
    GoogleSignInClient mGoogleSignInClient;
    RelativeLayout relativeLayout;
    private String girisBilgi = "";
    ArrayList<Cargo> cargoList = new ArrayList<>();
    ArrayList<Cargo> cargoArrayList = new ArrayList<>();
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Retrofit retrofit;
    String kargoId;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://afternoon-spire-41332.herokuapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_path);
        sp=getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
        relativeLayout = findViewById(R.id.relat);
        rvCargo = findViewById(R.id.rv_Cargo_path);
        Intent myIntent = getIntent();
        girisBilgi = myIntent.getStringExtra("giris");
        kargoId = myIntent.getStringExtra("kargoId");
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

    }
    public void getList(View view) {
        f_getList();
    }
    public void f_getList(){
        DBHelper db = new DBHelper(getApplicationContext());
        cargoList = db.getCargoList();
        cargoArrayList = new ArrayList<>();
        if(girisBilgi.equals("0")){
            try{
                Call<List<CargoResults>> call = retrofitInterface.doGetUserListCargoPath(sp.getInt("user_id",-1),kargoId);
                call.enqueue(new Callback<List<CargoResults>>() {
                    @Override
                    public void onResponse(Call<List<CargoResults>> call, Response<List<CargoResults>> response) {
                        if (response.code() == 200) {
                            Log.i("TAG"   , "onResponse: ");
                            for(CargoResults cargo : response.body()){
                                Cargo savedCargo= null;
                                if(cargo.cargoStatus.equals("1") && kargoId.equals(cargo.cargoNo)){
                                    savedCargo = new Cargo(cargo.cargoId.toString(),cargo.cargoName,cargo.cargoNo,"Teslim edildi", cargo.cargoDate);
                                    //savedCargo.setCargo_date(cargo.cargoDate);
                                    cargoArrayList.add(savedCargo);
                                }else if (kargoId.equals(cargo.cargoNo)){
                                    savedCargo = new Cargo(cargo.cargoId.toString(),cargo.cargoName,cargo.cargoNo,"Yoldayız", cargo.cargoDate);
                                    //savedCargo.setCargo_date(cargo.cargoDate);
                                    cargoArrayList.add(savedCargo);
                                }
                            }
                            CargoAdapter adp = new CargoAdapter(getApplicationContext(), cargoArrayList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rvCargo.setLayoutManager(layoutManager);
                            //rvCargo.setHasFixedSize(true);
                            rvCargo.setAdapter(adp);
                            db.close();
                        } else if (response.code() == 400) {
                            Toast.makeText(getApplicationContext(),
                                    "Server Connection Error", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<CargoResults>> call, Throwable t) {
                    }
                });
            }catch (Exception e){
                Log.i("TAG"   , "onResponse: "+e.toString());
            }
        }else{
            CargoAdapter adp = new CargoAdapter(getApplicationContext(), cargoList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rvCargo.setLayoutManager(layoutManager);
            //rvCargo.setHasFixedSize(true);
            rvCargo.setAdapter(adp);
            db.close();
        }
    }
}