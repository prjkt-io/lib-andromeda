package projekt.andromedaclientexample.java

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import projekt.andromeda.client.AndromedaClient
import projekt.andromeda.client.AndromedaStatusBar

class MainActivityKt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndromedaClient.initialize(this)
        findViewById<Button>(R.id.expandNotification).setOnClickListener {
            AndromedaStatusBar.expandNotifications()
        }
        requestPermissions(
                arrayOf(AndromedaClient.ACCESS_PERMISSION),
                ANDROMEDA_REQUEST_CODE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ANDROMEDA_REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    if (!AndromedaClient.isServerActive) {
                        AlertDialog.Builder(this)
                                .setMessage("Andromeda server inactive")
                                .setPositiveButton("OK") { _, _ -> finish() }
                                .show()
                    }
                } else {
                    AlertDialog.Builder(this)
                            .setMessage("Andromeda access permission needed")
                            .setPositiveButton("OK") { _, _ -> finish() }
                            .show()
                }
            }
        }
    }

    companion object {
        private const val ANDROMEDA_REQUEST_CODE_PERMISSION = 14045
    }
}