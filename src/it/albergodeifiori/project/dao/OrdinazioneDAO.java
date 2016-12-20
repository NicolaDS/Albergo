package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.Ordinazione;
import it.albergodeifiori.project.entity.PersonaleRistoro;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Domenico on 08/12/2016.
 */
public interface OrdinazioneDAO {
    /*ritorna un arraylist di ordinazioni dichiarate pronte dal cuoco e quindi in stato di pagare*/
    public ArrayList<Ordinazione> getAllOrdinazioniPagare();
    /*ritorna un arraylist di ordinazioni in stato di preparazione*/
    public ArrayList<Ordinazione> getAllOrdinazioniPreparazione();
    /*ritorna un arraylist di ordinazioni in stato di attesa*/
    public ArrayList<Ordinazione> getAllOrdinazioniAttesa();
    /*Ritorna un ordinazione considerando l'ordine numerico di inserimento(utile al cuoco)*/
    public int getOrdineCronologico();
    /*inserimento dei valori relativi ad un ordine nella tabella Ordinazioni,
     *nella tabella intermedia tra personaleRistoro e ordinazioni
     * e nella tabella intermedia tra ordinazioni e clienti*/
    public void insertOrdineConCamera(Ordinazione ordine, PersonaleRistoro cameriere);
    /*inserimento dei valori relativi ad un ordine nella tabella Ordinazioni,
    * e nella tabella intermedia tra personaleRistoro e ordinazioni*/
    public void insertOrdine(Ordinazione ordine, PersonaleRistoro cameriere);
    //effettuerà un aggiornamento del conto nella tabella Ordinazioni
    public void updateContoOrdine(Ordinazione ordine, double conto);
    //aggiornerà il contoTotale di un cliente nella tabella Cliente
    public void updateContoTotaleCliente (Ordinazione ordine, double conto);
    //cancellerà un ordine dalla tabella ordinazione
    public void deleteOrdine (int idOrdine);
    //
    public int getMaxIdOrdine();
    public ArrayList<Ordinazione> getOrdiniCliente(String nDoc) throws SQLException;
    public ArrayList<Ordinazione> getOrdiniCameriere(int id) throws SQLException;


}
