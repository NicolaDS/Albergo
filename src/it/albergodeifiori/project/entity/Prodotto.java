package it.albergodeifiori.project.entity;

import it.albergodeifiori.project.dao.DAOException;
import it.albergodeifiori.project.dao.OrdinazioneDAOImp;
import it.albergodeifiori.project.dao.ProdottoDAO;
import it.albergodeifiori.project.dao.ProdottoDaoImpl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by utente on 09/12/2016.
 */

public class Prodotto {

    // ATTRIBUTI
    private int codProdotto;
    private String prodotto;
    private int ordineBar;
    private double prezzo;
    private String giorno;


    // RELAZIONE TRA Prodotto E Ordinazione
    public ArrayList<Ordinazione> selezionare;

    // METODI

    // COSTRUTTORE PARAMETRIZZATO. SOVRASCRIVE IL COSTRUTTORE DI DEFAULT
    public  Prodotto() {
        this.codProdotto = 0;
        this.prodotto = null;
        this.prezzo = 0.0;
        this.giorno = null;
    }
    public  Prodotto(int id, String nome) {
        this.codProdotto = id;
        this.prodotto = nome;
        this.prezzo = 0.0;
        this.giorno = null;
    }


    public  Prodotto(int codProdotto, String prodotto, int ordineBar, double prezzo, String giorno) {
        this.codProdotto = codProdotto;
        this.prodotto = prodotto;
        this.ordineBar = ordineBar;
        this.prezzo = prezzo;
        this.giorno = giorno;
    }

    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setCodProdotto(int codProdotto) {
        this.codProdotto = codProdotto;  // da valore (setta, imposta) il codProdotto
    }

    //Metodo get servono per restituire il valore dal campo di interesse
    public int getCodProdotto() {
        return codProdotto;   // restituisce il codProdotto cercato
    }

    public void setProdotto(String prodotto) {
        this.prodotto = prodotto;
    }

    public String getProdotto() {
        return prodotto;
    }

    public int getOrdineBar() {
        return ordineBar;       // se l'ordine è relativo al bar restituisce true
    }

    public void setOrdineBar(int ordineBar) {
        this.ordineBar = ordineBar;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getGiorno() {
        return giorno;
    }

    // funzione per selezionare i prodotti del giorno -> Menù del giorno
    public static ArrayList<Prodotto> menuGiorno(String giorno) throws DAOException {

        ArrayList<Prodotto> listaProdotti = new ArrayList<Prodotto>();

        //istanzio oggetto DAO che eseguirà la seguente query:
        // SELECT * FROM Prodotto WHERE giorno = giorno
        ProdottoDAO dao = ProdottoDaoImpl.getInstance();
        // dalla query ottengo la lista di prodotti relativi al menu del giorno
        // e la memorizzo in listaProdotti
        listaProdotti = dao.getProdotti(giorno);

        return listaProdotti;
    }

    // restituisce tutti i prodotti presenti nella tabella
    public static ArrayList<Prodotto> getAllProdotti () throws DAOException {

        ArrayList<Prodotto> listaProdotti;

        //istanzio oggetto DAO che eseguirà la seguente query:
        // SELECT * FROM Prodotto;
        ProdottoDAO dao = ProdottoDaoImpl.getInstance();

        // dalla query ottengo la lista di prodotti
        // e la memorizzo in listaProdotti
        listaProdotti = dao.getAllProdotto();

        return listaProdotti;
    }

    //Restituisce tutti i prodotti del bar
    public static ArrayList<Prodotto> getAllProdottiBar() throws DAOException{
        ArrayList<Prodotto> listaBar;
        listaBar=ProdottoDaoImpl.getInstance().getProdottoBar();
        return listaBar;

    }

    //Restituisce tutti i prodotti del ristorante
    public static ArrayList<Prodotto> getAllProdottiRist() throws DAOException{
        ArrayList<Prodotto> listaRist;
        listaRist=ProdottoDaoImpl.getInstance().getProdottoRist();
        return listaRist;

    }
    //restituisce gli idProdoti da un array di nomi prodotti
    public static ArrayList<Integer> getIdProdotti(String[] prodotto) throws DAOException {
        ProdottoDAO dao= ProdottoDaoImpl.getInstance();
        ArrayList<Integer> id=new ArrayList<Integer>();
        for (int i=0; i<prodotto.length; i++){
            id.add(dao.getIdProdottoDB(prodotto[i]));
        }
        return id;
    }
    //restituisce una stringa di prodotti da un idOrdinazione
    public static String getProdottiOrd(int idOrdinazione) throws Exception {
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
        ProdottoDAO dao = ProdottoDaoImpl.getInstance();
        prodotti = dao.getProdottiOrd(idOrdinazione);

        if (prodotti.isEmpty()) {
            throw new Exception("Nessun ordine effettuato");
        }

        String prod = null;
        prod = " Nome:" + prodotti.get(0).getProdotto() + "\n";
        for (int i = 1; i < prodotti.size(); i++) {
            prod += " Nome:" + prodotti.get(i).getProdotto() + "\n";
        }
        return prod;
    }
}