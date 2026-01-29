package elorServ.restApi.dto;

public record HorariosDto(
	    String dia,
	    Integer hora,
	    String tipo,
	    Integer curso,
	    String ciclo,
	    String modulo,
	    String aula,
	    String observaciones
	) {}
