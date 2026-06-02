package ni.edu.uam.michimaker.utils

import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.model.CatFilter

object FilterCatalog {

    val filtros = listOf(

        CatFilter(
            nombre = "Gato",
            earsRes = R.drawable.cat_ears,
            noseRes = R.drawable.feline_nose,
            earsScale = 0.75f,
            noseScale = 0.85f
        ),

        CatFilter(
            nombre = "Tigre",
            earsRes = R.drawable.tiger_ears,
            noseRes = R.drawable.feline_nose,
            earsScale = 0.8f,
            noseScale = 0.9f
        ),

        CatFilter(
            nombre = "Leon",
            earsRes = R.drawable.lion_ears,
            noseRes = R.drawable.feline_nose,
            earsScale = 0.85f,
            noseScale = 0.9f
        )
    )
}