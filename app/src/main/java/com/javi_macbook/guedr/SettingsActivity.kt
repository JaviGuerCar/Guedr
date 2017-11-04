package com.javi_macbook.guedr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.RadioGroup

/**
 * Created by Javi-MacBook on 3/11/17.
 */
class SettingsActivity: AppCompatActivity() {

    companion object {
        val EXTRA_UNITS = "EXTRA_UNITS"

//      fun intent(context: Context): Intent {
//         return Intent(context, SettingsActivity::class.java)
//      }
    fun intent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    var radioGroup: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Esto sería un equivalente a una clase anónima en Kotlin (para los "javeros")
//        findViewById<View>(R.id.ok_btn).setOnClickListener(object: View.OnClickListener{
//            override fun onClick(p0: View?) {
//                // Aqui iría el código de Aceptar
//                acceptSettings()
//            }
//        })

        // Esto es una closure indicando que recibe view y no devuelve nada
//        findViewById<View>(R.id.ok_btn).setOnClickListener { view ->
//            acceptSettings()
//        }

        findViewById<View>(R.id.ok_btn).setOnClickListener {
            acceptSettings()
        }

        findViewById<View>(R.id.cancel_btn).setOnClickListener {
            cancelSettings()
        }

        radioGroup = findViewById(R.id.units_rg)
    }

    private fun cancelSettings() {
        setResult(Activity.RESULT_CANCELED)
        // Finalizamos la actividad, regresando a la anterior
        finish()
    }

    private fun acceptSettings(){
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_UNITS, radioGroup?.checkedRadioButtonId)
        setResult(Activity.RESULT_OK, returnIntent)
        // Finalizamos la actividad, regresando a la anterior
        finish()
    }
}