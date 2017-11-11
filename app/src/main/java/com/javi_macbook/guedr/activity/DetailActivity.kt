package com.javi_macbook.guedr.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.TextView
import com.javi_macbook.guedr.PREFERENCE_SHOW_CELSIUS
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.model.Forecast

class DetailActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_FORECAST = "EXTRA_FORECAST"
        private val EXTRA_DAY = "EXTRA_DAY"
        private val EXTRA_CITY = "EXTRA_CITY"

        fun intent(context: Context, city: String?,  day: String?, forecast: Forecast): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_FORECAST, forecast)
            intent.putExtra(EXTRA_DAY, day)
            intent.putExtra(EXTRA_CITY, city)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.title = intent.getStringExtra(EXTRA_CITY)

        val forecast = intent.getSerializableExtra(EXTRA_FORECAST) as? Forecast

        if (forecast != null) {
            // Actualizamos la interfaz
            val day = findViewById<TextView>(R.id.day)
            val forecastImage = findViewById<ImageView>(R.id.forecast_image)
            val humidity = findViewById<TextView>(R.id.humidity)
            val forecastDescription = findViewById<TextView>(R.id.forecast_description)

            // Actualizamos la vista con el modelo
            forecastImage.setImageResource(forecast.icon)
            forecastDescription.text = forecast.description
            val humidityString = getString(R.string.humidity_format, forecast.humidity)
            humidity.text = humidityString
            day.text = intent.getStringExtra(EXTRA_DAY)
            updateTemperature(forecast, temperatureUnits())
        }
    }

    private fun updateTemperature(forecast: Forecast, units: Forecast.TempUnit) {
        val unitsString = temperatureUnitsString(units)

        val maxTempString = getString(R.string.max_temp_format, forecast.getMaxTemp(units), unitsString)
        val minTempString = getString(R.string.min_temp_format, forecast.getMinTemp(units), unitsString)
        val maxTemp = findViewById<TextView>(R.id.max_temp)
        val minTemp = findViewById<TextView>(R.id.min_temp)
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
