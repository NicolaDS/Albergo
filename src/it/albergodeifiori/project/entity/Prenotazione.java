package it.albergodeifiori.project.entity;

/**
 * Created by Domenico on 08/12/2016.
 */
public class Prenotazione {
    /*variabili inerenti alla classe*/
    private int idPrenotazione;    //è un valore chiave per le prenotazioni
    private Date dataArrivo;       //rappresenta la data di arrivvo all'albergo da parte di un alloggiatore
    private Date dataPartenza;     //rapppresenta la data di uscita dall'albergo da parte di un alloggiatore
    /**
     *
     * @element-type Camera
     */
    public ArrayList<Camera> listaCamereRichieste;  //rappresenta una lista delle camere richieste in fase di prenotazione
    public Cliente cliente;                       //manterrà i dati del cliente che sta effettuando una prenotazione

    public void Prenotazione() {
    }

    public void Prenotazione(int id, Date arrivo, Date partenza) {
        idPrenotazione = id;
        dataArrivo = arrivo;
        dataPartenza = partenza;
    }

    /*Metodi*/
    public void datiCliente (int id, String nome, String cognome, String nDoc, boolean sogg, Date dataNascita, double contoFin){
        cliente = new Cliente (id, nome, cognome, nDoc, sogg, dataNascita,  contoFin);
    }

    public void setIdPrenotazione(int id) {
        idPrenotazione = id;
        return;
    }

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setDataArrivo(Date arrivo) {
        dataArrivo = arrivo;
        return;
    }

    public Date getDateArrivo() {
        return dataArrivo;
    }

    public void setDataPartenza(Date partenza) {
        dataPartenza = partenza;
        return;
    }

    public Date getDatePartenza() {
        return dataPartenza;
    }

    /*
    *Bisogna poter controllare la disponibilità delle camere in un determinato periodo.
    *Faccio un join tra le prenotazioni in un dato periodo e le camere richieste, dalle camere sottraggo le righe ricavate
    * precedentemente.
    * */
    public ArrayList<Camera> controllaDisponibilitàCamere() {

    }

    public void addCamera (Camera camera){
        listaCamereRichieste.add(camera);
        //vado ad   inserire nel db che una tipologia di camera è stata occupata in un dato periodo


    }

    /**
     * quando il cliente confermerà una prenotazione verrà chiamato il sottostante metodo che salverà i dati nel db
     */
    public void insertPrenotazione (){
        /*Vado ad inserire questo oggetto nelle tabelle Prenotazione e l'oggetto cliente nella tab Cliente*/
    }

    public void prezzoCamereRichieste () {
        double prezzoCamere = 0;  //aggiorno il conto finale del cliente con il prezzo delle camere richieste

        prezzoCamere = listaCamereRichieste.get(0).getPrezzoCamera();
        for (int i = 1; i < listaCamereRichieste.size(); i++) {
            prezzoCamere += listaCamereRichieste.get(i).getPrezzoCamera();
        }
    }
}
