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

        fun intent(context: Context, units: Int): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(EXTRA_UNITS, units)
            return intent
        }
    }

    var radioGroup: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        findViewById<View>(R.id.ok_btn).setOnClickListener {
            acceptSettings()
        }

        findViewById<View>(R.id.cancel_btn).setOnClickListener {
            cancelSettings()
        }

        radioGroup = findViewById(R.id.units_rg)
        var radioSelected = intent.getIntExtra(EXTRA_UNITS, R.id.celsius_rb)
        radioGroup?.check(radioSelected)
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