package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.PersonaleRistoro;

import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * * il DAO (Data Access Object) è un pattern architetturale per la gestione della persistenza: si tratta
 * fondamentalmente di una classe con relativi metodi che rappresenta un'entità tabellare di un DB.
 */

/**
 * Qui trovo tutte le azioni che svolgerà la classe PersonaleRistoro.
 */
public interface PersonaleRistoroDAO {
    public List<PersonaleRistoro> getAllPersonaleRistoro() throws DAOException;
    public PersonaleRistoro getAddetto(int cod) throws DAOException;
    public List<PersonaleRistoro> getAddetti(PersonaleRistoro a) throws DAOException;
    public void updatePersonaleRistoro(PersonaleRistoro a,int cod) throws DAOException;
    public void insertPersonaleRistoro(PersonaleRistoro a) throws DAOException;
    public void deletePersonaleRistoro(PersonaleRistoro a) throws DAOException;
}
