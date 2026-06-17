package ni.edu.uam.michimaker.utils

import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.model.Transformacion

fun TransformacionEntity.toModel(): Transformacion {

    return Transformacion(
        id = id,
        filtro = nombreFiltro,
        fecha = fecha,
        rutaImagen = rutaImagen
    )
}

fun Transformacion.toEntity(): TransformacionEntity {

    return TransformacionEntity(
        id = id,
        nombreFiltro = filtro,
        fecha = fecha,
        rutaImagen = rutaImagen,
        usuarioId = 0,
        leyenda = ""
    )
}