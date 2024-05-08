package edu.esprit.user.services;

import java.util.List;

public interface IService<T> {
    public void ajout(T t);
    public void modifier(T t);
    public void supprimer(int id);
    public T recherche(int id);
    public List<T> liste();
}
