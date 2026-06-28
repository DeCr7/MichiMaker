package ni.edu.uam.michimaker_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String nombre;

    // CAMBIO: Usamos TEXT para que la biografía sea flexible
    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(unique = true)
    private String correo;

    @JsonIgnore
    private String password;

    // CAMBIO CRÍTICO: Usamos TEXT para que soporte la cadena Base64 sin límites de tamaño
    @Column(columnDefinition = "TEXT")
    private String fotoPerfil;

    @JsonIgnore
    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Transformacion> transformaciones = new ArrayList<>();

    public Usuario() {
    }
}