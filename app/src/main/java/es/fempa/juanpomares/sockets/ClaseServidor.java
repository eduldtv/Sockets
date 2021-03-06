package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClaseServidor extends AppCompatActivity {

    EditText editTextPuertoServidor;
    EditText editTextNombreServidor;
    int puertoServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_clase_servidor);

        Button botonAtras = (Button)findViewById(R.id.buttonServidorAtras);
        Button botonChat = (Button) findViewById(R.id.buttonServidorChat);

        editTextPuertoServidor = (EditText) findViewById(R.id.plainServidorPuerto);
        editTextNombreServidor = (EditText) findViewById(R.id.plainServidorNombre);


        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaseServidor.this, ClasePrincipal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        botonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ClaseServidor.this, ClaseChat.class);
                boolean empezarActividad = true;
                try {
                    puertoServidor = Integer.parseInt(editTextPuertoServidor.getText().toString());
                }catch (Exception e){
                    Toast.makeText(ClaseServidor.this, "Error puerto no válido", Toast.LENGTH_SHORT).show();
                    empezarActividad = false;
                }
                if(puertoServidor >= 1024 && puertoServidor <= 65535){
                    intent.putExtra("puerto del servidor", puertoServidor);

                    intent.putExtra("soy servidor", true);

                    if(editTextNombreServidor.length() > 0){
                        intent.putExtra("nombre del servidor", editTextNombreServidor.getText());
                    } else{
                        Toast.makeText(ClaseServidor.this, "Error no ha introducido un nombre.", Toast.LENGTH_SHORT).show();
                        empezarActividad = false;
                    }

                } else {
                    Toast.makeText(ClaseServidor.this, "Error puerto no válido. Introduzca un valor entre 1024 y 65535.", Toast.LENGTH_LONG).show();
                    empezarActividad = false;
                }

                if(empezarActividad == true){
                    startActivity(intent);
                }

            }
        });
    }

}
