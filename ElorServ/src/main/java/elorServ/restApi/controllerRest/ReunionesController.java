package elorServ.restApi.controllerRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import elorServ.restApi.dto.ReunionListaDto;
import elorServ.restApi.serviceRest.ReunionesService;

@RestController
@RequestMapping("/api/reuniones")
public class ReunionesController {

    @Autowired
    private ReunionesService reunionesService;

    @GetMapping("/usuario/{id}")
    public List<ReunionListaDto> reunionesUsuario(@PathVariable Integer id) {
        return reunionesService.obtenerReunionesUsuario(id);
    }
}