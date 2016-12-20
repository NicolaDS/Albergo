package it.albergodeifiori.project.dao;


import it.albergodeifiori.project.entity.Prenotazione;
import it.albergodeifiori.project.entity.Room;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Domenico on 09/12/2016.
 */
public class PrenotazioneDAOImp implements PrenotazioneDAO {

    /*Costruttore vuoto*/
    private PrenotazioneDAOImp(){};    //deve essere privato poichè istanzierà un oggetto all'interno di una singleton

    private static PrenotazioneDAO dao = null;

    /*Singleton: fa in modo che venga realizzata una sola istanza dell'interfaccia*/
    public static PrenotazioneDAO getIstance(){
        if (dao == null){
            dao = new PrenotazioneDAOImp();
        }
        return dao;
    }

    /*Effettuerà l'inserimento della prenotazione in Prenotazioni. Inoltre collegherà la prenotazione ad un cliente
    (inserendo nella tabella intermedia l'id del cliente che ha effettuato la prenotazione) ed inserirà la lista delle
    camere prenotate nella tabella intermedia tra Prenotazioni e Room (lo si fa inserendo i rispettivi id ovviamente).*/
    @Override
    public void insertPrenotazione(Prenotazione p, int idCliente, ArrayList<Room> camera) {
        String queryInsertPrenotazione = "INSERT INTO prenotazioni" +
                "(data_arrivo,data_partenza) VALUES" +
                "('"+p.getDateArrivo()+"','"+p.getDatePartenza()+"');";

        String queryInsertClienteCheHaPrenotato = "INSERT INTO cliente_has_prenotazioni" +
                "(cliente_idcliente,prenotazioni_idprenotazioni)VALUES" +
                "('"+idCliente+"','"+p.getIdPrenotazione()+"');";


        try {
            Statement st = DAOSettings.getStatement();
            st.executeUpdate(queryInsertPrenotazione);
            st.executeUpdate(queryInsertClienteCheHaPrenotato);
            for (int i=0;i<camera.size();i++){
                String queryInsertCameraPrenotata = "INSERT INTO prenotazioni_has_camera" +
                        "(prenotazioni_idprenotazioni , camera_idcamera)VALUES" +
                        "('"+p.getIdPrenotazione()+"','"+camera.get(i).getIdCamera()+"');";
                st.executeUpdate(queryInsertCameraPrenotata);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Fa unn accesso alla tabella Prenotazioni e mi ritorna una lista delle prenotazioni fatte in un certo periodo.*/
    @Override
    public ArrayList<Prenotazione> cercaPrenotazioni(Date arrivo, Date partenza) {
        ArrayList<Prenotazione> lista = new ArrayList<Prenotazione>();
        String queryPrenotazioniFatte = "SELECT * FROM prenotazioni" +
                "WHERE data_arrivo = '"+arrivo+"' AND data_partenza = '"+partenza+"';";


        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryPrenotazioniFatte);
            while (rs.next()){
                lista.add(new Prenotazione( rs.getDate("data_arrivo"), rs.getDate("data_partenza")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //COMPLETARE
        }

        return lista;
    }

    //Restituisce tutte le prenotazini presenti nel DB
    @Override
    public ArrayList<Prenotazione> getPrenotazioni(Date arrivo) {
        ArrayList<Prenotazione> lista = new ArrayList<Prenotazione>();
        String queryPrenotazioniFatte = "SELECT * FROM prenotazioni" +
                "WHERE data_arrivo = '"+arrivo+"';";


        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryPrenotazioniFatte);
            while (rs.next()){
                lista.add(new Prenotazione( rs.getDate("data_arrivo"), rs.getDate("data_partenza")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //COMPLETARE
        }

        return lista;
    }

    //Restituisce l'ultima prenotazione effettuata
    @Override
    public int getIdMaxPrenotazione() {
        //Usiamo il MAX id perchè,esseno autoincrement, l'id più alto è associato all'ultima prenotazione
        String queryMaxIdOrdine = "SELECT MAX(idprenotazioni) FROM prenotazioni ;";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryMaxIdOrdine);
            if(rs.next()){
                return rs.getInt("MAX(idprenotazioni)");
            }else{

            }

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
            //COMPLETARE
        }

        return 0;
    }


}
