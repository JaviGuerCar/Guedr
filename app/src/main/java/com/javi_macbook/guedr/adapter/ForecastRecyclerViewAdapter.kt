package com.javi_macbook.guedr.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.model.Forecast
import kotlinx.android.synthetic.main.content_forecast.view.*

class ForecastRecyclerViewAdapter(val forecast: List<Forecast>?, val units: Forecast.TempUnit) : RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder>(){

    // Le añadimos el atributo onClickListener
    var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.content_forecast, parent, false)
        // Le pasamos el atributo a la vista, cuando de pulse en esta
        view.setOnClickListener(onClickListener)

        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder?, position: Int) {
        if (forecast != null){
            holder?.bindForecast(forecast[position], units, position)
        }
    }

    override fun getItemCount() = forecast?.size ?: 0



    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val day = itemView.findViewById<TextView>(R.id.day)
        val forecastImage = itemView.findViewById<ImageView>(R.id.forecast_image)
        val maxTemp = itemView.findViewById<TextView>(R.id.max_temp)
        val minTemp = itemView.findViewById<TextView>(R.id.min_temp)
        val humidity = itemView.findViewById<TextView>(R.id.humidity)
        val forecastDescription = itemView.findViewById<TextView>(R.id.forecast_description)

        fun bindForecast(forecast: Forecast, tempUnit: Forecast.TempUnit, position: Int){
            // Accediendo al contexto, ya que todas las vistas lo tienen
            val context = itemView.context

            // Actualizamos la vista con el modelo
            forecastImage.setImageResource(forecast.icon)
            forecastDescription.text = forecast.description
            val humidityString = context.getString(R.string.humidity_format, forecast.humidity)
            humidity.text = humidityString
            day.text = generateDayText(position)
            updateTemperature(forecast, tempUnit)

        }

        private fun generateDayText(position: Int) = when(position) {
            0 -> "Hoy"
            1 -> "Mañana"
            2 -> "Pasado Mañana"
            3 -> "Pasado Pasado Mañana"
            4 -> "Pasado Pasado Pasado Mañana"
            5 -> "Pasado Pasado Pasado Pasado Mañana"
            else -> "No sé en que día vivo"
        }

        private fun updateTemperature(forecast: Forecast, units: Forecast.TempUnit) {
            val unitsString = temperatureUnitsString(units)

            val maxTempString = itemView.context.getString(R.string.max_temp_format, forecast.getMaxTemp(units), unitsString)
            val minTempString = itemView.context.getString(R.string.min_temp_format, forecast.getMinTemp(units), unitsString)
            maxTemp.text = maxTempString
            minTemp.text = minTempString
        }

        private fun temperatureUnitsString(units: Forecast.TempUnit) = when (units){
            Forecast.TempUnit.CELSIUS -> "ºC"
            else -> "ºF"
        }

    }
}