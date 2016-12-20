package it.albergodeifiori.project.dao;

        import it.albergodeifiori.project.entity.Cliente;
        import it.albergodeifiori.project.entity.Prenotazione;
        import it.albergodeifiori.project.entity.Room;

        import java.util.ArrayList;
        import java.util.Date;

/**
 * Created by Domenico on 09/12/2016.
 */
public interface PrenotazioneDAO {
    /*permette l'inserimento di una singola prenotazione in prenotazioni*/
    public void insertPrenotazione(Prenotazione p, int idCliente, ArrayList<Room> camera);
    /*Ritorna un elenco delle prenotazioni in un dato periodo*/
    public ArrayList<Prenotazione> cercaPrenotazioni(Date arrivo, Date partenza);
    /*Ritorna tutte le prenotazioni fatte a partire da una data di arrivo*/
    public ArrayList<Prenotazione> getPrenotazioni(Date arrivo);

    public int getIdMaxPrenotazione();
}
