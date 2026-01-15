package elorServ.modelo.dao;

import java.util.List;

import elorServ.modelo.exception.ElorException;

public interface DaoInterface <I>{

	public List <I> selectAll () throws ElorException;
	
	public I selectById (int id);
	
	public void insert (I i);
	
	public void update (I i);
	
	public void delete(I i);
	
	
	
}
