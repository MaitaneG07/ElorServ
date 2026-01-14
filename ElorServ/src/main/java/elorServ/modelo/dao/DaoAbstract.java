package elorServ.modelo.dao;

import java.util.List;

public abstract class DaoAbstract <I> {


	public abstract List <I> selectAll ();
	
	public abstract I selectById (int id);
	
	public abstract void insert (I i);
	
	public abstract void update (I i);
	
	public abstract void delete (I i);
	
}
