package ni.edu.uam.michimaker.viewmodel

data class StatsState(
    val total: Int = 0,
    val porFiltro: Map<String, Int> = emptyMap()
)