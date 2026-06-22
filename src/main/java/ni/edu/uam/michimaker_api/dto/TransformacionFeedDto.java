package ni.edu.uam.michimaker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransformacionFeedDto {

    private Integer id;

    private String nombreFiltro;
    private String fecha;

    // Compatibilidad con imágenes locales
    private String rutaImagen;

    // Nueva imagen almacenada en PostgreSQL
    private String imagenBase64;

    private Integer usuarioId;
    private String username;
    private String fotoPerfil;

    private String leyenda;

    public TransformacionFeedDto() {
    }
}