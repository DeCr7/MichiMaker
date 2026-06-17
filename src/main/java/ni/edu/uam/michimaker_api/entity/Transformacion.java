package ni.edu.uam.michimaker_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "transformaciones")
public class Transformacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombreFiltro;

    private String fecha;

    @Column(length = 1000)
    private String rutaImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    private String leyenda;

    public Transformacion() {
    }
}