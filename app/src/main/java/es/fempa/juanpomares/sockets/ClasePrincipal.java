package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ClasePrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_clase_principal);

        Button botonCreadoPor = (Button) findViewById(R.id.b_creado_por);

        botonCreadoPor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreadores = new Intent(ClasePrincipal.this, ClaseAbout.class);
                startActivity(intentCreadores);
            }
        });
    }

    public void manejador(View v){

        if (v.getId() == R.id.b_servidor){
            Intent intent = new Intent(ClasePrincipal.this, ClaseServidor.class);
            startActivity(intent);
        }else if(v.getId() == R.id.b_cliente){
            Intent intent = new Intent(ClasePrincipal.this, ClaseCliente.class);
            startActivity(intent);
        }

    }
}
