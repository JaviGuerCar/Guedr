package com.javi_macbook.guedr.activity

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.fragment.CityListFragment
import com.javi_macbook.guedr.fragment.CityPagerFragment
import com.javi_macbook.guedr.model.Cities
import com.javi_macbook.guedr.model.City

class ForecastActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        // Chuleta para saber los detalles físicos del dispositivo donde se esta ejecutando esto
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val dpWidth = (width / metrics.density).toInt()
        val dpHeight = (height / metrics.density).toInt()
        val model = Build.MODEL
        val androidVersion = Build.VERSION.SDK_INT
        val dpi = metrics.densityDpi


        // Comprobamos que en la interfaz tenemos un FrameLayout llamado cityListFragment
        if (findViewById<View>(R.id.city_list_fragment) != null){
            //Comprobamos primero que no tenemos ya el fragment añadido a nuestra jerarquia
            if (fragmentManager.findFragmentById(R.id.city_list_fragment) == null){
                val fragment = CityListFragment.newInstance()
                fragmentManager.beginTransaction()
                        .add(R.id.city_list_fragment, fragment)
                        .commit()
            }
        }

        // Lo mismo pero con el fragment de FragmentCityPager
        if (findViewById<View>(R.id.fragment_city_pager) != null){
            if (fragmentManager.findFragmentById(R.id.fragment_city_pager) == null) {
                val fragment = CityPagerFragment.newInstance(0)
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_city_pager, fragment)
                        .commit()
            }
        }



    }

    override fun onCitySelected(city: City?, position: Int) {
        val cityPagerFragment = fragmentManager.findFragmentById(R.id.fragment_city_pager) as? CityPagerFragment
        if (cityPagerFragment == null){
            startActivity(CityPagerActivity.intent(this, position))
        }
        else{
            // Estamos viendo el view pager, le pedimos que se mueva
            cityPagerFragment.moveToCity(position)

        }
    }

}
