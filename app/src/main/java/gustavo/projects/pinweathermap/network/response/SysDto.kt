package gustavo.projects.pinweathermap.network.response

data class SysDto(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)