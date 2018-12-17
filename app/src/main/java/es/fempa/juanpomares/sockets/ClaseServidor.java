package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ClaseServidor extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_clase_servidor);

        Button botonAtras = (Button)findViewById(R.id.buttonServidorAtras);
        Button botonChat = (Button) findViewById(R.id.buttonServidorChat);

        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaseCliente.this, ClasePrincipal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        botonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void manejador(View v){
        if (v.getId() == R.id.button4){
            Intent intent = new Intent(ClaseServidor.this, ClasePrincipal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if(v.getId() == R.id.b_vamosAChatear2) {
            Intent intent = new Intent(ClaseServidor.this, ClaseChat.class);
            startActivity(intent);
        }
    }
}
