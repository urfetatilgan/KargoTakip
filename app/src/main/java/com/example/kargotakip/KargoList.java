package com.example.kargotakip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KargoList extends AppCompatActivity {

    TextView etCargo;
    ImageView ivCargo;
    private String username;
    private String password;
    Button btnSave, btnList, btnDelete, btnEdit;
    RecyclerView rvCargo;
    GoogleSignInClient mGoogleSignInClient;
    String cargoID = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean giris = true;
    ArrayList<Cargo> cargoList = new ArrayList<>();
    ArrayList<Cargo> cargoArrayList = new ArrayList<>();
    RelativeLayout relativeLayout;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://afternoon-spire-41332.herokuapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kargo_list);
        //F_GetList();
        sp=getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
        username = sp.getString("username", "kullanıcı adı yok");
        password = sp.getString("password","şifre yok");
        if (username.equals("admin") && password.equals("12345")){
            giris = false;
        }
        definitions();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String token= acct.getIdToken();
            //etCargo.setText(token);
            String personId = acct.getId();
        }

    }

    private void definitions() {
        relativeLayout = findViewById(R.id.relat);
        ivCargo = findViewById(R.id.iv_CargoImage);
        etCargo = findViewById(R.id.et_Cargo);
        btnDelete = findViewById(R.id.btn_Delete);
        btnEdit = findViewById(R.id.btn_Edit);
        btnList = findViewById(R.id.btn_List);
        //btnSave = findViewById(R.id.btn_Add);
        rvCargo = findViewById(R.id.rv_Cargo);
    }

    public void btn_List_CLick(View view) {
        F_GetList();
    }

    void F_GetList() {
        DBHelper db = new DBHelper(getApplicationContext());
        cargoList = db.getCargoList();
        cargoArrayList = new ArrayList<>();
        if(giris){
            try{
                Call<List<CargoResults>> call = retrofitInterface.doGetUserList();
                call.enqueue(new Callback<List<CargoResults>>() {
                    @Override
                    public void onResponse(Call<List<CargoResults>> call, Response<List<CargoResults>> response) {
                        if (response.code() == 200) {
                            Log.i("TAG"   , "onResponse: ");
                            ArrayList<Cargo> data = new ArrayList<>();
                            for(CargoResults cargo : response.body()){
                                Cargo savedCargo= null;
                                if(cargo.cargoStatus.equals("1")){
                                    savedCargo = new Cargo(cargo.cargoId.toString(),cargo.cargoName,cargo.cargoNo,"Teslim edildi", cargo.cargoDate);
                                    //savedCargo.setCargo_date(cargo.cargoDate);
                                    cargoArrayList.add(savedCargo);
                                }else{
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
                            adp.setOnItemClickListener(onItemNoteClickListener);
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
            adp.setOnItemClickListener(onItemNoteClickListener);
            db.close();
        }

    }

    View.OnClickListener onItemNoteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int i = viewHolder.getAdapterPosition();
            Cargo item;
            if(giris){
                item = cargoArrayList.get(i);
            }else{
                item = cargoList.get(i);
            }

            etCargo.setText("Seçili Kargo: " + item.getCargo_no());
            cargoID = item.getCargo_id();
        }
    };

    /*public void btn_Save_Click(View view) {
        if (!etCargo.getText().toString().trim().equals("")) {
            DBHelper db = new DBHelper(getApplicationContext());
            db.addCargo(etCargo.getText().toString());

            db.close();
            etCargo.setText("");
        }
    }*/

    public void btn_Delete_Click(View view) {
        if (!cargoID.equals("")) {
            if(!giris){
                DBHelper db = new DBHelper(getApplicationContext());
                db.deleteCargo(cargoID);
                db.close();
            }else{
                //TODO backend silme işlemi yazılacak.
            }

            Toast.makeText(getApplicationContext(), "Kargo silindi", Toast.LENGTH_SHORT).show();
            cargoID = "";
            etCargo.setText("");
            F_GetList();
        } else
            Toast.makeText(getApplicationContext(), "Lütfen silinecek kargoyu seçiniz", Toast.LENGTH_SHORT).show();
    }
    public void btn_Goster(View view){
        DBHelper db = new DBHelper(getApplicationContext());
        cargoList = db.getCargoList();

        String name = "";
        if(!giris){
            for(int i=0; i<cargoList.size(); i++){
                if(cargoList.get(i).getCargo_id().equals(cargoID)){
                    name = cargoList.get(i).getCargo_name();
                }
            }
        }else{
            for(int i=0; i<cargoArrayList.size(); i++){
                if(cargoArrayList.get(i).getCargo_id().equals(cargoID)){
                    name = cargoArrayList.get(i).getCargo_name();
                }
            }
        }

        if(name.contains("ARAS")){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.araskargo.com.tr/tr/")));
        }else if(name.contains("MNG")){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.mngkargo.com.tr/gonderitakip")));
        }else if(name.contains("SURAT")){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.mngkargo.com.tr/gonderitakip")));
        }


        //PyObject obj = pyObj.callAttr("arasBot", cargoList.get(0).getCargo_no());
        //etCargo.setText(obj.toString());
    }
}