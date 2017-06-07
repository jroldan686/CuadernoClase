package jroldan.acdat.cuadernoclase;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ListaAlumnoActivity extends AppCompatActivity {
    public static final String URL_API = "alumno";
    public static final String MAIL = "mail";
    public static final int ADD_CODE = 100;
    public static final int UPDATE_CODE = 200;
    public static final int OK = 1;

    private FloatingActionButton fabAgregarAlumno;
    private RecyclerView rcvAlumno;
    private AlumnoAdapter adapter;
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_lista);
        setTitle(getString(R.string.listaAlumnos));

        fabAgregarAlumno = (FloatingActionButton) findViewById(R.id.fabAgregarAlumno);
        fabAgregarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlumnoActivity.this, AgregarAlumnoActivity.class);
                startActivityForResult(intent, ADD_CODE);
            }
        });

        rcvAlumno = (RecyclerView) findViewById(R.id.rcvAlumno);
        rcvAlumno.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlumnoAdapter(this);
        rcvAlumno.setAdapter(adapter);
        rcvAlumno.addOnItemTouchListener(new RecyclerTouchListener(this, rcvAlumno, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent emailIntent = new Intent(getApplicationContext(), EmailActivity.class);
                emailIntent.putExtra(MAIL, adapter.getAt(position).getEmail());
                startActivity(emailIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                showPopup(view, position);
            }
        }));
        descargarAlumnos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Alumno alumno = new Alumno();
        if (requestCode == ADD_CODE) {
            if (resultCode == OK) {
                alumno.setId(data.getIntExtra("id", 1));
                alumno.setNombre(data.getStringExtra("nombre"));
                alumno.setDireccion(data.getStringExtra("direccion"));
                alumno.setCiudad(data.getStringExtra("ciudad"));
                alumno.setCodpostal(data.getStringExtra("codpostal"));
                alumno.setTelefono(data.getStringExtra("telefono"));
                alumno.setEmail(data.getStringExtra("email"));
                adapter.add(alumno);
            }
        }
        if (requestCode == UPDATE_CODE) {
            if (resultCode == OK) {
                alumno.setId(data.getIntExtra("id", 1));
                alumno.setNombre(data.getStringExtra("nombre"));
                alumno.setDireccion(data.getStringExtra("direccion"));
                alumno.setCiudad(data.getStringExtra("ciudad"));
                alumno.setCodpostal(data.getStringExtra("codpostal"));
                alumno.setTelefono(data.getStringExtra("telefono"));
                alumno.setEmail(data.getStringExtra("email"));
                adapter.modifyAt(alumno, posicion);
            }
        }
    }

    private void showPopup(View view, final int position) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modificarAlumno:
                        modify(adapter.getAt(position));
                        posicion = position;
                        return true;
                    case R.id.eliminarAlumno:
                        confirm(adapter.getAt(position).getId(), adapter.getAt(position).getNombre(), position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void modify(Alumno alumno) {
        Intent i = new Intent(this, ActualizarAlumnoActivity.class);
        i.putExtra("alumno", alumno);
        startActivityForResult(i, UPDATE_CODE);
    }

    private void confirm(final int idAlumno, String name, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format(getString(R.string.confirmacionEliminar), name))
                .setTitle(getString(R.string.eliminar))
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        conectar(idAlumno, position);
                    }
                })
                .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void conectar(int idAlumno, final int position) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RestClient.delete(URL_API + "/" + idAlumno, new JsonHttpResponseHandler() {
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
                        message = getString(R.string.exitoEliminar);
                        adapter.removeAt(position);
                    } else
                        message = getString(R.string.errorEliminar) + "\n" + result.getStatus() + "\n" +
                                result.getMessage();
                else
                    message = getString(R.string.datosNulos);
                Toast.makeText(ListaAlumnoActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progreso.dismiss();
                Toast.makeText(ListaAlumnoActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                progreso.dismiss();
                Toast.makeText(ListaAlumnoActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void descargarAlumnos() {
        final ProgressDialog progreso = new ProgressDialog(this);
        RestClient.get(URL_API + 's', new JsonHttpResponseHandler() {
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
                        adapter.set(result.getAlumnos());
                        message = getString(R.string.exitoDescarga);
                    } else
                        message = result.getMessage();
                else
                    message = getString(R.string.datosNulos);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progreso.dismiss();
                Toast.makeText(ListaAlumnoActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progreso.dismiss();
                Toast.makeText(ListaAlumnoActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
