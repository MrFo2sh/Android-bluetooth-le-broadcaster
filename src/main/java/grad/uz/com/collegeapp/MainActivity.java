package grad.uz.com.collegeapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

public class MainActivity extends Activity {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    Button btnLogin;
    EditText txtUsername;
    EditText txtPassword;

    OkHttpClient client = new OkHttpClient();
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                btnLogin.setEnabled(false);
            }
        });

    }


//    @TargetApi(Build.VERSION_CODES.M)
//    void req() {
//        requestPermissions(new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, 1 );
//    }



    void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    void login() {
        String username = txtUsername.getText().toString().trim();
        String password = txtUsername.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty()) return;
        btnLogin.setEnabled(false);

        RequestBody body = RequestBody.create(JSON, "{\"username\": \""+username+"\", \"password\": \""+password+"\"}");
        Request request = new Request.Builder()
                .url("http://aast.voidbits.co/api/login")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 403) {
                    showMessage("Invalid credentials");
                }
                else if(response.code() == 200) {
                    token = response.body().string().split("\"token\":")[1].split("\\}")[0];
                    token = token.split("\"")[1].split("\"")[0];
                    System.out.println(token);
                    loggedIn();
                }
                else  throw new IOException("Unexpected code " + response);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

        });


    }

    private void loggedIn() {
        Intent intent = new Intent(this, HomeActivity.class);
//        showMessage("Logged in successfully");
        finish();
        startActivity(intent);
    }


}
