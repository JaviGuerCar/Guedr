package com.javi_macbook.guedr.fragment


import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

import com.javi_macbook.guedr.R
import com.javi_macbook.guedr.model.Cities
import com.javi_macbook.guedr.model.City


class CityListFragment : Fragment() {

    lateinit var root: View
    private var onCitySelectedListener: OnCitySelectedListener? = null

    companion object {

        fun newInstance(): CityListFragment {
            val fragment = CityListFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (inflater!=null){
            root = inflater.inflate(R.layout.fragment_city_list, container, false)
            val list = root.findViewById<ListView>(R.id.city_list)
            // Creamos el Array adapter de city, pasando la activity, el formato de item de lista y el array de citys
            val adapter = ArrayAdapter<City>(activity, android.R.layout.simple_list_item_1, Cities.toArray())
            list.adapter = adapter

            // Así nos enteramos de que se ha pulsado un elemento de la lista
            list.setOnItemClickListener { parent, view, position, id ->
                // Aviso al listener
                onCitySelectedListener?.onCitySelected(Cities.get(position), position)
            }
        }

        return root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        commonAttach(context)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        commonAttach(activity)
    }

    override fun onDetach() {
        super.onDetach()
        onCitySelectedListener = null
    }

    fun commonAttach (listener: Any?){
        if (listener is OnCitySelectedListener)
            onCitySelectedListener = listener
    }


    interface OnCitySelectedListener {
        fun onCitySelected(city: City?, position: Int)
    }

}
