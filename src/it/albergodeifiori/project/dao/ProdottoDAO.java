package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.Prodotto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * * il DAO (Data Access Object) è un pattern architetturale per la gestione della persistenza: si tratta
 * fondamentalmente di una classe con relativi metodi che rappresenta un'entità tabellare di un DB.
 */
public interface ProdottoDAO {
    public ArrayList<Prodotto> getAllProdotto() throws DAOException;
    public Prodotto getProdotto(int cod) throws DAOException;
    public List<Prodotto> getProdotti(Prodotto a) throws DAOException;
    public ArrayList<Prodotto> getProdotti(String giorno) throws DAOException;
    public void updateProdotto(Prodotto a,int cod) throws DAOException;
    public void insertProdotto(Prodotto a) throws DAOException;
    public void deleteProdotto(Prodotto a) throws DAOException;
    public ArrayList<Prodotto> getProdottoBar() throws DAOException;
    public ArrayList<Prodotto> getProdottoRist() throws DAOException;
    public int getIdProdottoDB(String prodotto) throws DAOException;
    //ricerca i prodotti dell'ordinazione
    public ArrayList<Prodotto> getProdottiOrd(int idOrdinazione) throws DAOException;
}
