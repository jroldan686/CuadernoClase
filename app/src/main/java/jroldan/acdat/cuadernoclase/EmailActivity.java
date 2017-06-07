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

public class EmailActivity extends AppCompatActivity {
    public static final String URL_API = "email";
    public static final String MAIL = ListaAlumnoActivity.MAIL;
    public static final int OK = 1;

    EditText edtDe, edtClave, edtPara, edtAsunto, edtMensaje;
    Button btnAceptar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        setTitle(getString(R.string.enviarEmail));

        edtDe = (EditText) findViewById(R.id.edtDe);
        edtClave = (EditText) findViewById(R.id.edtClave);
        edtPara = (EditText) findViewById(R.id.edtPara);
        edtAsunto = (EditText) findViewById(R.id.edtAsunto);
        edtMensaje = (EditText) findViewById(R.id.edtMensaje);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = edtDe.getText().toString();
                String pass = edtClave.getText().toString();
                String to = edtPara.getText().toString();
                String subject = edtAsunto.getText().toString();
                String message = edtMensaje.getText().toString();
                if (from.isEmpty() || pass.isEmpty() || to.isEmpty()) {
                    Toast.makeText(EmailActivity.this, getString(R.string.emailIncompleto), Toast.LENGTH_SHORT).show();
                } else {
                    Email email = new Email(from, pass, to, subject, message);
                    conectar(email);
                }
            }
        });
        Intent i = getIntent();
        edtPara.setText(i.getStringExtra(MAIL));
    }

    private void conectar(Email email) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RequestParams params = new RequestParams();
        params.put("from", email.getFrom());
        params.put("password", email.getPassword());
        params.put("to", email.getTo());
        params.put("subject", email.getSubject());
        params.put("message", email.getMessage());
        RestClient.post(URL_API, params, new JsonHttpResponseHandler() {
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
                        message = getString(R.string.exitoEnviarEmail);
                        Intent i = new Intent();
                        setResult(OK, i);
                        finish();
                    } else
                        message = getString(R.string.errorEmail) + "\n" + result.getMessage();
                else
                    message = getString(R.string.datosNulos);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progreso.dismiss();
                Toast.makeText(EmailActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progreso.dismiss();
                Toast.makeText(EmailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
