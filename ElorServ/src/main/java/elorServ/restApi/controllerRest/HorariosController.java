package elorServ.restApi.controllerRest;

import org.springframework.web.bind.annotation.*;

import elorServ.restApi.dto.HorarioProfesorDto;
import elorServ.restApi.serviceRest.HorariosService;

@RestController
@RequestMapping("/api/horarios")
public class HorariosController {

    private final HorariosService horariosService;

    public HorariosController(HorariosService horariosService) {
        this.horariosService = horariosService;
    }

    @GetMapping("/profesor/{id}")
    public HorarioProfesorDto getHorarioProfesor(@PathVariable("id") Integer profesorId) {
        return horariosService.obtenerHorarioProfesor(profesorId);
    }
    
    @GetMapping("/alumno/{id}")
    public HorarioProfesorDto getHorarioAlumno(@PathVariable Integer id) {
        return horariosService.obtenerHorarioAlumno(id);
    }
}
