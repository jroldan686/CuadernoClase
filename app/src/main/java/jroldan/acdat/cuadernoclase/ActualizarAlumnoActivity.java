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

public class ActualizarAlumnoActivity extends AppCompatActivity {
    public static final String URL = "alumno";
    public static final int OK = 1;

    Alumno alumno;

    EditText edtNombre, edtDireccion, edtCiudad, edtCodigoPostal, edtTelefono, edtEmail;
    Button btnActualizar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_alumno);
        setTitle(getString(R.string.modificarAlumno));

        edtNombre = (EditText) findViewById(R.id.edtActualizarAlumnoNombre);
        edtDireccion = (EditText) findViewById(R.id.edtActualizarAlumnoDireccion);
        edtCiudad = (EditText) findViewById(R.id.edtActualizarAlumnoCiudad);
        edtCodigoPostal = (EditText) findViewById(R.id.edtActualizarAlumnoCodigoPostal);
        edtTelefono = (EditText) findViewById(R.id.edtActualizarAlumnoTelefono);
        edtEmail = (EditText) findViewById(R.id.edtActualizarAlumnoEmail);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
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
                    alumno.setNombre(nombre);
                    alumno.setDireccion(direccion);
                    alumno.setCiudad(ciudad);
                    alumno.setCodpostal(codpostal);
                    alumno.setTelefono(telefono);
                    alumno.setEmail(email);
                    conectar(alumno);
                } catch (Exception e) {
                    Toast.makeText(ActualizarAlumnoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent i = getIntent();
        alumno = (Alumno) i.getSerializableExtra("alumno");
        edtNombre.setText(alumno.getNombre());
        edtDireccion.setText(alumno.getDireccion());
        edtCiudad.setText(alumno.getCiudad());
        edtCodigoPostal.setText(alumno.getCodpostal());
        edtTelefono.setText(alumno.getTelefono());
        edtEmail.setText(alumno.getEmail());
    }

    private void conectar(final Alumno alumno) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(alumno.getId()));
        params.put("nombre", alumno.getNombre());
        params.put("direccion", alumno.getDireccion());
        params.add("ciudad", alumno.getCiudad());
        params.add("codpostal", alumno.getCodpostal());
        params.add("telefono", alumno.getTelefono());
        params.put("email", alumno.getEmail());
        RestClient.put(URL + "/" + alumno.getId(), params, new JsonHttpResponseHandler() {
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
                String message;
                result = gson.fromJson(String.valueOf(response), Result.class);
                if (result != null)
                    if (result.getCode()) {
                        message = getString(R.string.exitoActualizar);
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
                        message = getString(R.string.errorActualizar) + "\n" + result.getMessage();
                else
                    message = getString(R.string.datosNulos);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progreso.dismiss();
                Toast.makeText(ActualizarAlumnoActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progreso.dismiss();
                Toast.makeText(ActualizarAlumnoActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
