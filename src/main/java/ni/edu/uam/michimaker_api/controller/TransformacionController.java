package ni.edu.uam.michimaker_api.controller;

import ni.edu.uam.michimaker_api.dto.TransformacionDto;
import ni.edu.uam.michimaker_api.dto.TransformacionFeedDto;
import ni.edu.uam.michimaker_api.entity.Transformacion;
import ni.edu.uam.michimaker_api.entity.Usuario;
import ni.edu.uam.michimaker_api.repository.TransformacionRepository;
import ni.edu.uam.michimaker_api.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 🔵 CREATE POST
    @PostMapping
    public ResponseEntity<Transformacion> guardar(@RequestBody TransformacionDto dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Transformacion t = new Transformacion();
        t.setNombreFiltro(dto.getNombreFiltro());
        t.setFecha(dto.getFecha());
        t.setRutaImagen(dto.getRutaImagen());
        t.setUsuario(usuario);

        return ResponseEntity.ok(transformacionRepository.save(t));
    }

    // 🔵 FEED GLOBAL (DTO)
    @GetMapping("/feed")
    public ResponseEntity<List<TransformacionFeedDto>> feed() {

        List<TransformacionFeedDto> feed = transformacionRepository
                .findAllByOrderByIdDesc()
                .stream()
                .map(this::toFeedDto)
                .toList();

        return ResponseEntity.ok(feed);
    }

    // 🔵 FEED POR USUARIO (DTO)
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<TransformacionFeedDto>> porUsuario(@PathVariable Integer id) {

        List<TransformacionFeedDto> lista = transformacionRepository
                .findByUsuarioIdOrderByIdDesc(id)
                .stream()
                .map(this::toFeedDto)
                .toList();

        return ResponseEntity.ok(lista);
    }

    // 🔧 MAPPER CENTRAL (IMPORTANTE)
    private TransformacionFeedDto toFeedDto(Transformacion t) {

        TransformacionFeedDto dto = new TransformacionFeedDto();

        dto.setId(t.getId());
        dto.setNombreFiltro(t.getNombreFiltro());
        dto.setFecha(t.getFecha());
        dto.setRutaImagen(t.getRutaImagen());

        dto.setUsuarioId(t.getUsuario().getId());
        dto.setUsername(t.getUsuario().getUsername());
        dto.setFotoPerfil(t.getUsuario().getFotoPerfil());

        return dto;
    }
}