package it.albergodeifiori.project.entity;

import it.albergodeifiori.project.dao.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Domenico on 08/12/2016.
 */
public class Ordinazione {

    final public static String MESS_STATO_NON_ATTESA = "L'ordine non è in stato di attesa!";
    final public static String MESS_STATO_ATTESA = "L'ordine è in stato di attesa!";
    final public static String MESS_STATOERRATO = "Errore, lo stato non è coerente con i tre ammissibili!";
    final public static String[] STATIORDINE = {"attesa", "preparazione", "pagare", "chiuso"};
    final public static String MESS_ERRORE_MENU="nessun menù del giorno";

    /*variabili inerenti alla classe*/
    private int idOrdine;             //valore chiave per le ordinazioni
    private int servizioCamera;   //flag che indica se l'ordine è di tipo "servizio in camera"
    private int camera;               //numero di camera su cui accreditare il conto ->necessario al receptionist
    private int tavolo;               //numero di tavolo su cui accreditare il conto -> necessario al cassiere
    private String statoOrdine;       //indica se lo l'ordine è in fase di "attesa" , "preparazione" oppure che sia "chiuso"
    private double contoOrdine;       //rappresenta la somma dei prezzi dei prodotti ordinati


    /*variabili con  il quale si gestiscono ulteriori classi*/
    public Cliente cliente;
    public PersonaleRistoro cameriere;

    /*Elementi di tipo Prodotto*/
    public ArrayList<Prodotto> listaProdotti;   //rappresenta la lista dei prodotti ordinati da un tavolo, su questa si calcolerà il contoOrdina

    /*Costruttori*/
    public Ordinazione() {
        contoOrdine = 0;
        tavolo = 0;
        camera = 0;

        listaProdotti = new ArrayList<Prodotto>();
        cameriere = new PersonaleRistoro();
    }
    public Ordinazione(int idOrdine,int servizioCamera, int camera, int tavolo, String statoOrdine, double contoOrdine){

        this.idOrdine=idOrdine;
        this.servizioCamera=servizioCamera;
        this.camera=camera;
        this.statoOrdine=statoOrdine;
        this.contoOrdine=contoOrdine;

    }

    /*Qui bisogna passare un valore nullo di camera per chi paga al cassiere*/
    public Ordinazione( int flagSC, int numCam, int numTavolo, String statoOrd, double contoOrd, int idCameriere) {
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();

        idOrdine = dao.getMaxIdOrdine() + 1;
        servizioCamera = flagSC;
        camera = numCam;
        tavolo = numTavolo;
        statoOrdine = new String(statoOrd);
        contoOrdine = contoOrd;

        if (numCam != 0) { //se si specifica il numero di camera bisogna caricare il cliente relativo alla canera
            //effettuo un accesso al db, prelevo le info relative al cliente
            CameraDAO c_dao = CameraDaoImpl.getInstance();
            cliente = c_dao.getClienteDaNumeroCamera(numCam);
        }

        cameriere = new PersonaleRistoro(idCameriere);
        listaProdotti = new ArrayList<Prodotto>();
    }

    /*********METODI DI CLASSE*********/

    /*Ritorna l'ordine da elaborare  ricavandolo dal minimo id associato. */
    public  static int getIdOrdineDaElaborare() {
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
        return dao.getOrdineCronologico();
    }

    //Restituiscono tutti gli ordini nei vari stati di interesse

    public static ArrayList<Ordinazione> getAllOrdiniInAttesa() { //stato di atesa
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
        return dao.getAllOrdinazioniAttesa();
    }

    public static ArrayList<Ordinazione> getAllOrdiniInPreparazione() { //stato di preparazione
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
        return dao.getAllOrdinazioniPreparazione();
    }

    public static ArrayList<Ordinazione> getAllOrdiniInPagamento() { //stato di attesa nel pagamento
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
        return dao.getAllOrdinazioniPagare();
    }

    /*********METODI DI ISTANZA***********/
    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setIdCameriere(int idCameriere) {
        cameriere.setCodDipRist(idCameriere);
    }

    public void setIdOrdine(int id) {
        idOrdine = id;

    }
    //Metodo get servono per restituire il valore dal campo di interesse
    public int getIdOrdine() {
        return idOrdine;
    }

    public void setServizioCamera(int flagSC) {
        servizioCamera = flagSC;
        return;
    }

    public int getServizioCamera() {
        return servizioCamera;
    }

    public void setCamera(int numCam) {
        camera = numCam;
        return;
    }

    public int getCamera() {
        return camera;
    }

    public void setTavolo(int numTavolo) {
        tavolo = numTavolo;
        return;
    }

    public int getTavolo() {
        return tavolo;
    }

    public void setStatoOrdine(String statoOrd) throws ExceptionOrdinazione {
        /*Se lo stato non è uguale almeno ad uno dei tre ammissibili lancio l'eccezione, altrimenti setto statoOrdine*/
        if (statoOrd.equals(STATIORDINE[0]) || statoOrd.equals(STATIORDINE[1])
                || statoOrd.equals(STATIORDINE[2]) || statoOrd.equals(STATIORDINE[3])) {
            statoOrdine = statoOrd;
        } else {
            throw new ExceptionOrdinazione(MESS_STATOERRATO);
        }
        return;
    }

    public String getStatoOrdine() {
        return statoOrdine;
    }

    public void setContoOrdine(double contoOrd) {
        contoOrdine = contoOrd;
    }

    public double getContoOrdine() {
        return contoOrdine;
    }

    /*Aggiunge un prodotto alla lista*/
    public void addProdotto(Prodotto prodotto) {
        listaProdotti.add(prodotto);
        return;
    }


    /*
    * Calcola la somma dei prezzi dei prodotti ordinati aggiornando la variabile conto.
    * Se il cliente ha specificato il pagamento al cassiere allora non effettua l'aggiornamento del contoTotale nel DB,
    * cosa che avverrà per i clienti che non alloggiano nell'albergo
    * */
    public void calcolaConto() throws ExceptionOrdinazione {
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();

        /*se l'ordine è chiuso o in preparazione o in pagare, si può calcolare il conto*/
        if (statoOrdine.equals(STATIORDINE[2]) || statoOrdine.equals(STATIORDINE[1]) || statoOrdine.equals(STATIORDINE[3])) {
            contoOrdine = listaProdotti.get(0).getPrezzo();
            for (int i = 1; i < listaProdotti.size(); i++) {     //somma dei prezzi dei prodotti ordinati
                contoOrdine += listaProdotti.get(i).getPrezzo();
            }
            if (camera != 0) {   //se il conto vuole essere accreditato sulla camera (per cliente alloggiatore)
                //inserisco il conto dell'ordine in Ordinazioni
                dao.updateContoOrdine(this, contoOrdine);
                /*Aggiorno il conto totale del cliente alloggiatore*/
                somContoClienteAlbergo();
            } else {
                //inserisco il conto dell'ordine in Ordinazioni
                dao.updateContoOrdine(this, contoOrdine);
            }
        } else {    //se l'ordine è in stato di attesa lancio un'eccezione
            throw new ExceptionOrdinazione(MESS_STATO_ATTESA);
        }
    }

    /*
    * Poichè questa è utilizzata per i clienti che soggiornano nell'albergo, verrà aggiornato il contoTotale del cliente
    * effettuando un read del contoTotale dalla tabella Clienti su cui verrà sommato il ContoOrdine calcolato.
    * Il contoTotale aggiornato verrà inserito nel DB.
    **/
    public void somContoClienteAlbergo() {
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
        dao.updateContoTotaleCliente(this, contoOrdine);
    }

    /*Diamo la possibilità di annullare l'ordine solo se esso è in fase di attesa*/
    public static void annullaOrdine(int idOrdine, String stato) throws ExceptionOrdinazione {
        if (stato.equals(STATIORDINE[0])) {
            /*Se l'ordine è in stato di attesa, lo elimino dalla tabella ordinazioni*/
            OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
            dao.deleteOrdine(idOrdine);
        } else {
            throw new ExceptionOrdinazione(MESS_STATO_NON_ATTESA);
        }
    }

    public void insertOrdineDB() {
       /*inserisco i dati dell'ordine e del cameriere nelle rispettive tabelle*/
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();

        if (camera != 0) {
            /*devo inserire anche l'id del cliente*/
            dao.insertOrdineConCamera(this, cameriere);
        } else {
            /*non inserisco l'id del cliente*/
            dao.insertOrdine(this, cameriere);
        }
    }

    /*Preleva menu del giorno */
    public static   String prelevaMenuGiorno() throws Exception {
//restituisce un int relativo alla posizione del giorno della settimana
        String nomiProdotti = null;
        int ngiorno = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String giorno = null;

//uso tale costrutto che va a sostituire molto if else
        switch (ngiorno) {
            case 1:
                giorno = "domenica";
                break;
            case 2:
                giorno = "lunedì";
                break;
            case 3:
                giorno = "martedì";
                break;
            case 4:
                giorno = "mercoledì";
                break;
            case 5:
                giorno = "giovedì";
                break;
            case 6:
                giorno = "venerdì";
                break;
            case 7:
                giorno = "sabato";
                break;
        }//non utilizzo default poichè DAY_OF_WEEK è un int che va da 1 a 7
        //Prodotto chiavediricerca = new Prodotto(giorno);//creare un nuovo costruttore e un nuovo getProdotti eventualmente
        ArrayList<Prodotto> menuGiorno = ProdottoDaoImpl.getInstance().getProdotti(giorno);

        if (menuGiorno.isEmpty()) {
            throw new Exception(MESS_ERRORE_MENU);
        }

        nomiProdotti=menuGiorno.get(0).getProdotto()+","+menuGiorno.get(0).getPrezzo()+"\n";

        for (int i=1; i<menuGiorno.size(); i++){
            nomiProdotti+=menuGiorno.get(i).getProdotto()+","+menuGiorno.get(i).getPrezzo()+"\n";//controllare se funziona!
        }
        return nomiProdotti;
    }

    //Restituisce gli ordine di un cliente dato il numero di deocumento di quest'ultimo
    public static String getOrdiniCliente (String nDoc) throws Exception {
        ArrayList<Ordinazione> ordini=new ArrayList<Ordinazione>();
        OrdinazioneDAO dao= OrdinazioneDAOImp.getIstance();
        ordini=dao.getOrdiniCliente(nDoc);

        if (ordini.isEmpty()) {
            throw new Exception("Nessun ordine effettuato");
        }
        String ord = null;
        ord="id: "+ordini.get(0).getIdOrdine()+" Camera:"+ordini.get(0).getCamera()+" Stato:"+ordini.get(0).getStatoOrdine()+" Conto:"+ordini.get(0).getContoOrdine()+"\n";
        for (int i=1; i<ordini.size(); i++){
            ord+="id: "+ordini.get(i).getIdOrdine()+" Camera:"+ordini.get(i).getCamera()+" Stato:"+ordini.get(i).getStatoOrdine()+" Conto:"+ordini.get(i).getContoOrdine()+"\n";;
        }
        return ord;
    }
    //Restituisce gli ordini assegnati a un cameriere identificato da un idCameriere
    public static String getOrdiniCameriere(int id) throws Exception {
        ArrayList<Ordinazione> ordini = new ArrayList<Ordinazione>();
        OrdinazioneDAO dao = OrdinazioneDAOImp.getIstance();
        ordini = dao.getOrdiniCameriere(id);

        if (ordini.isEmpty()) {
            throw new Exception("Nessun ordine effettuato");
        }

        String ord = null;
        ord = "id:" + ordini.get(0).getIdOrdine() + " Camera:" + ordini.get(0).getCamera() +
                " ServizioCamera:" + ordini.get(0).getServizioCamera() + " Stato:" + ordini.get(0).getStatoOrdine() +
                " Conto:" + ordini.get(0).getContoOrdine() + "\n";
        for (int i = 1; i < ordini.size(); i++) {
            ord += "id:" + ordini.get(i).getIdOrdine() + " Camera:" + ordini.get(i).getCamera() +
                    " ServizioCamera:" + ordini.get(0).getServizioCamera() +
                    " Stato:" + ordini.get(i).getStatoOrdine() + " Conto:" + ordini.get(i).getContoOrdine() + "\n";
            ;
        }
        return ord;
    }
}

