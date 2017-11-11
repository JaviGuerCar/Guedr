package com.javi_macbook.guedr.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.model.Cities

class CityPagerFragment : Fragment() {

    companion object {
        val ARG_CITY_INDEX = "ARG_CITY_INDEX"

        fun newInstance(cityIndex: Int): CityPagerFragment {
            val arguments = Bundle()
            arguments.putInt(ARG_CITY_INDEX, cityIndex)
            val fragment = CityPagerFragment()
            fragment.arguments = arguments

            return fragment
        }
    }

    lateinit var root: View
    val pager by lazy { root.findViewById<ViewPager>(R.id.view_pager) }

    var initialCityIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        if (inflater != null) {
            root = inflater.inflate(R.layout.fragment_city_pager, container, false)

            // Operador Elvis!! oh yeah!
            initialCityIndex = arguments?.getInt(ARG_CITY_INDEX) ?: 0

            val adapter = object: FragmentPagerAdapter(fragmentManager) {
                override fun getItem(position: Int) = ForecastFragment.newInstance(Cities[position])

                override fun getCount() = Cities.count

                override fun getPageTitle(position: Int) = Cities[position].name
            }

            pager.adapter = adapter

            pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    updateCityInfo(position)
                }

            })

            pager.currentItem = initialCityIndex
            updateCityInfo(initialCityIndex)
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.pager, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
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

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        val menuPrev = menu?.findItem(R.id.previous)
        menuPrev?.setEnabled(pager.currentItem > 0)

        val menuNext = menu?.findItem(R.id.next)
        menuNext?.setEnabled(pager.currentItem < Cities.count - 1)
    }

    fun updateCityInfo(position: Int) {
        if (activity is AppCompatActivity) {
            val supportActionBar = (activity as? AppCompatActivity)?.supportActionBar
            supportActionBar?.title = Cities[position].name
        }
    }

    fun moveToCity(position: Int){
        pager.currentItem = position
    }

}
