package com.example.kargotakip;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    public static final String DATABASE_NAME = "my_cargo.db";
    private static final int DATABASE_VERSION = 1;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://afternoon-spire-41332.herokuapp.com";
    String msg ="", phoneNo = "", tmpMsg = "";
    String[] msg2;
    CargoResults insertedCargo= new CargoResults("","","");
    Cargo cargo = new Cargo("","","","","");
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    public void onReceive(Context context, Intent intent) {
        sp=context.getSharedPreferences("GirisBilgi",MODE_PRIVATE);
        editor=sp.edit();
        DBHelper db = new DBHelper(context);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        boolean find = false;
        int findTmp = 0;
        int size;
        String findTmp1 = "";
        Log.i(TAG,"Intent Received:"+ intent.getAction());
        if (intent.getAction().equals(SMS_RECEIVED)){
            Bundle dataBundle = intent.getExtras();
            if(dataBundle!=null){
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for (int i=0; i< mypdu.length; i++){
                    //int i=0;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        String format = dataBundle.getString("format");
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i], format);
                    }else{
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = msg.concat(message[i].getMessageBody());
                    tmpMsg = msg;
                    phoneNo = message[i].getOriginatingAddress();
//                    Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
//                    Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
//                    try {
//                        c.moveToFirst();
//                        String  displayName = c.getString(0);
//                        phoneNo = displayName;
//                        //Toast.makeText(context, ContactName, Toast.LENGTH_LONG).show();
//
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
                    if(i==0){

                        msg2 = msg.split(" ");
                    }
                }
                if(phoneNo.contains("ARAS")){
                    if(msg2[1].contains("takip") ){
                        for(int i = 0; i<db.getCargoList().size(); i++){
                            if(db.getCargoList().get(i).getCargo_no().contains(msg2[0])){
                                find = true;
                                db.deleteCargo(String.valueOf(i+1));
                                findTmp = i;
                            }
                        }
                        findTmp1 = msg2[0];
                    }else if(msg2[0].contains("Yoldayız,") ){
                        for(int i = 0; i<db.getCargoList().size(); i++){
                            if(db.getCargoList().get(i).getCargo_no().contains(msg2[2])){
                                find = true;
                                db.deleteCargo(String.valueOf(i+1));
                                findTmp = i;
                            }
                        }
                        findTmp1 = msg2[2];
                    }

                /*if(false) {
                    if (tmpMsg.contains("teslim ettik.")) {
                        db.getCargoList().get(findTmp).setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("Yoldayız,")) {
                        db.getCargoList().get(findTmp).setCargo_status("Yola Çıktı");
                    } else if (tmpMsg.contains("teslimata çıktı")) {
                        db.getCargoList().get(findTmp).setCargo_status("Teslimata Çıktı");
                    }
                }else {*/
                    size = db.getCargoList().size() + 1;
                    cargo.setCargo_id(String.valueOf(size));
                    cargo.setCargo_name(phoneNo);
                    cargo.setCargo_no(findTmp1);
                    String date = df.format(Calendar.getInstance().getTime());
                    cargo.setCargo_date(date);
                    insertedCargo.setCargoDate(date);
                    if (tmpMsg.contains("teslim ettik.")) {
                        insertedCargo.setCargoStatus("1");
                        cargo.setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("Yoldayız,")) {
                        insertedCargo.setCargoStatus("0");
                        cargo.setCargo_status("Dağıtıma Çıktı");
                    }
                    db.addCargo(cargo);
                }else if(phoneNo.contains("MNG")){
                    if(tmpMsg.contains("dağıtıma") ){
                        for(int i = 0; i<db.getCargoList().size(); i++){
                            if(db.getCargoList().get(i).getCargo_no().contains(msg2[0])){
                                find = true;
                                db.deleteCargo(String.valueOf(i+1));
                                findTmp = i;
                            }
                        }
                        findTmp1 = msg2[0];
                    }else if(tmpMsg.contains("teslim edilmiştir.") ){
                        for(int i = 0; i<db.getCargoList().size(); i++){
                            if(db.getCargoList().get(i).getCargo_no().contains(msg2[0])){
                                find = true;
                                db.deleteCargo(String.valueOf(i+1));
                                findTmp = i;
                            }
                        }
                        findTmp1 = msg2[0];
                    }
                    size = db.getCargoList().size() + 1;
                    cargo.setCargo_id(String.valueOf(size));
                    cargo.setCargo_name(phoneNo);
                    cargo.setCargo_no(findTmp1);
                    String date = df.format(Calendar.getInstance().getTime());
                    cargo.setCargo_date(date);
                    insertedCargo.setCargoDate(date);
                    if (tmpMsg.contains("teslim edilmiştir.")) {
                        insertedCargo.setCargoStatus("1");
                        cargo.setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("dağıtıma")) {
                        insertedCargo.setCargoStatus("0");
                        cargo.setCargo_status("Dağıtıma Çıktı");
                    }
                    db.addCargo(cargo);
                }else if(phoneNo.contains("SURAT")){
                    if(tmpMsg.contains("dagitima") ){
                        for(int i = 0; i<db.getCargoList().size(); i++){
                            if(db.getCargoList().get(i).getCargo_no().contains(msg2[5])){
                                find = true;
                                db.deleteCargo(String.valueOf(i+1));
                                findTmp = i;
                            }
                        }
                        findTmp1 = msg2[5];
                    }else if(tmpMsg.contains("teslim edilmiştir.") ){
                        for(int i = 0; i<db.getCargoList().size(); i++){
                            if(db.getCargoList().get(i).getCargo_no().contains(msg2[5])){
                                find = true;
                                db.deleteCargo(String.valueOf(i+1));
                                findTmp = i;
                            }
                        }
                        findTmp1 = msg2[5];
                    }
                    size = db.getCargoList().size() + 1;
                    cargo.setCargo_id(String.valueOf(size));
                    cargo.setCargo_name(phoneNo);
                    cargo.setCargo_no(findTmp1);
                    String date = df.format(Calendar.getInstance().getTime());
                    insertedCargo.setCargoDate(date);
                    cargo.setCargo_date(date);
                    if (tmpMsg.contains("teslim edilmiştir.")) {
                        insertedCargo.setCargoStatus("1");
                        cargo.setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("dagitima")) {
                        insertedCargo.setCargoStatus("0");
                        cargo.setCargo_status("Dağıtıma Çıktı");
                    }
                    db.addCargo(cargo);
                }
                if(!insertedCargo.cargoDate.equals(null)){
                    insertedCargo.setCargoName(cargo.getCargo_name());
                    insertedCargo.setCargoNo(cargo.getCargo_no());
                    insertedCargo.setCargoFrom("0");//From Sms
                    try {
                        Call<Void> call = retrofitInterface.insertCargo(insertedCargo,sp.getInt("user_id",-1));
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                } else if (response.code() == 400) {
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                            }
                        });
                    }catch (Exception e){
                        Log.i("TAG", "error: "+e.toString());
                    }
                }
                //}
                //Toast.makeText(context,"Mesaj: "+msg+"\nNumber: "+phoneNo+"\nKargoTakipNo: "+msg2[0],Toast.LENGTH_LONG).show();
            }
        }
    }
}
