package elorServ.restApi.controllerRest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import elorServ.modelo.entities.Tipos;
import elorServ.restApi.serviceRest.TiposService;

@RestController
@RequestMapping("/api/tipos")
@CrossOrigin(origins = "*")
public class TiposController {
    
    @Autowired
    private TiposService tiposService;


    /**
     * GET /api/users
     * Obtener todos los users
     */
    @GetMapping
    public ResponseEntity<List<Tipos>> getAllTipos() {
        List<Tipos> tipos = tiposService.findAll();
        return ResponseEntity.ok(tipos);
    }
    
}
