package com.javi_macbook.guedr.activity

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter

import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.fragment.ForecastFragment
import com.javi_macbook.guedr.model.Cities
import com.javi_macbook.guedr.model.City

class CityPagerActivity : AppCompatActivity() {

    companion object {
        val EXTRA_CITY_INDEX = "EXTRA_CITY_INDEX"

        fun intent(context: Context, cityIndex: Int) : Intent{
            val intent = Intent(context, CityPagerActivity::class.java)
            intent.putExtra(EXTRA_CITY_INDEX, cityIndex)
            return intent
        }
    }

    val pager by lazy {findViewById<ViewPager>(R.id.view_pager)}
    val cities = Cities()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_pager)

        // Configuramos la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_launcher)
        setSupportActionBar(toolbar)


        val adapter = object: FragmentPagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                return ForecastFragment.newInstance(cities[position])
            }

            override fun getCount(): Int {
                return cities.count
            }

            override fun getPageTitle(position: Int): CharSequence {
                return cities[position].name
            }

        }

        pager.adapter = adapter

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                updateCityInfo(position)
            }

        })

        val initialCityIndex = intent.getIntExtra(EXTRA_CITY_INDEX, 0)
        pager.currentItem = initialCityIndex
        updateCityInfo(initialCityIndex)
    }



    private fun updateCityInfo(position: Int) {
        supportActionBar?.title = cities[position].name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.pager, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.previous -> {
                pager.currentItem = pager.currentItem - 1
                true
            }
            R.id.next -> {
                pager.currentItem++
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        invalidateOptionsMenu()

        val menuPrev = menu?.findItem(R.id.previous)
        menuPrev?.setEnabled(pager.currentItem > 0) //Esto devuelve true o false

        val menuNext = menu?.findItem(R.id.next)
        menuNext?.setEnabled(pager.currentItem < cities.count - 1)

        return true
    }
}
