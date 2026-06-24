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

        this.transformacionRepository =
                transformacionRepository;

        this.usuarioRepository =
                usuarioRepository;
    }



    // =========================
    // CREATE
    // =========================


    @PostMapping
    public ResponseEntity<TransformacionFeedDto> guardar(
            @RequestBody TransformacionDto dto
    ) {


        System.out.println(
                "USUARIO RECIBIDO: "
                        + dto.getUsuarioId()
        );


        if(dto.getUsuarioId() == null){

            throw new RuntimeException(
                    "UsuarioId es obligatorio"
            );
        }



        Usuario usuario =
                usuarioRepository
                        .findById(
                                dto.getUsuarioId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Usuario no encontrado"
                                )
                        );



        Transformacion t =
                new Transformacion();


        t.setNombreFiltro(
                dto.getNombreFiltro()
        );


        t.setFecha(
                dto.getFecha()
        );


        t.setRutaImagen(
                dto.getRutaImagen()
        );


        t.setLeyenda(
                dto.getLeyenda()
        );


        t.setUsuario(
                usuario
        );



        // =========================
        // IMAGEN BASE64
        // =========================

        if(dto.getImagenBase64() != null
                &&
                !dto.getImagenBase64().isBlank()) {


            try {


                String base64 =
                        limpiarBase64(
                                dto.getImagenBase64()
                        );



                byte[] imagenBytes =
                        Base64.getDecoder()
                                .decode(base64);



                t.setImagen(
                        imagenBytes
                );


            } catch(Exception e){


                System.out.println(
                        "ERROR DECODIFICANDO IMAGEN"
                );


                e.printStackTrace();


                throw new RuntimeException(
                        "Imagen Base64 inválida"
                );
            }
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


        return ResponseEntity.ok(

                transformacionRepository
                        .findAllByOrderByIdDesc()
                        .stream()
                        .map(this::toFeedDto)
                        .toList()

        );
    }





    // =========================
    // FEED USUARIO
    // =========================


    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<TransformacionFeedDto>> usuario(

            @PathVariable Integer id

    ) {

        return ResponseEntity.ok(

                transformacionRepository
                        .findByUsuarioIdOrderByIdDesc(id)
                        .stream()
                        .map(this::toFeedDto)
                        .toList()

        );
    }





    // =========================
    // RANDOM
    // =========================


    @GetMapping("/feed/random")
    public ResponseEntity<List<TransformacionFeedDto>> random() {


        return ResponseEntity.ok(

                transformacionRepository
                        .feedAleatorio()
                        .stream()
                        .map(this::toFeedDto)
                        .toList()

        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarIndividual(
            @PathVariable Integer id
    ) {

        transformacionRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransformacionFeedDto> actualizarLeyenda(
            @PathVariable Integer id,
            @RequestBody TransformacionDto dto
    ) {

        Transformacion transformacion =
                transformacionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transformación no encontrada"
                                )
                        );

        transformacion.setLeyenda(
                dto.getLeyenda()
        );

        transformacionRepository.save(
                transformacion
        );

        return ResponseEntity.ok(
                toFeedDto(transformacion)
        );
    }


    // =========================
    // DELETE
    // =========================


    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id
    ){

        System.out.println(
                "ELIMINANDO HISTORIAL DE USUARIO: " + id
        );

        transformacionRepository
                .deleteByUsuarioId(id);

        System.out.println(
                "ELIMINACION COMPLETADA"
        );

        return ResponseEntity.ok().build();
    }





    // =========================
    // LIMPIAR BASE64
    // =========================


    private String limpiarBase64(
            String base64
    ){

        if(base64.contains(",")){

            return base64.substring(
                    base64.indexOf(",") + 1
            );
        }


        return base64;
    }





    // =========================
    // ENTITY -> DTO
    // =========================


    private TransformacionFeedDto toFeedDto(
            Transformacion t
    ){


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



        if(t.getUsuario()!=null){


            dto.setUsuarioId(
                    t.getUsuario().getId()
            );


            dto.setUsername(
                    t.getUsuario().getUsername()
            );


            dto.setFotoPerfil(
                    t.getUsuario().getFotoPerfil()
            );
        }




        if(t.getImagen()!=null){


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