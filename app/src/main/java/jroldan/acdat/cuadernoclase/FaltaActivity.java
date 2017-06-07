package jroldan.acdat.cuadernoclase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import jroldan.acdat.cuadernoclase.FaltaAdapter;
import jroldan.acdat.cuadernoclase.ClickListener;
import jroldan.acdat.cuadernoclase.RecyclerTouchListener;
import jroldan.acdat.cuadernoclase.Falta;
import jroldan.acdat.cuadernoclase.Result;
import jroldan.acdat.cuadernoclase.RestClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FaltaActivity extends AppCompatActivity {
    public static final String URL_API = "falta";
    private static final int OK = 1;
    public static final int UPDATE_CODE = 200;

    private FloatingActionButton fabGuardarFaltas;
    private RecyclerView rcvFaltas;
    private FaltaAdapter adapter;
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falta);
        setTitle(R.string.faltas);

        fabGuardarFaltas = (FloatingActionButton) findViewById(R.id.fabGuardarFaltas);
        fabGuardarFaltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < adapter.getItemCount(); i++)
                    conectar(adapter.getAt(i));
            }
        });

        rcvFaltas = (RecyclerView) findViewById(R.id.rcvFaltas);
        rcvFaltas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FaltaAdapter(this);
        rcvFaltas.setAdapter(adapter);
        rcvFaltas.addOnItemTouchListener(new RecyclerTouchListener(this, rcvFaltas, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                actualizarFalta(adapter.getAt(position));
                posicion = position;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        descargarFaltas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Falta falta = new Falta();
        if (requestCode == UPDATE_CODE) {
            if (resultCode == OK) {
                falta.setId(data.getIntExtra("id", 0));
                falta.setFecha(data.getStringExtra("fecha"));
                falta.setAlumno(data.getStringExtra("alumno"));
                falta.setActitud(data.getIntExtra("actitud", 0));
                falta.setObservaciones(data.getStringExtra("observaciones"));
                falta.setTipo(data.getIntExtra("tipo", 0));
                falta.setTrabajo(data.getIntExtra("trabajo", 0));
                adapter.modifyAt(falta, posicion);
            }
        }
    }

    private void actualizarFalta(Falta falta) {
        Intent i = new Intent(this, ActualizarFaltaActivity.class);
        i.putExtra("falta", falta);
        startActivityForResult(i, UPDATE_CODE);
    }

    private void descargarFaltas() {
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
                        adapter.set(result.getFaltas());
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
                Toast.makeText(FaltaActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progreso.dismiss();
                Toast.makeText(FaltaActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void conectar(final Falta falta) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(falta.getId()));
        params.add("fecha", String.valueOf(falta.getFecha()));
        params.add("alumno", String.valueOf(falta.getAlumno()));
        params.add("trabajo", String.valueOf(falta.getTrabajo()));
        params.add("tipo", String.valueOf(falta.getTipo()));
        params.add("actitud", String.valueOf(falta.getActitud()));
        params.add("observaciones", falta.getObservaciones());
        RestClient.put(URL_API + "/" + falta.getId(), params, new JsonHttpResponseHandler() {
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

                    } else
                        message = getString(R.string.errorActualizar) + "\n" + result.getMessage();
                else
                    message = getString(R.string.datosNulos);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progreso.dismiss();
                Toast.makeText(FaltaActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progreso.dismiss();
                Toast.makeText(FaltaActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
