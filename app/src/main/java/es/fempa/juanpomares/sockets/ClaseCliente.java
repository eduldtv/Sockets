package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClaseCliente extends AppCompatActivity {
    EditText ipServidor;
    EditText puertoServidor;
    EditText nombreCliente;
    String TheIP;
    int numPuertoServidor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_cliente);

        ipServidor = (EditText) findViewById(R.id.plain_ip);
        puertoServidor = (EditText) findViewById(R.id.plain_puerto);
        nombreCliente = (EditText) findViewById(R.id.plain_nombre);
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
                TheIP=ipServidor.getText().toString();
                if(TheIP.length()>5) {
                    intent.putExtra("ip del servidor", ipServidor.getText());

                    try {
                        numPuertoServidor = Integer.parseInt(puertoServidor.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(ClaseCliente.this, "Error puerto no válido", Toast.LENGTH_SHORT).show();
                        empezarActividad = false;
                    }
                    if(numPuertoServidor >= 1024 && numPuertoServidor <= 65535){
                        intent.putExtra("puerto del servidor", puertoServidor.getText());

                        if(nombreCliente.length() > 0){
                            intent.putExtra("nombre del cliente", nombreCliente.getText());
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
