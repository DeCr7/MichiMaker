package ni.edu.uam.michimaker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransformacionDto {

    private Integer id;

    private String nombreFiltro;

    private String fecha;

    private String rutaImagen;

    private String imagenBase64;

    private Integer usuarioId;

    private String leyenda;
}