package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ClaseCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_cliente);
    }

    public void manejador(View v){
        if (v.getId() == R.id.button5){
            Intent intent = new Intent(ClaseCliente.this, ClasePrincipal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if(v.getId() == R.id.b_vamosAChatear) {
            Intent intent = new Intent(ClaseCliente.this, ClaseChat.class);
            startActivity(intent);
        }
    }
}
