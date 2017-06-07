package jroldan.acdat.cuadernoclase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AgregarAlumnoActivity extends AppCompatActivity {
    public static final String URL = "alumno";
    public static final int OK = 1;

    EditText edtNombre, edtDireccion, edtCiudad, edtCodigoPostal, edtTelefono, edtEmail;
    Button btnAgregar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alumno);
        setTitle(getString(R.string.agregarAlumno));

        edtNombre = (EditText) findViewById(R.id.edtAgregarAlumnoNombre);
        edtDireccion = (EditText) findViewById(R.id.edtAgregarAlumnoDireccion);
        edtCiudad = (EditText) findViewById(R.id.edtAgregarAlumnoCiudad);
        edtCodigoPostal = (EditText) findViewById(R.id.edtAgregarAlumnoCodigoPostal);
        edtTelefono = (EditText) findViewById(R.id.edtAgregarAlumnoTelefono);
        edtEmail = (EditText) findViewById(R.id.edtAgregarAlumnoEmail);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre, direccion, ciudad, codpostal, telefono, email;
                nombre = edtNombre.getText().toString();
                direccion = edtDireccion.getText().toString();
                ciudad = edtCiudad.getText().toString();
                codpostal = edtCodigoPostal.getText().toString();
                telefono = edtTelefono.getText().toString();
                email = edtEmail.getText().toString();
                try {
                    Alumno alumno = new Alumno(nombre, direccion, ciudad, codpostal, telefono, email);
                    conectar(alumno);
                } catch (Exception e) {
                    Toast.makeText(AgregarAlumnoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void conectar(final Alumno alumno) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RequestParams params = new RequestParams();
        params.put("nombre", alumno.getNombre());
        params.put("direccion", alumno.getDireccion());
        params.add("ciudad", alumno.getCiudad());
        params.add("codpostal", alumno.getCodpostal());
        params.add("telefono", alumno.getTelefono());
        params.put("email", alumno.getEmail());
        RestClient.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage(getString(R.string.conectando));
                progreso.setCancelable(false);
                progreso.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progreso.dismiss();
                Result result;
                Gson gson = new Gson();
                String mensaje;
                result = gson.fromJson(String.valueOf(response), Result.class);
                if (result != null)
                    if (result.getCode()) {
                        mensaje = getString(R.string.exitoAgregar);
                        Intent i = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", result.getLast());
                        bundle.putString("nombre", alumno.getNombre());
                        bundle.putString("direccion", alumno.getDireccion());
                        bundle.putString("ciudad", alumno.getCiudad());
                        bundle.putString("codpostal", alumno.getCodpostal());
                        bundle.putString("telefono", alumno.getTelefono());
                        bundle.putString("email", alumno.getEmail());
                        i.putExtras(bundle);
                        setResult(OK, i);
                        finish();
                    } else
                        mensaje = getString(R.string.errorAgregar) + "\n" + result.getMessage();
                else
                    mensaje = getString(R.string.datosNulos);
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progreso.dismiss();
                Toast.makeText(AgregarAlumnoActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progreso.dismiss();
                Toast.makeText(AgregarAlumnoActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
