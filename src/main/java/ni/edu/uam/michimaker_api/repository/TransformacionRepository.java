package ni.edu.uam.michimaker_api.repository;

import ni.edu.uam.michimaker_api.entity.Transformacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransformacionRepository
        extends JpaRepository<Transformacion, Integer> {

    List<Transformacion> findAllByOrderByIdDesc();

    List<Transformacion> findByUsuarioIdOrderByIdDesc(Integer usuarioId);
}