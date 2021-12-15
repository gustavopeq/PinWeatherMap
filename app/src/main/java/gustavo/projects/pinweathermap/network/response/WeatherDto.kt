package gustavo.projects.pinweathermap.network.response

data class WeatherDto(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)