package gustavo.projects.pinweathermap.network.response

data class GetWeatherByCoordResponse(
        val base: String,
        val clouds: CloudsDto,
        val cod: Int,
        val coord: CoordDto,
        val dt: Long,
        val id: Int,
        val main: MainDto,
        val name: String,
        val sys: SysDto,
        val timezone: Int,
        val weather: List<WeatherDto>,
        val wind: WindDto
)