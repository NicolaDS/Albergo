package it.albergodeifiori.project.entity;

import it.albergodeifiori.project.dao.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nicola on 06/12/2016.
 * Vado a creare la classe Cliente che andremo ad usare nel programma
 */
public class Cliente {

    /**
     * Vado ad inizializzare tuttii gli elementi che contenuti nella nostra classe.
     */
    private int idCliente  = 0;
    private String nome;
    private String cognome ;
    private int soggiorno ;
    private String numDoc;
    private String dataNascita ;
    double conto;

    /**
     * Si crea il costruttore.
     */
    public Cliente (String nome, String congome, int soggiorno, String numDoc, String dataNascita, double conto) throws DAOException {
        ClienteDAO dao = ClienteDaoImpl.getInstance();
        idCliente = dao.getIdCliente()+1;
        this.nome = nome;
        this.cognome = congome;
        this.soggiorno = soggiorno;
        this.numDoc = numDoc;
        this.dataNascita = dataNascita;
        this.conto = conto;
    }

    /**
     * Vado ad implementare i metodi get e set che sono importantissimi per l' utilizzo della classe, poichè mi
     * gli elementi che compongono la nostra istanza.
     */

    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setId(int id){
        idCliente = id;
    }

    //Metodo get servono per restituire il valore dal campo di interesse
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public int getSoggiorno() {
        return soggiorno;
    }

    public void setSoggiorno(int soggiorno) {
        this.soggiorno = soggiorno;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public double getConto() {
        return conto;
    }

    public void setConto(double conto) {
        this.conto = conto;
    }

    //Metodo che permette di effettuare l' inserimento del cliente nel DB
    public void inserisciClienteDB(){
        //Rcihiamiamo l'istanza tenendo conto della classe singleton
        ClienteDAO dao = ClienteDaoImpl.getInstance();

        try {
            dao.insertCliente(this);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    //REsitituisce l'id del cliente
    public int getIdCliente() throws DAOException{
        int id = 0;
        if (idCliente != 0){
            id = idCliente;
        }else{
            throw new DAOException("Nessun cliente");
        }
        return id;
    }

    /*METODI DI CLASSE*/
    //Ci da tutti i clienti dell'albergo
    public static List<Cliente> getAllClienti(){
        List<Cliente> lista = null;
        ClienteDAO dao = ClienteDaoImpl.getInstance();

        try {
            lista = dao.getAllClienti();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return lista;
    }
    //Cancella un cliente passando come parametro il Documento di identità corrispondente
    public static void deleteCliente(String numDoc){
        ClienteDAO dao = ClienteDaoImpl.getInstance();
        try {
            dao.deleteClienti(numDoc);
        } catch (DAOException e) {
            e.printStackTrace();
        }

    }


}
