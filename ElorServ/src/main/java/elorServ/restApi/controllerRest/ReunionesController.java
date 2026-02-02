package elorServ.restApi.controllerRest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import elorServ.restApi.dto.ReunionListaDto;
import elorServ.restApi.serviceRest.ReunionesService;

@RestController
@RequestMapping("/api/reuniones")
@CrossOrigin
public class ReunionesController {

    @Autowired
    private ReunionesService reunionesService;

    @GetMapping("/usuario/{id}")
    public List<ReunionListaDto> reunionesUsuario(@PathVariable Integer id) {
        return reunionesService.obtenerReunionesUsuario(id);
    }

    /**
     * Body: { "profesorId": 3, "estado": "aceptada" }  o  "denegada"
     */
    @PatchMapping("/{reunionId}/estado")
    public Map<String, Object> cambiarEstado(
            @PathVariable Integer reunionId,
            @RequestBody Map<String, Object> body) {

        Integer profesorId = ((Number) body.get("profesorId")).intValue();
        String estado = (String) body.get("estado");

        reunionesService.cambiarEstado(reunionId, profesorId, estado);

        return Map.of("ok", true, "idReunion", reunionId, "estado", estado);
    }
}