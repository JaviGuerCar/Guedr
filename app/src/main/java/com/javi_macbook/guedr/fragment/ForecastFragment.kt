package com.javi_macbook.guedr.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import com.javi_macbook.guedr.CONSTANT_OWM_APIKEY
import com.javi_macbook.guedr.model.Forecast
import com.javi_macbook.guedr.PREFERENCE_SHOW_CELSIUS
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.activity.SettingsActivity
import com.javi_macbook.guedr.adapter.ForecastRecyclerViewAdapter
import com.javi_macbook.guedr.model.City
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread


class ForecastFragment : Fragment() {

    enum class VIEW_INDEX(val index: Int) {
        LOADING(0),
        FORECAST(1)
    }

    companion object {
        val REQUEST_UNITS = 1
        private val ARG_CITY = "ARG_CITY"

        fun newInstance(city: City): ForecastFragment {
            // me creo una instancia del fragment
            val fragment = ForecastFragment()
            // paso argumentos al fragment
            val arguments = Bundle()
            arguments.putSerializable(ARG_CITY, city)
            fragment.arguments = arguments
            return fragment
        }
    }

    lateinit var root: View
    lateinit var viewSwitcher: ViewSwitcher
    lateinit var forecastList: RecyclerView

    var city: City? = null
        set(value){
            field = value
            if (value != null){
//                root.findViewById<TextView>(R.id.city).setText(value.name)
                forecast = value.forecast
            }
        }


    var forecast: List<Forecast>? = null
        set(value) {
            // Field es una palabra reservada de Kotlin, nos sirve para establecer el valor de Forecast
            field = value
            // Actualizamos la vista con el modelo
            // Si value es distinto de null se ejecuta este código
            if (value != null) { //equivale a if (value != null)
                //Asignamos el adapter al RecyclerView ahora que tenemos datos
                forecastList.adapter = ForecastRecyclerViewAdapter(value, temperatureUnits())

                // Le decimos al viewSwitcher que muestre el RelativeLayout
                viewSwitcher.displayedChild = VIEW_INDEX.FORECAST.index
                // Supercaché de la muerte
                city?.forecast = value
            }
            else {
                updateForecast()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        if (inflater != null) {
            root = inflater.inflate(R.layout.fragment_forecast, container, false)

            viewSwitcher = root.findViewById(R.id.view_switcher)
            viewSwitcher.setInAnimation(activity, android.R.anim.fade_in)
            viewSwitcher.setOutAnimation(activity, android.R.anim.fade_out)

            // 1) Accedemos al RecyclerView
            forecastList = root.findViewById(R.id.forecast_list)

            // 2) Le decimos cómo debe visualizarse el RecyclerView (su LayoutManager)
            forecastList.layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.recycler_columns))

            // 3) Le decimos como debe animarse el RecyclerView (su itemAnimator)
            forecastList.itemAnimator = DefaultItemAnimator()

            // 4) Por último, un Recycler View necesita un Adapter
            // Esto aun no lo hacemos aqui, porque aqui aun no tenemos datos


//            forecast = Forecast(25f, 10f, 35f, "Soleado con alguna nube", R.drawable.ico_01)
            if (arguments != null) {
                city = arguments.getSerializable(ARG_CITY) as? City
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_show_settings){
            //Aqui sabemos que se ha pulsado nuestro boton del menu
            val units = if (temperatureUnits() == Forecast.TempUnit.CELSIUS)
                R.id.celsius_rb
            else
                R.id.farenheit_rb

            val intent = SettingsActivity.intent(activity, units)
            //Esto lo hariamos si la 2º pantalla no nos tiene que devolver nada
            //startActivity(intent)

            // Para que la 2º actividad nos devuelva los valores
            startActivityForResult(intent, REQUEST_UNITS)

            return true

        }
        return super.onOptionsItemSelected(item)
    }

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

                PreferenceManager.getDefaultSharedPreferences(activity)
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, unitSelected == R.id.celsius_rb)// TRUE OR FALSE
                        .apply()

                updateTemperature()


                Snackbar.make(root, "Han cambiado las preferencias", Snackbar.LENGTH_LONG).setAction("Deshacer") {
                    PreferenceManager.getDefaultSharedPreferences(activity)
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

    //Para saber si, estando en un ViewPager, debemos refrescar las unidades de las temperaturas
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && forecast != null){
            updateTemperature()
        }
    }

    // Función para descarga en 2º plano con librería Anko
    private fun updateForecast() {
        // Le decimos al viewSwitcher que muestre el Progress Bar
        viewSwitcher.displayedChild = VIEW_INDEX.LOADING.index

        async(UI) {
            val newForecast: Deferred<List<Forecast>?> = bg { // Con bg hacemos la descarga en 2º plano
                downloadForecast(city)
            }

            val downloadedForecast = newForecast.await()
            if(downloadedForecast != null) {
                // Si ha ido bien, se lo asigno al atributo forecast
                forecast = downloadedForecast
            }
            else {
                // Ha habido error, le mostramos un diálogo
                AlertDialog.Builder(activity)
                        .setTitle("Error")
                        .setMessage("No pude descargar la información del tiempo")
                        .setPositiveButton("Reintentar", { dialog, _ ->
                            dialog.dismiss()
                            updateForecast()
                        })
                        .setNegativeButton("Salir", { _, _ -> activity.finish()})
                        .show()
            }
        }
    }

    fun downloadForecast(city: City?): List<Forecast>?{
        try{
            // Le doy un retardo
            Thread.sleep(1000)

            // Nos descargamos la información del tiempo a machete
            val url = URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=${city?.name}&lang=sp&units=metric&appid=${CONSTANT_OWM_APIKEY}")
            val jsonString = Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next()

            // Analizamos los datos que nos acabamos de descargar
            val jsonRoot = JSONObject(jsonString.toString())
            val list = jsonRoot.getJSONArray("list")

            // Me creo una lista que vamos a ir rellenando con los datos del JSON
            val forecasts = mutableListOf<Forecast>()

            //Recorremos la lista del objeto JSON
            for (dayIndex in 0..list.length() - 1){
                val today = list.getJSONObject(dayIndex)
                val max = today.getJSONObject("temp").getDouble("max").toFloat()
                val min = today.getJSONObject("temp").getDouble("min").toFloat()
                val humidity = today.getDouble("humidity").toFloat()
                val description = today.getJSONArray("weather").getJSONObject(0).getString("description")
                var iconString = today.getJSONArray("weather").getJSONObject(0).getString("icon")

                // Convertimos el texto iconString a un drawable
                iconString = iconString.substring(0, iconString.length - 1)
                val iconInt = iconString.toInt()
                val iconResource = when (iconInt) {
                    2 -> R.drawable.ico_02
                    3 -> R.drawable.ico_03
                    4 -> R.drawable.ico_04
                    9 -> R.drawable.ico_09
                    10 -> R.drawable.ico_10
                    11 -> R.drawable.ico_11
                    13 -> R.drawable.ico_13
                    50 -> R.drawable.ico_50
                    else -> R.drawable.ico_01
                }

                // Añadimos el Forecast que acabamos de descargar a la lista forecasts
                forecasts.add(Forecast(max, min, humidity, description, iconResource))
            }

            return forecasts


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null

    }

    private fun updateTemperature() {
        //forecastList.adapter.notifyDataSetChanged()
        forecastList.adapter = ForecastRecyclerViewAdapter(forecast, temperatureUnits())
    }

    private fun temperatureUnitsString(units: Forecast.TempUnit) = when (units){
        Forecast.TempUnit.CELSIUS -> "ºC"
        else -> "ºF"
    }


    private fun temperatureUnits(): Forecast.TempUnit = if (PreferenceManager.getDefaultSharedPreferences(activity)
            .getBoolean(PREFERENCE_SHOW_CELSIUS, true)){
        Forecast.TempUnit.CELSIUS
    }
    else{
        Forecast.TempUnit.FAHRENHEIT
    }

}