package ni.edu.uam.michimaker_api.controller;

import ni.edu.uam.michimaker_api.dto.TransformacionDto;
import ni.edu.uam.michimaker_api.dto.TransformacionFeedDto;
import ni.edu.uam.michimaker_api.entity.Transformacion;
import ni.edu.uam.michimaker_api.entity.Usuario;
import ni.edu.uam.michimaker_api.repository.TransformacionRepository;
import ni.edu.uam.michimaker_api.repository.UsuarioRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/transformaciones")
@CrossOrigin("*")
public class TransformacionController {

    private final TransformacionRepository transformacionRepository;
    private final UsuarioRepository usuarioRepository;

    public TransformacionController(
            TransformacionRepository transformacionRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.transformacionRepository = transformacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public ResponseEntity<TransformacionFeedDto> guardar(
            @RequestBody TransformacionDto dto
    ) {

        System.out.println(
                "USUARIO RECIBIDO: " + dto.getUsuarioId()
        );


        Usuario usuario = usuarioRepository
                .findById(dto.getUsuarioId())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Usuario no encontrado"
                        )
                );


        Transformacion t = new Transformacion();

        t.setNombreFiltro(dto.getNombreFiltro());
        t.setFecha(dto.getFecha());
        t.setRutaImagen(dto.getRutaImagen());
        t.setLeyenda(dto.getLeyenda());
        t.setUsuario(usuario);


        if(dto.getImagenBase64() != null &&
                !dto.getImagenBase64().isBlank()) {

            byte[] bytes =
                    Base64.getDecoder()
                            .decode(dto.getImagenBase64());

            t.setImagen(bytes);
        }


        Transformacion guardada =
                transformacionRepository.save(t);


        return ResponseEntity.ok(
                toFeedDto(guardada)
        );
    }

    // =========================
    // FEED GLOBAL
    // =========================

    @GetMapping("/feed")
    public ResponseEntity<List<TransformacionFeedDto>> feed() {

        List<TransformacionFeedDto> feed =
                transformacionRepository
                        .findAllByOrderByIdDesc()
                        .stream()
                        .map(this::toFeedDto)
                        .toList();

        return ResponseEntity.ok(feed);
    }

    // =========================
    // FEED POR USUARIO
    // =========================

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<TransformacionFeedDto>> porUsuario(
            @PathVariable Integer id
    ) {

        List<TransformacionFeedDto> lista =
                transformacionRepository
                        .findByUsuarioIdOrderByIdDesc(id)
                        .stream()
                        .map(this::toFeedDto)
                        .toList();

        return ResponseEntity.ok(lista);
    }

    // =========================
    // FEED ALEATORIO
    // =========================

    @GetMapping("/feed/random")
    public ResponseEntity<List<TransformacionFeedDto>> feedRandom() {

        return ResponseEntity.ok(
                transformacionRepository
                        .feedAleatorio()
                        .stream()
                        .map(this::toFeedDto)
                        .toList()
        );
    }

    // =========================
    // ELIMINAR
    // =========================

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> eliminarPorUsuario(
            @PathVariable Integer id
    ) {

        transformacionRepository
                .deleteByUsuarioId(id);

        return ResponseEntity.ok().build();
    }

    // =========================
    // MAPPER
    // =========================

    private TransformacionFeedDto toFeedDto(
            Transformacion t
    ) {

        TransformacionFeedDto dto =
                new TransformacionFeedDto();

        dto.setId(
                t.getId()
        );

        dto.setNombreFiltro(
                t.getNombreFiltro()
        );

        dto.setFecha(
                t.getFecha()
        );

        dto.setRutaImagen(
                t.getRutaImagen()
        );

        dto.setLeyenda(
                t.getLeyenda()
        );

        dto.setUsuarioId(
                t.getUsuario().getId()
        );

        dto.setUsername(
                t.getUsuario().getUsername()
        );

        dto.setFotoPerfil(
                t.getUsuario().getFotoPerfil()
        );

        // NUEVO:
        if (t.getImagen() != null) {

            dto.setImagenBase64(
                    Base64.getEncoder()
                            .encodeToString(
                                    t.getImagen()
                            )
            );
        }

        return dto;
    }
}