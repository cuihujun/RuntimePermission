package com.github.florent37.runtimepermission.sample

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.florent37.runtimepermission.sample.AppendText.appendText
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import kotlinx.android.synthetic.main.runtime_permissions_activity_request.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Sample of a very basic activity asking for permission.
 * It shows a button to trigger the permission dialog if permission is needed,
 * and hide it when it doesn't
 */
class RuntimePermissionMainActivityKotlinCoroutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.runtime_permissions_activity_request)

        requestView.setOnClickListener {
            myMethod()
        }
    }

    fun myMethod() = launch(UI) {
        try {
            val result = askPermission(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
            //all permissions already granted or just granted
            //your action
            resultView.setText("Accepted :${result.accepted.toString()}")

        } catch (e: PermissionException) {
            appendText(resultView, "Denied :")
            //the list of denied permissions
            e.denied.forEach { permission ->
                appendText(resultView, permission)
            }
            //but you can ask them again, eg:


             AlertDialog.Builder(this@RuntimePermissionMainActivityKotlinCoroutine )
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes", { dialog, which -> /* ask again */ e.askAgain() })
                    .setNegativeButton("no", { dialog, which -> dialog.dismiss(); })
                    .show();


            appendText(resultView, "ForeverDenied")
            //the list of forever denied permissions, user has check 'never ask again'
            e.foreverDenied.forEach { permission ->
                appendText(resultView, permission)
            }
            //you need to open setting manually if you really need it
            //e.goToSettings();
        }
    }

}
