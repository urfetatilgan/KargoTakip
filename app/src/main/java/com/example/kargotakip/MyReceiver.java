package com.example.kargotakip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg ="", phoneNo = "", tmpMsg = "";
    String[] msg2;

    Cargo cargo = new Cargo("","","","");
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper db = new DBHelper(context);
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
                    Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
                    Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
                    try {
                        c.moveToFirst();
                        String  displayName = c.getString(0);
                        phoneNo = displayName;
                        //Toast.makeText(context, ContactName, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
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
                    if (tmpMsg.contains("teslim ettik.")) {
                        cargo.setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("Yoldayız,")) {
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
                    if (tmpMsg.contains("teslim edilmiştir.")) {
                        cargo.setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("dağıtıma")) {
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
                    if (tmpMsg.contains("teslim edilmiştir.")) {
                        cargo.setCargo_status("Teslim edildi");
                    } else if (tmpMsg.contains("dagitima")) {
                        cargo.setCargo_status("Dağıtıma Çıktı");
                    }
                    db.addCargo(cargo);
                }
                //}
                Toast.makeText(context,"Mesaj: "+msg+"\nNumber: "+phoneNo+"\nKargoTakipNo: "+msg2[0],Toast.LENGTH_LONG).show();
            }
        }
    }
}
