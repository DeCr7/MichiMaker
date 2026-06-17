package ni.edu.uam.michimaker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransformacionFeedDto {

    private Integer id;

    private String nombreFiltro;
    private String fecha;
    private String rutaImagen;

    private Integer usuarioId;
    private String username;
    private String fotoPerfil;
    private String leyenda;

    public TransformacionFeedDto() {}

    // GETTERS & SETTERS

}