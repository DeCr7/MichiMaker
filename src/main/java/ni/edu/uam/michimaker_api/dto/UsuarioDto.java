package ni.edu.uam.michimaker_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioDto {

    private Integer id;

    private String username;

    private String nombre;

    // Dentro de tu clase UsuarioDto.java añade:
    private String biografia;

    private String correo;

    private String password;

    private String fotoPerfil;

    public UsuarioDto() {
    }
}