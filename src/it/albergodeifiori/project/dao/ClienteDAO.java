package it.albergodeifiori.project.dao;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * il DAO (Data Access Object) è un pattern architetturale per la gestione della persistenza: si tratta
 * fondamentalmente di una classe con relativi metodi che rappresenta un'entità tabellare di un DB.
 */

import it.albergodeifiori.project.entity.Cliente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Qui trovo tutte le azioni che svolgerà il cliente.
 */
public interface ClienteDAO {
    //public ArrayList<Cliente> Cliente()throws DAOException;
    /*la roba commentata possiamo prelevarla mediante le altre classi*/
    //public void insertListaProdottiOrdinati()throws DAOException;//dovrebbe stare in ordine
    // public void annullaordine(int a)throws DAOException; //Dovrebbe andare a ordine
    //public String prelevaMenuGiorno()throws DAOException;//Dovrebbe stare in ordine
    public List<Cliente> getAllClienti() throws DAOException;
    public Cliente getCliente(int id) throws DAOException;
    public List<Cliente> getClienti(Cliente a) throws DAOException;
    public void updateContoClienti(Cliente a) throws DAOException;
    public void insertCliente(Cliente a) throws DAOException;
    public void deleteCliente(Cliente a) throws DAOException;
    public void updateClienti(Cliente a) throws DAOException;
    public int getIdCliente() throws DAOException;
    public int getIdCliente(String nDoc) throws DAOException;
    public void deleteClienti(String nDoc) throws DAOException;

}
