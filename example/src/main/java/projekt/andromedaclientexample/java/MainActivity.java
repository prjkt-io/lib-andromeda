package projekt.andromedaclientexample.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import projekt.andromeda.client.AndromedaClient;
import projekt.andromeda.client.AndromedaInput;
import projekt.andromeda.client.AndromedaStatusBar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private final static int ANDROMEDA_REQUEST_CODE_PERMISSION = 14045;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndromedaClient.INSTANCE.initialize(this);
        findViewById(R.id.expandNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndromedaStatusBar.INSTANCE.expandNotifications();
            }
        });
        requestPermissions(
                new String[]{AndromedaClient.ACCESS_PERMISSION},
                ANDROMEDA_REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ANDROMEDA_REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    if (!AndromedaClient.INSTANCE.isServerActive()) {
                        new AlertDialog.Builder(this)
                                .setMessage("Andromeda server inactive")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("Andromeda access permission needed")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .show();
                }
        }
    }
}
