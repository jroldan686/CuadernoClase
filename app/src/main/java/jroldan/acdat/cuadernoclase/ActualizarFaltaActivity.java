package jroldan.acdat.cuadernoclase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ActualizarFaltaActivity extends AppCompatActivity {
    private static final int OK = 1;

    Spinner spnTipo, spnTrabajo, spnActitud;
    EditText edtObservaciones;

    Button btnAgregar;

    Falta falta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_falta);
        setTitle(R.string.agregarFalta);

        spnActitud = (Spinner) findViewById(R.id.spnActitud);
        spnTipo = (Spinner) findViewById(R.id.spnTipo);
        spnTrabajo = (Spinner) findViewById(R.id.spnTrabajo);
        edtObservaciones = (EditText) findViewById(R.id.edtObservaciones);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                falta.setTipo(spnTipo.getSelectedItemPosition());
                falta.setActitud(spnActitud.getSelectedItemPosition());
                falta.setTrabajo(spnTrabajo.getSelectedItemPosition());
                falta.setObservaciones(edtObservaciones.getText().toString());

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", falta.getId());
                bundle.putInt("tipo", falta.getTipo());
                bundle.putInt("trabajo", falta.getTrabajo());
                bundle.putInt("actitud", falta.getActitud());
                bundle.putString("observaciones", falta.getObservaciones());
                bundle.putString("alumno", falta.getAlumno());
                intent.putExtras(bundle);
                setResult(OK, intent);
                finish();
            }
        });

        Intent intent = getIntent();
        falta = (Falta) intent.getSerializableExtra("falta");
        spnActitud.setSelection(falta.getActitud());
        spnTrabajo.setSelection(falta.getTrabajo());
        spnTipo.setSelection(falta.getTipo());
        edtObservaciones.setText(falta.getObservaciones());
    }
}
