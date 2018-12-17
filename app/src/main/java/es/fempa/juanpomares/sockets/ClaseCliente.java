package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClaseCliente extends AppCompatActivity {

    EditText editTextIpServidor;
    EditText editTextPuertoServidor;
    EditText editTextNombreUsuario;
    String ipServidor;
    int puertoServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_cliente);

        editTextIpServidor = (EditText) findViewById(R.id.plain_ip);
        editTextPuertoServidor = (EditText) findViewById(R.id.plain_puerto);
        editTextNombreUsuario = (EditText) findViewById(R.id.plain_nombre);
        Button botonAtras = (Button) findViewById(R.id.buttonClienteAtras);
        Button botonChat = (Button) findViewById(R.id.buttonClienteChat);

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

                boolean empezarActividad = true;
                Intent intent = new Intent(ClaseCliente.this, ClaseChat.class);
                ipServidor = editTextIpServidor.getText().toString();
                if(ipServidor.length()>5) {
                    intent.putExtra("ip del servidor", ipServidor);
                    intent.putExtra("soy servidor", false);

                    try {
                        puertoServidor = Integer.parseInt(editTextPuertoServidor.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(ClaseCliente.this, "Error puerto no válido", Toast.LENGTH_SHORT).show();
                        empezarActividad = false;
                    }
                    if(puertoServidor >= 1024 && puertoServidor <= 65535){
                        intent.putExtra("puerto del servidor", puertoServidor);

                        if(editTextNombreUsuario.length() > 0){
                            intent.putExtra("nombre del cliente", editTextNombreUsuario.getText());
                        } else{
                            Toast.makeText(ClaseCliente.this, "Error no ha introducido un nombre.", Toast.LENGTH_SHORT).show();
                            empezarActividad = false;
                        }

                    } else {
                        Toast.makeText(ClaseCliente.this, "Error puerto no válido. Introduzca un valor entre 1024 y 65535.", Toast.LENGTH_LONG).show();
                        empezarActividad = false;
                    }

                } else {
                    Toast.makeText(ClaseCliente.this, "Error ip no válida", Toast.LENGTH_SHORT).show();
                    empezarActividad = false;
                }

                if(empezarActividad == true){
                    startActivity(intent);
                }
            }
        });
    }
}
