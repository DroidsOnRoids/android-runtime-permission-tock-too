package pl.droidsonroids.demo.tocktoo

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), 1)
    }

    @SuppressLint("SetTextI18n")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val checkResultTextView = findViewById<TextView>(R.id.grantResultTextView)
        val grantResultTextView = findViewById<TextView>(R.id.checkResultTextView)

        val checkPermissionResult = checkSelfPermission(WRITE_EXTERNAL_STORAGE).toPermissionResult()
        val grantPermissionResult = grantResults.firstOrNull()?.toPermissionResult()
        checkResultTextView.text = "checkSelfPermission: $checkPermissionResult"
        grantResultTextView.text = "onRequestPermissionsResult: $grantPermissionResult"
    }

    private fun Int.toPermissionResult() = when (this) {
        PERMISSION_GRANTED -> "granted"
        PERMISSION_DENIED -> "denied"
        else -> "unknown"
    }
}