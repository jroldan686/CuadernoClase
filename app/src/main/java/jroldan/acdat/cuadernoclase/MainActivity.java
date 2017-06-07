package jroldan.acdat.cuadernoclase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnAlumnos, btnFaltas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlumnos = (Button) findViewById(R.id.btnAlumnos);
        btnAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaAlumnoActivity.class);
                startActivity(intent);
            }
        });

        btnFaltas = (Button) findViewById(R.id.btnFaltas);
        btnFaltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaltaActivity.class);
                startActivity(intent);
            }
        });
    }
}
