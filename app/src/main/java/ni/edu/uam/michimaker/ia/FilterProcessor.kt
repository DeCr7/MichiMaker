package ni.edu.uam.michimaker.ia

interface FilterProcessor {

    fun aplicarFiltro(
        rutaImagen: String,
        filtro: String
    ): String
}