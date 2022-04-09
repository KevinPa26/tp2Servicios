package com.example.tp2servicios;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

public class Contador extends Service {
    private static int bandera;

    public Contador() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("salida", "Iniciado");
        bandera = 1;
        //hilo
        Thread trabajador = new Thread(new Cuenta());
        trabajador.start();


        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bandera = 0;
        Log.d("salida","Servicio destruido");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @SuppressLint("Range")
    public void leerMensajes(){
        Uri Mensajes = Uri.parse("content://sms/");

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Mensajes, null, null, null, null);

        String mensaje = null,numero = null,nombre = null;

        //StringBuilder stringBuilder = new StringBuilder();

        if(cursor.getCount() > 0){
            for (int i=0; i<5;i++){
                cursor.moveToPosition(i);
                if(cursor.moveToNext()){

                    mensaje = cursor.getString(12);
                    numero = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                    nombre = cursor.getString(cursor.getColumnIndex(Telephony.Sms.CREATOR));
                    Log.d("Mensajes: ","Nombre: " +nombre + " Mensaje: " + mensaje + " NUMERO: " + numero);

                }

            }
            Log.d("Mensaje",  "-----------------------------------------------------------------------");

        }
    }

    private class Cuenta implements Runnable{
        public void run(){

            while (bandera==1) {
                try {

                    leerMensajes();
                    Thread.sleep(9000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


    }

}