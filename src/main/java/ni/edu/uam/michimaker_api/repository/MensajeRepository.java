package ni.edu.uam.michimaker_api.repository;

import ni.edu.uam.michimaker_api.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // 1. Obtener el historial completo entre dos usuarios
    @Query("SELECT m FROM Mensaje m WHERE " +
            "(m.remitenteId = :user1 AND m.receptorId = :user2) OR " +
            "(m.remitenteId = :user2 AND m.receptorId = :user1) " +
            "ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findHistorialEntre(
            @Param("user1") Integer user1,
            @Param("user2") Integer user2
    );

    // 2. POLING: Obtener solo los mensajes nuevos que Android no ha visto (filtrando por el último ID recibido)
    @Query("SELECT m FROM Mensaje m WHERE " +
            "((m.remitenteId = :remitente AND m.receptorId = :receptor) OR " +
            "(m.remitenteId = :receptor AND m.receptorId = :remitente)) " +
            "AND m.id > :ultimoId " +
            "ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findNuevosMensajes(
            @Param("remitente") Integer remitente,
            @Param("receptor") Integer receptor,
            @Param("ultimoId") Long ultimoId
    );
}