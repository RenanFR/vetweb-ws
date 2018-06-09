package com.vetweb.dao;
 //@author renanrodrigues
import java.util.List;

public interface IDAO<E> {
	
    public void salvar(E e);
    
    public List<E> listar();
    
    public E consultarPorId(long id);
    
    public void remover(E e);
    
    public E consultarPorNome(String nome);
    
    public long quantidadeRegistros();
    
}
