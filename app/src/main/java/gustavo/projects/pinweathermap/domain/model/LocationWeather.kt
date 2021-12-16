package gustavo.projects.pinweathermap.domain.model

data class LocationWeather(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val temp: Double, //°C
    val feelsLike: Double, //°C
    val tempMin: Double, //°C
    val tempMax: Double, //°C
    val humidity: Double, //%
    val pressure: Double, //hPa
    val unixTime: Int, // Unix time
    val windSpeed: Double, // Meter/Second
    val description: String,
    val clouds: Double //%,
)
