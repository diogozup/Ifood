package com.pandapanda.ifood.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pandapanda.ifood.R;
import com.pandapanda.ifood.activity.AboutActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) {

        // public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //  super.onMessageReceived(remoteMessage);
        //  Log.i("Notificacao", "Notificacao recebida");
        // }
        if(notificacao.getNotification() != null ){
            String titulo = notificacao.getNotification().getTitle();
            String corpo = notificacao.getNotification().getBody();

            enviarNotificacao(titulo,corpo);
            //Log.i("Notificacao","titulo: " + titulo + "corpo: " +corpo );
        }
    }

    private void enviarNotificacao(String titulo, String corpo){

        //Configuracao para notificacao
        String canal = getString(R.string.default_notification_channel_id);
        Uri uriSom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, AboutActivity.class);

        //-------------------------activity para onde queremos que va on click
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);



        // Criar notificacao
        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this,canal)
                .setContentTitle( titulo )
                .setContentText( corpo )
                .setSmallIcon(R.drawable.ic_local_pizza_black_24dp)
                //.setColor(Color.RED)
                .setSound(uriSom)
                .setAutoCancel( true )
                .setContentIntent(pendingIntent) // activity onde user vai depois de clicar na notificacao
                ;

        // Recupera notificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Verifica versao do Android a partir do Oreo para configurar canal de notificacao
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(canal,"canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Envia notificacao
        notificationManager.notify(0,notificacao.build() );

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
