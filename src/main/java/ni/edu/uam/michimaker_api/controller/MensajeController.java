package ni.edu.uam.michimaker_api.controller;

import ni.edu.uam.michimaker_api.dto.ChatPreviewDto;
import ni.edu.uam.michimaker_api.dto.MensajeDto;
import ni.edu.uam.michimaker_api.entity.Mensaje;
import ni.edu.uam.michimaker_api.repository.MensajeRepository;
import ni.edu.uam.michimaker_api.repository.UsuarioRepository; // Asegúrate de tener este import
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

    @Autowired
    private UsuarioRepository usuarioRepository; // Re-introducido para buscar nombres/fotos de forma segura

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

    // Endpoint 3: EL ENDPOINT DEL POLLING
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

    @GetMapping("/bandeja/{userId}")
    public ResponseEntity<List<ChatPreviewDto>> obtenerBandejaEntrada(@PathVariable Integer userId) {
        List<Mensaje> ultimosMensajes = mensajeRepository.findUltimosMensajesPorUsuario(userId);

        List<ChatPreviewDto> bandeja = ultimosMensajes.stream().map(m -> {
            // Evaluamos dinámicamente quién es el OTRO usuario en la conversación
            Integer otroId = m.getRemitenteId().equals(userId) ? m.getReceptorId() : m.getRemitenteId();
            var otroUsuario = usuarioRepository.findById(otroId).orElse(null);

            ChatPreviewDto dto = new ChatPreviewDto();
            dto.setUsuarioId(otroId);
            dto.setUltimoMensaje(m.getContenido());
            dto.setLeido(m.isLeido());

            if (m.getFechaEnvio() != null) {
                dto.setFechaEnvio(m.getFechaEnvio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                dto.setFechaEnvio("");
            }

            if (otroUsuario != null) {
                dto.setUsername(otroUsuario.getUsername());
                dto.setFotoPerfil(otroUsuario.getFotoPerfil()); // Base64 directo de la entidad
            } else {
                dto.setUsername("Usuario Eliminado");
                dto.setFotoPerfil(null);
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(bandeja);
    }

    // Auxiliar para mapear entidad a DTO
    private MensajeDto convertToDto(Mensaje m) {
        if (m == null) return null;

        MensajeDto dto = new MensajeDto();
        dto.setId(m.getId());
        dto.setRemitenteId(m.getRemitenteId());
        dto.setReceptorId(m.getReceptorId());
        dto.setContenido(m.getContenido());
        dto.setLeido(m.isLeido());
        if (m.getFechaEnvio() != null) {
            dto.setFechaEnvio(m.getFechaEnvio().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        return dto;
    }
}