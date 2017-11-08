package com.javi_macbook.guedr.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.fragment.CityListFragment
import com.javi_macbook.guedr.model.Cities
import com.javi_macbook.guedr.model.City

class ForecastActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        //Comprobamos primero que no tenemos ya el fragment añadido a nuestra jerarquia
        if (fragmentManager.findFragmentById(R.id.city_list_fragment) == null){
            val fragment = CityListFragment.newInstance()
            fragmentManager.beginTransaction()
                    .add(R.id.city_list_fragment, fragment)
                    .commit()
        }


    }

    override fun onCitySelected(city: City?, position: Int) {
        startActivity(CityPagerActivity.intent(this, position))
    }

}
