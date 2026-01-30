package elorServ.restApi.dto;

import java.util.List;

public record HorarioProfesorDto(
    Integer profesorId,
    String profesorNombre,
    List<HorariosDto> slots
) {}
