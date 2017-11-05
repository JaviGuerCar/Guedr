package com.javi_macbook.guedr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ForecastActivity : AppCompatActivity() {

    companion object {
        val REQUEST_UNITS = 1
    }

    lateinit var maxTemp: TextView
    lateinit var minTemp: TextView

    var forecast: Forecast? = null
        set(value) {
            // Field es una palabra reservada de Kotlin, nos sirve para establecer el valor de Forecast
            field = value
            val forecastImage = findViewById<ImageView>(R.id.forecast_image)
            maxTemp = findViewById(R.id.max_temp)
            minTemp = findViewById(R.id.min_temp)
            val humidity = findViewById<TextView>(R.id.humidity)
            val forecastDescription = findViewById<TextView>(R.id.forecast_description)

            // Actualizamos la vista con el modelo
            // Si value es distinto de null se ejecuta este código
            value?.let { //equivale a if (value != null)
                forecastImage.setImageResource(value.icon)
                forecastDescription.text = value.description
                val humidityString = getString(R.string.humidity_format, value.humidity)
                humidity.text = humidityString
                updateTemperature()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        forecast = Forecast(25f, 10f, 35f, "Soleado con alguna nube", R.drawable.ico_01)

    }

    // Método que define qué opciones de menú tenemos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.settings, menu)

        return true
    }

    // método que dice que se hace una vez que se ha pulsado una opcion de menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_show_settings){
            //Aqui sabemos que se ha pulsado nuestro boton del menu
            val units = if (temperatureUnits() == Forecast.TempUnit.CELSIUS)
                R.id.celsius_rb
            else
                R.id.farenheit_rb

            val intent = SettingsActivity.intent(this, units)
            //Esto lo hariamos si la 2º pantalla no nos tiene que devolver nada
            //startActivity(intent)

            // Para que la 2º actividad nos devuelva los valores
            startActivityForResult(intent, REQUEST_UNITS)

            return true

        }
        return super.onOptionsItemSelected(item)
    }

    //este método se ejecuta cuando la 2º activity hace algo que devuelve un resultado que espera la 1º activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_UNITS){
            if (resultCode == Activity.RESULT_OK){
                val unitSelected = data?.getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.celsius_rb)
                when (unitSelected){
                    R.id.celsius_rb -> {
                        Log.v("TAG", "Soy ForecastActivity y han pulsado OK y Celsius")
                        //Toast.makeText(this, "Celsius seleccionado",Toast.LENGTH_LONG).show()
                    }
                    R.id.farenheit_rb -> {
                        Log.v("TAG", "Soy ForecastActivity y han pulsado OK y Fahrenheit")
                        //Toast.makeText(this, "Fahrenheit seleccionado",Toast.LENGTH_LONG).show()
                    }
                }

                val oldShowCelsius = temperatureUnits() // Me guardo las pref para luego por si el usuario quiere deshacer

                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, unitSelected == R.id.celsius_rb)// TRUE OR FALSE
                        .apply()

                updateTemperature()


                Snackbar.make(findViewById<View>(android.R.id.content), "Han cambiado las preferencias", Snackbar.LENGTH_LONG).setAction("Deshacer") {
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit()
                            .putBoolean(PREFERENCE_SHOW_CELSIUS, oldShowCelsius == Forecast.TempUnit.CELSIUS)// TRUE OR FALSE
                            .apply()

                    updateTemperature()
                }.show()
            }
            else{
                Log.v("TAG", "Soy ForecastActivity y han pulsado CANCEL")
            }
        }

    }

    private fun updateTemperature() {
        val units = temperatureUnits()
        val unitsString = temperatureUnitsString(units)

        val maxTempString = getString(R.string.max_temp_format, forecast?.getMaxTemp(units), unitsString)
        val minTempString = getString(R.string.min_temp_format, forecast?.getMinTemp(units), unitsString)
        maxTemp.text = maxTempString
        minTemp.text = minTempString
    }

    private fun temperatureUnitsString(units: Forecast.TempUnit) = when (units){
        Forecast.TempUnit.CELSIUS -> "ºC"
        else -> "ºF"
    }


    private fun temperatureUnits(): Forecast.TempUnit = if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREFERENCE_SHOW_CELSIUS, true)){
            Forecast.TempUnit.CELSIUS
    }
    else{
            Forecast.TempUnit.FAHRENHEIT
    }

}
