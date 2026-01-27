package elorServ.restApi.controllerRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import elorServ.restApi.serviceRest.CiclosService;

@RestController
@RequestMapping("/api/ciclos")
@CrossOrigin(origins = "*")
public class CiclosController {
    
    @Autowired
    private CiclosService ciclosService;

}
