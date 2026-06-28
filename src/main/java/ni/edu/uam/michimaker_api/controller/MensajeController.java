package ni.edu.uam.michimaker_api.controller;

import ni.edu.uam.michimaker_api.dto.MensajeDto;
import ni.edu.uam.michimaker_api.entity.Mensaje;
import ni.edu.uam.michimaker_api.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @Autowired
    private MensajeRepository mensajeRepository;

    // Endpoint 1: Enviar Mensaje
    @PostMapping("/enviar")
    public ResponseEntity<MensajeDto> enviarMensaje(@RequestBody MensajeDto dto) {
        Mensaje mensaje = new Mensaje();
        mensaje.setRemitenteId(dto.getRemitenteId());
        mensaje.setReceptorId(dto.getReceptorId());
        mensaje.setContenido(dto.getContenido());
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setLeido(false);

        Mensaje guardado = mensajeRepository.save(mensaje);
        return ResponseEntity.ok(convertToDto(guardado));
    }

    // Endpoint 2: Cargar Historial inicial
    @GetMapping("/historial")
    public ResponseEntity<List<MensajeDto>> obtenerHistorial(
            @RequestParam Integer user1,
            @RequestParam Integer user2) {

        List<Mensaje> historial = mensajeRepository.findHistorialEntre(user1, user2);
        List<MensajeDto> dtos = historial.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Endpoint 3: EL ENDPOINT DEL POLLING (Android llamará a este repetidamente)
    @GetMapping("/nuevos")
    public ResponseEntity<List<MensajeDto>> obtenerNuevos(
            @RequestParam Integer remitente,
            @RequestParam Integer receptor,
            @RequestParam Long ultimoId) {

        List<Mensaje> nuevos = mensajeRepository.findNuevosMensajes(remitente, receptor, ultimoId);
        List<MensajeDto> dtos = nuevos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Auxiliar para mapear entidad a DTO
    private MensajeDto convertToDto(Mensaje m) {
        MensajeDto dto = new MensajeDto();
        dto.setId(m.getId());
        dto.setRemitenteId(m.getRemitenteId());
        dto.setReceptorId(m.getReceptorId());
        dto.setContenido(m.getContenido());
        dto.setLeido(m.isLeido());
        dto.setFechaEnvio(m.getFechaEnvio().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dto;
    }
}