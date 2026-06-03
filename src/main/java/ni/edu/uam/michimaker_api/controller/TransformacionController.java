package ni.edu.uam.michimaker_api.controller;

import ni.edu.uam.michimaker_api.dto.TransformacionDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transformaciones")
@CrossOrigin("*")
public class TransformacionController {

    private final List<TransformacionDto> historial =
            new ArrayList<>();

    @PostMapping
    public TransformacionDto guardar(
            @RequestBody TransformacionDto dto
    ) {

        historial.add(dto);

        System.out.println(
                "Transformación recibida: "
                        + dto.getNombreFiltro()
        );

        return dto;
    }

    @GetMapping
    public List<TransformacionDto> obtenerTodas() {

        return historial;
    }
}