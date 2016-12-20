package it.albergodeifiori.project.entity;

import it.albergodeifiori.project.dao.*;

import java.util.ArrayList;
import java.util.Date;

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
     * @element-type Room
     */
    public ArrayList<Room> listaCamereRichieste;  //rappresenta una lista delle camere richieste in fase di prenotazione
    public Cliente cliente;                       //manterrà i dati del cliente che sta effettuando una prenotazione



    //Costruttori
    public Prenotazione(Date arrivo, Date partenza, String nome, String cognome, String nDoc, String nascita, double prezzoSoggiorno) throws DAOException {
        /*Instanziare un cliente*/
        cliente = new Cliente(nome, cognome, 0, nDoc, nascita, prezzoSoggiorno);

        PrenotazioneDAO dao = PrenotazioneDAOImp.getIstance();
        idPrenotazione = dao.getIdMaxPrenotazione()+1;
        dataArrivo = arrivo;
        dataPartenza = partenza;
    }

    public Prenotazione( Date arrivo, Date partenza) {

        PrenotazioneDAO dao = PrenotazioneDAOImp.getIstance();
        idPrenotazione = dao.getIdMaxPrenotazione()+1;
        dataArrivo = arrivo;
        dataPartenza = partenza;
    }

    public Prenotazione( Date arrivo, Date partenza, ArrayList<Room> lista) {

        PrenotazioneDAO dao = PrenotazioneDAOImp.getIstance();
        idPrenotazione = dao.getIdMaxPrenotazione()+1;
        dataArrivo = arrivo;
        dataPartenza = partenza;
        listaCamereRichieste=lista;
    }

    /*Metodi*/

    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setIdPrenotazione(int id) {
        idPrenotazione = id;
        return;
    }

    //Metodo get servono per restituire il valore dal campo di interesse
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
    public ArrayList<Room> controllaDisponibilitàCamere(Date arrivo, Date partenza) throws DAOException {
        ArrayList<Room> lista=CameraDaoImpl.getInstance().getCamereLibereDB(arrivo,partenza);

        return lista;
    }

    public void addCamera (Room room){
        listaCamereRichieste.add(room);
    }

    /**
     * quando il cliente confermerà una prenotazione verrà chiamato il sottostante metodo che salverà i dati nel db
     */
    public void insertPrenotazione (Cliente cliente) throws DAOException {
        /*Vado ad inserire questo oggetto nelle tabelle Prenotazione, e gli id di cliente e camera nelle rispettive tabelle
        intermedie
         */
        PrenotazioneDAO dao = PrenotazioneDAOImp.getIstance();
        dao.insertPrenotazione(this, cliente.getIdCliente(), listaCamereRichieste);

    }

    public void prezzoCamereRichieste () throws DAOException {
        double prezzoCamere = 0;  //aggiorno il conto finale del cliente con il prezzo delle camere richieste

        prezzoCamere = listaCamereRichieste.get(0).getPrezzo();
        for (int i = 1; i < listaCamereRichieste.size(); i++) {
            prezzoCamere += listaCamereRichieste.get(i).getPrezzo();
        }
        cliente.setConto(prezzoCamere);

        /*Accesso alla tabella Clienti mediante il dao Cliente per l'aggiornamento del conto finale*/
        ClienteDAO dao = ClienteDaoImpl.getInstance();
        dao.updateContoClienti(cliente);
    }

    public ArrayList<Prenotazione> getPrenotazioni(Date arrivo, Date partenza){
        PrenotazioneDAO dao = PrenotazioneDAOImp.getIstance();

        ArrayList<Prenotazione> lista = new ArrayList<Prenotazione>();
        /*Ottengo tutte le prenotazioni in un certo periodo*/
        lista = dao.cercaPrenotazioni(arrivo,partenza);

        return lista;
    }

    public ArrayList<Prenotazione> getPrenotazioni(Date arrivo){
        PrenotazioneDAO dao = PrenotazioneDAOImp.getIstance();

        ArrayList<Prenotazione> lista = new ArrayList<Prenotazione>();
        /*Ottengo tutte le prenotazioni a partire da una data di arrivo*/
        lista = dao.getPrenotazioni(arrivo);

        return lista;
    }
    public void setContoCliente(double conto){
        cliente.setConto(conto);
    }
}
