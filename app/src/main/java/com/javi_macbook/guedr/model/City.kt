package com.javi_macbook.guedr.model

import java.io.Serializable

data class City (var name: String, var forecast: Forecast?) : Serializable {

    // Constructor de conveniencia, cuando forecast es null
    constructor(name: String) : this(name, null)

    override fun toString() = name
}