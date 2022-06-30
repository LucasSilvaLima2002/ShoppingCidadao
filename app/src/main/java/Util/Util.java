package Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {
    public static boolean StatusInternet(Context context) {
        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo informação = conexao.getActiveNetworkInfo();
        if (informação != null && informação.isConnected()) {
            return true;
        } else {
            return false;
        }


    }
    public static boolean verificarCampos(Context context, String texto_1, String texto_2) {
        if (!texto_1.isEmpty() && !texto_2.isEmpty()) {
            if (StatusInternet(context)) {

            } else {
                Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                return false;

            }
        } else {
            Toast.makeText(context, "Preencha os campos", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}



