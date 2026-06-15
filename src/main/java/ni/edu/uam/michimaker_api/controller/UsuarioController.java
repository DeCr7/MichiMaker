package ni.edu.uam.michimaker_api.controller;

import ni.edu.uam.michimaker_api.dto.UsuarioDto;
import ni.edu.uam.michimaker_api.entity.Usuario;
import ni.edu.uam.michimaker_api.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(
            UsuarioRepository repository
    ) {
        this.repository = repository;
    }

    @PostMapping
    public Usuario registrar(
            @RequestBody UsuarioDto dto
    ) {

        Usuario usuario = new Usuario();

        usuario.setUsername(
                dto.getUsername()
        );

        usuario.setNombre(
                dto.getNombre()
        );

        usuario.setCorreo(
                dto.getCorreo()
        );

        usuario.setPassword(
                dto.getPassword()
        );

        usuario.setFotoPerfil(
                dto.getFotoPerfil()
        );

        return repository.save(
                usuario
        );
    }

    @GetMapping
    public List<Usuario> obtenerTodos() {

        return repository.findAll();
    }
}
