package ni.edu.uam.michimaker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatPreviewDto {
    private Integer usuarioId;   // ID del otro usuario
    private String username;     // Nombre del otro usuario
    private String fotoPerfil;   // Su foto en Base64
    private String ultimoMensaje;
    private String fechaEnvio;
    private boolean leido;

    // Constructor vacío requerido por Lombok / Jackson
    public ChatPreviewDto() {}

    // Constructor manual para mapear la consulta nativa limpia
    public ChatPreviewDto(Integer usuarioId, String username, String fotoPerfil, String ultimoMensaje, String fechaEnvio, boolean leido) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.fotoPerfil = fotoPerfil;
        this.ultimoMensaje = ultimoMensaje;
        this.fechaEnvio = fechaEnvio;
        this.leido = leido;
    }
}