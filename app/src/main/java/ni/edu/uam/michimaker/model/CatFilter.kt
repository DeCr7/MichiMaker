package ni.edu.uam.michimaker.model

data class CatFilter(
    val nombre: String,
    val earsRes: Int,
    val noseRes: Int,

    // Opcional pero recomendado para tu CatFilterManager mejorado
    val earsScale: Float = 0.75f,
    val noseScale: Float = 0.85f
)