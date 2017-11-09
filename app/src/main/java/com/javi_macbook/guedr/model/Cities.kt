package com.javi_macbook.guedr.model

import com.javi_macbook.guedr.R
import java.io.Serializable

object Cities : Serializable {

    private var cities: List<City> = listOf(
            City("Madrid"),
            City("Jaén"),
            City("Quito")
    )

    val count
        get() = cities.size

    //fun getCity (index: Int) = cities[index]
    // Esto se puede hacer también así, sobreescribiendo el operador get[...]
    operator fun get(i: Int) = cities[i]

    fun toArray() = cities.toTypedArray()

}