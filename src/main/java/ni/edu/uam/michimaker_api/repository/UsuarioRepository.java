package ni.edu.uam.michimaker_api.repository;

import ni.edu.uam.michimaker_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    Usuario findByUsername(String username);

    Usuario findByCorreo(String correo);
}