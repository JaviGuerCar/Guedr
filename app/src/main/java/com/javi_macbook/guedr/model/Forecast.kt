package com.javi_macbook.guedr.model


data class Forecast(val maxTemp: Float, val minTemp: Float, val humidity: Float, val description: String, val icon: Int) {

    enum class TempUnit {
        CELSIUS,
        FAHRENHEIT
    }

    // método que se ejecuta al iniciar la clase, para comprobar cosas
    init {
//        if (humidity < 0 || humidity > 100){
//            throw IllegalArgumentException("La humedad debe estar entre 0 y 100")
//        }

        if (humidity !in 0f..100f){
            throw IllegalArgumentException("La humedad debe estar entre 0 y 100")
        }
    }

    protected fun toFahrenheit(celsius: Float) = celsius * 1.8f + 32

    fun getMaxTemp(units: TempUnit) = when (units){
        TempUnit.CELSIUS -> maxTemp
        TempUnit.FAHRENHEIT -> toFahrenheit(maxTemp)
    }

    fun getMinTemp(units: TempUnit) = when (units){
        TempUnit.CELSIUS -> minTemp
        TempUnit.FAHRENHEIT -> toFahrenheit(minTemp)
    }
}