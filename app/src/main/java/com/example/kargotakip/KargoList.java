package com.example.kargotakip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KargoList extends AppCompatActivity {

    TextView etCargo;
    ImageView ivCargo;
    Button btnSave, btnList, btnDelete, btnEdit;
    RecyclerView rvCargo;
    String cargoID = "";
    ArrayList<Cargo> cargoList = new ArrayList<>();
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kargo_list);
        //F_GetList();
        definitions();
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

        CargoAdapter adp = new CargoAdapter(this, cargoList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvCargo.setLayoutManager(layoutManager);
        //rvCargo.setHasFixedSize(true);
        rvCargo.setAdapter(adp);

        adp.setOnItemClickListener(onItemNoteClickListener);

        db.close();
    }

    View.OnClickListener onItemNoteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int i = viewHolder.getAdapterPosition();
            Cargo item = cargoList.get(i);
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
            DBHelper db = new DBHelper(getApplicationContext());
            db.deleteCargo(cargoID);
            db.close();

            Toast.makeText(getApplicationContext(), "Kargo silindi", Toast.LENGTH_SHORT).show();
            cargoID = "";
            etCargo.setText("");
            F_GetList();
        } else
            Toast.makeText(getApplicationContext(), "Lütfen silinecek kargoyu seçiniz", Toast.LENGTH_SHORT).show();
    }
}