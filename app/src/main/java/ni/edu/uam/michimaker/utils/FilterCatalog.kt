package ni.edu.uam.michimaker.utils

import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.model.CatFilter

object FilterCatalog {

    val filtros = listOf(

        CatFilter(
            nombre = "Gato",
            earsRes = R.drawable.cat_ears,
            noseRes = R.drawable.feline_nose,
            earsScale = 1.0f,
            noseScale = 1.25f   // 🌟 Aumentado para que sea más grande
        ),

        CatFilter(
            nombre = "Tigre",
            earsRes = R.drawable.tiger_ears,
            noseRes = R.drawable.feline_nose,
            earsScale = 1.1f,
            noseScale = 1.3f   // 🌟 Aumentado para que sea más grande
        ),

        CatFilter(
            nombre = "Leon",
            earsRes = R.drawable.lion_ears,
            noseRes = R.drawable.feline_nose,
            earsScale = 1.15f,
            noseScale = 1.35f  // 🌟 Aumentado para que sea más grande
        )
    )
}