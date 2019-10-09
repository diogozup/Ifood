package com.pandapanda.ifood.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UtilizadorFirebase {


    public static String getIdUtilizador(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return autenticacao.getCurrentUser().getUid();
    }


public static FirebaseUser getUtilizadorAtual(){
        FirebaseAuth utilizador = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return utilizador.getCurrentUser();
}


public static boolean atualizarTipoUtilizador(String tipo){

        try{
            FirebaseUser user = getUtilizadorAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build();

            user.updateProfile(profile);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
}



}
