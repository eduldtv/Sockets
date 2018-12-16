package es.fempa.juanpomares.sockets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClaseAbout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_about);

        Button b_atras = (Button) findViewById(R.id.button3);

        b_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrincipal = new Intent(ClaseAbout.this, ClasePrincipal.class);
                startActivity(intentPrincipal);
            }
        });
    }
}
