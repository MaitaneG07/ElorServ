package modelo.dao;

import java.util.List;

public abstract class DaoAbstract <I> {


	public abstract List <I> getAll ();
	
	public abstract I get (int id);
	
	public abstract void insert (I i);
	
	public abstract void updateDetached (I i);
	
	public abstract void updatePersistent(int id, String name);
	
	public abstract void deleteDetached (I i);
	
	public abstract void deletePersistent(int id);
}
