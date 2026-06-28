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
}