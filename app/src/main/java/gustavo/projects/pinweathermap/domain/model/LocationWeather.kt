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
    val unixTime: Long, // Unix time
    val sunRiseTime: Long, // Unix Time
    val sunSetTime: Long, //Unix Time
    val windSpeed: Double, // Meter/Second
    val main: String,
    val description: String,
    val clouds: Double //%,
)
