package it.albergodeifiori.project.entity;

/**
 * Created by Domenico on 08/12/2016.
 */
public class Ordinazione {
    final public static String MESS_STATO_ATTESA = "L'ordine è in stato di attesa!";
    final public static String MESS_STATOERRATO = "Errore, lo stato non è coerente con i tre ammissibili!";
    final public static String[] STATIORDINE = {"attesa", "preparazione", "chiuso"}  ;

    /*variabili inerenti alla classe*/
    private int idOrdine;             //valore chiave per le ordinazioni
    private boolean servizioCamera;   //flag che indica se l'ordine è di tipo "servizio in camera"
    private int camera;               //numero di camera su cui accreditare il conto ->necessario al receptionist
    private int tavolo;               //numero di tavolo su cui accreditare il conto -> necessario al cassiere
    private String statoOrdine;       //indica se lo l'ordine è in fase di "attesa" , "preparazione" oppure che sia "chiuso"
    private double contoOrdine;       //rappresenta la somma dei prezzi dei prodotti ordinati

    /*variabili con  il quale si gestiscono ulteriori classi*/
    public Cliente Effettua;
    public PersonaleRistoro cameriere;

    /*Elementi di tipo Prodotto*/
    public ArrayList<Prodotto> listaProdotti;   //rappresenta la lista dei prodotti ordinati da un tavolo, su questa si calcolerà il contoOrdina

    /*Costruttori*/
    public Ordinazione() {
        contoOrdine = 0;
        tavolo = 0;
        camera = 0;

        listaProdotti = new ArrayList<Prodotto>();
        cameriere = new PersonaleRistoro ();
    }

    /*Qui bisogna passare un valore nullo di camera per chi paga al cassiere*/
    public Ordinazione(int id,  boolean flagSC,  int numCam,  int numTavolo,  String statoOrd,  double contoOrd, int idCameriere) {
        idOrdine = id;
        servizioCamera = flagSC;
        camera = numCam;
        tavolo = numTavolo;
        statoOrdine = new String(statoOrd);
        contoOrdine = contoOrd;

        if (numCam != 0){ //se si specifica il numero di camera bisogna caricare il cliente relativo alla canera
            //effettuo un accesso al db, prelevo le info relative al cliente

        }

        cameriere = new PersonaleRistoro (idCameriere);

        listaProdotti = new ArrayList<Prodotto>();
    }

    /*METODI*/
    public void serIdCameriere (int idCamerire){
        cameriere.setId (idCameriere);
        return;
    }

    public void setIdOrdine(int id) {
        idOrdine = id;
        return;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setServizioCamera(boolean flagSC) {
        servizioCamera = flagSC;
        return;
    }

    public boolean getServizioCamera() {
        return servizioCamera;
    }

    public void setCamera(int numCam) {
        camera = numCam;
        return;
    }

    public int getCamera() { return camera; }

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
                || statoOrd.equals(STATIORDINE[2])){
            statoOrdine = statoOrd;
        }else{
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
    public void addProdotto(Prodotto prodotto){
        listaProdotti.add(prodotto);
        return;
    }


    /*
    * Calcola la somma dei prezzi dei prodotti ordinati aggiornando la variabile conto.
    * Se il cliente ha specificato il pagamento al cassiere allora non effettua l'aggiornamento del contoTotale nel DB,
    * cosa che avverrà per i clienti che non alloggiano nell'albergo
    * */
    public void calcolaConto() throws ExceptionOrdinazione {
        /*se l'ordine è chiuso o in preparazione, si può calcolare il conto*/
        if (statoOrdine.equals(STATIORDINE[2]) || statoOrdine.equals(STATIORDINE[1])){
            contoOrdine = listaProdotti.get(0).getPrezzo();
            for (int i=1; i<listaProdotti.size(); i++){     //somma dei prezzi dei prodotti ordinati
                contoOrdine += listaProdotti.get(i).getPrezzo();
            }

            if (camera != 0){   //se il conto vuole essere accreditato sulla camera (per cliente alloggiatore)
                somContoClienteAlbergo();
                //inserisco il conto dell'ordine in Ordinazioni
            }else {
                //inserisco il conto dell'ordine in Ordinazioni
            }
        }else{    //se l'ordine è in stato di attesa lancio un eccezione
            throw new ExceptionOrdinazione(MESS_STATO_ATTESA);
        }
    }



    /*
    * Poichè questa è utilizzata per i clienti che soggiornano nell'albergo, verrà aggiornato il contoTotale del cliente
    * effettuando un read del contoTotale dalla tabella Clienti su cui verrà sommato il ContoOrdine calcolato.
    * Il contoTotale aggiornato verrà inserito nel DB.
    **/
    public void somContoClienteAlbergo() {
        /**/

    }

    public void insertOrdineDB(){
       /*inserisco i dati dell'ordine e del cameriere nelle rispettive tabelle*/
    }
}
