package com.javi_macbook.guedr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = MainActivity::class.java.canonicalName
    var offlineWeatherImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Asigno una referencia al Botón
        findViewById<Button>(R.id.stone_button).setOnClickListener(this)
        findViewById<Button>(R.id.donkey_button).setOnClickListener(this)

        offlineWeatherImage = findViewById(R.id.offline_weather_image)

        Log.v(TAG, "He pasado por onCreate")

        if (savedInstanceState != null) {
            Log.v(TAG, "SavedInstanceState no es null y su clave vale: ${savedInstanceState.getString("clave")}")
        }
        else{
            Log.v(TAG, "SavedInstanceState es null")
        }
    }

    override fun onClick(v: View?) {
        Log.v(TAG, "Hemos pasado por onClick")
//        if (v == stoneButton){
//            Log.v(TAG, "Han pulsado el botón piedra")
//        } else {
//            Log.v(TAG, "Han pulsado el botón burro")
//        }

//        if (v != null){
//            if (v.id == R.id.stone_button){
//                Log.v(TAG, "Han pulsado el botón piedra")
//            } else {
//                Log.v(TAG, "Han pulsado el botón burro")
//            }
//        }

//        Log.v(TAG, when (v?.id) {
//            R.id.stone_button -> "Han pulsado el botón piedra"
//            R.id.donkey_button -> "Han pulsado el botón burro"
//            else -> "No sé que han pulsado"
//        })

        when (v?.id){
            R.id.stone_button -> {
                Log.v(TAG, "Han pulsado el botón piedra")
                offlineWeatherImage?.setImageResource(R.drawable.offline_weather)
            }
            R.id.donkey_button -> {
                Log.v(TAG, "Han pulsado el botón burro")
                offlineWeatherImage?.setImageResource(R.drawable.offline_weather2)
            }
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
