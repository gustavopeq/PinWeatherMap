package gustavo.projects.pinweathermap.domain.model

data class LocationWeather(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val temp: Int, //°C
    val feelsLike: Int, //°C
    val tempMin: Int, //°C
    val tempMax: Int, //°C
    val humidity: Int, //%
    val pressure: Int, //hPa
    val unixTime: Long, // Unix time
    val sunRiseTime: Long, // Unix Time
    val sunSetTime: Long, //Unix Time
    val windSpeed: Double, // Meter/Second
    val main: String,
    val description: String,
    val clouds: Int //%,
)
