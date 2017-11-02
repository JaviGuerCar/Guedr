package com.javi_macbook.guedr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(TAG, "He pasado por onCreate")

        if (savedInstanceState != null) {
            Log.v(TAG, "SavedInstanceState no es null y su clave vale: ${savedInstanceState.getString("clave")}")
        }
        else{
            Log.v(TAG, "SavedInstanceState es null")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.v(TAG, "He pasado por onSaveInstanceState")

  /*      if (outState != null) {
            // Aqui estamos seguros de poder llamar a métodos sin NPE
            outState.putString("clave", "valor")
        }*/

        outState?.putString("clave", "valor")
    }
}
