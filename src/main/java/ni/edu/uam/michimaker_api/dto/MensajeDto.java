package ni.edu.uam.michimaker_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MensajeDto {
    private Long id;
    private Integer remitenteId;
    private Integer receptorId;
    private String contenido;
    private String fechaEnvio; // Enviado como String ISO o formateado para facilitar la vida a Android
    private boolean leido;

    public MensajeDto() {
    }
}