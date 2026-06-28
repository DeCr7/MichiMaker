package ni.edu.uam.michimaker_api.repository;

import ni.edu.uam.michimaker_api.entity.Transformacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TransformacionRepository extends JpaRepository<Transformacion, Integer> {

    List<Transformacion> findAllByOrderByIdDesc();

    List<Transformacion> findByUsuarioIdOrderByIdDesc(Integer usuarioId);

    void deleteByUsuarioId(Integer usuarioId);

    // Consulta nativa para traer posts aleatorios en el feed (ajustada para PostgreSQL)
    @Query(value = "SELECT * FROM transformaciones ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Transformacion> feedAleatorio();
}