package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;

import it.albergodeifiori.project.entity.Room;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * Si va ad implementare i possibili metodi che pu� effetturae la classe Room.
 */

public class CameraDaoImpl implements CameraDAO {
    /**
     * Andiamo ad implementare una classe Singleton.
     * Il Singleton viene usato nella programmazione ad oggetti e costringe il programmatore ad usare un sola istanza
     * della classe creata poich� si eliminano i costruttori.
     * Essa viene usata per creare una classe che mantiene tutti i dettagli del DB.
     */
    private CameraDaoImpl() {
    }

    private static CameraDAO dao = null;

    //Unico metodo presente nel DAO che ci istanzia un elemento se non lo si ha, altrimenti ci rid� quello esisente
    public static CameraDAO getInstance() {
        if (dao == null) {
            dao = new CameraDaoImpl();
        }
        return dao;
    }

    /**
     * Questo metodo permette di visualizzare tutte le camere presenti nell'albergo
     */

    @Override //operazione di riscrittura di un metodo ereditato.
    public List<Room> getAllCamera() throws DAOException {
        ArrayList<Room> lista = new ArrayList<Room>();
        //Dichiariamo la chiave che ci permette di trovare le camere interessate.
        //Room chiaveRicerca = new Room("", "", "", "", "", "", ""); // Cerca tutti gli elementi
        //return getCamera(chiaveRicerca);

        String querySelectAll = "SELECT * FROM camera";
        try {

            //Ci permette di aprire la connessione con il DB e di eseguire le azioni di interesse
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(querySelectAll);
            while (rs.next()) {
                lista.add(new Room(rs.getInt("idcamera"), rs.getString("tipo"),
                        rs.getDouble("prezzo"), rs.getInt("stato"), rs.getInt("accessibilit�"),
                        rs.getInt("addetto_pulizia_idaddetto_pulizia")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            e.printStackTrace();
            //COMPLETARE
        }
        return lista;
    }

    /**
     * Questo metodo permette di restituire una determinata camera presente nell'albergo considerando il suo parametro
     * identificativo.
     */

    @Override
    public Room getCamera(int num) throws DAOException {
        //Verifichiamo se l' id inserito � valido per procedere con il metodo get.
        if (num == 0) {
            throw new DAOException("Numero inserito non valido!");
        }

        // Query che permette di visualizzare le caratteristiche di un cliente tramite l'id.
        String query = "SELECT *FROM camera WHERE idcamera = '" + num + "'";

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {

                return new Room(rs.getInt("idcamera"), rs.getString("tipo"), rs.getDouble("prezzo"), rs.getInt("stato"), rs.getInt("accessibilit�"), rs.getInt("addetto_pulizia_idaddetto_pulizia"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            //Nel caso si verifichi l'eccezione
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Questo metodo permette di visualizzare pi� camere che possiedono una caratteristica comune.
     */
    @Override
    public List<Room> getCamera(Room a) throws DAOException {
        ArrayList<Room> lista = new ArrayList<Room>();
        try {
            //Verifica della validit� dei campi considerati.
            if (a == null) {
                throw new DAOException("In getCamere: any field can be null");
            }

            Statement st = DAOSettings.getStatement();

            String sql = "select * from camera where idcamera like '";
            sql += a.getIdCamera() + "%' and tipo like '" + a.getTipo();
            sql += "%' and prezzo like '" + a.getPrezzo() + "%'";
            sql += " and stato like '" + a.getStato() + "%'";
            sql += " and accessibilit� like '" + a.getAccessibilita() + "%'";
            sql += " and addetto_pulizia_idaddetto_pulizia like '" + a.getAddetto_pulizia_idaddetto_pulizia() + "%'";

            ResultSet rs = st.executeQuery(sql);
            //Si aggiunge alla lista la camera di interesse che si vuol vedere.
            while (rs.next()) {
                lista.add(new Room(rs.getInt("idcamera"), rs.getString("tipo"), rs.getDouble("prezzo"),
                        rs.getInt("stato"), rs.getInt("accessibilit�"), rs.getInt("addetto_pulizia_idaddetto_pulizia")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException sq) {
            throw new DAOException("In getAllCamere(): " + sq.getMessage());
        }
        return lista;
    }

    /**
     * Questo metodo permette di modificare le caratteristiche di una determinata camera presente nel DB.
     */
    @Override
    public void updateCamera(Room a, int num) throws DAOException {
        String query = "UPDATE camera SET tipo='" + a.getTipo() + "'," +
                "prezzo='" + a.getPrezzo() + "',stato='" + a.getStato() + "',accessibilit�='" + a.getAccessibilita() + "'" +
                "',addetto_pulizia_idaddetto_pulizia='" + a.getAddetto_pulizia_idaddetto_pulizia() + "' WHERE idCamera='" + num + "';";
        try {
            Statement st = DAOSettings.getStatement();

            int n = st.executeUpdate(query);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Questo metodo permette di fare l'inserimento nel DB di una nuova camera
     */
    @Override
    public void insertCamera(Room a) throws DAOException {
        String query = "INSERT INTO camera (tipo,prezzo,stato,accessibilit�,codpersonale,addetto_pulizia_idaddetto_pulizia) VALUES (' " + a.getTipo() + "','" + a.getPrezzo() + "','" +
                a.getStato() + "','" + a.getAccessibilita() + "','" + a.getAddetto_pulizia_idaddetto_pulizia() + "')";

        try {

            Statement st = DAOSettings.getStatement();
            //Si fa per effettuare query di modifica al DB
            int n = st.executeUpdate(query);
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Questo metodo permette di eliminare una camera presente nel DB.
     */
    @Override
    public void deleteCamera(int a) throws DAOException {
        String query = "DELETE FROM camera WHERE idCamera='" + a + "';";

        try {

            Statement st = DAOSettings.getStatement();

            //Si fa per effettuare query di modifica al DB
            int n = st.executeUpdate(query);
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    //Andiamo ad identificare un cliente dal suo numero di camera
    @Override
    public Cliente getClienteDaNumeroCamera(int nCamera) {
        Cliente cliente = null;

        //La query che ci permette di far ci� � data da una serie di join tra le tabelle cliente,prenotazioni,camera
        String queryGetClienteDaCamera = "select * from cliente as c join cliente_has_prenotazioni as cp" +
                " on(cp.cliente_idcliente = c.idcliente) join prenotazioni as p on " +
                "(p.idprenotazioni=cp.prenotazioni_idprenotazioni) join prenotazioni_has_camera as pc on " +
                "(pc.prenotazioni_idprenotazioni=p.idprenotazioni) join camera as ca on (ca.idcamera=pc.camera_idcamera)" +
                " where ca.idcamera="+nCamera+";";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetClienteDaCamera);
            if (rs.next()) {
                cliente = new Cliente(rs.getString("nome"), rs.getString("cognome"), rs.getInt("soggiorno"),
                        rs.getString("numdoc"), rs.getString("data_nascita"), rs.getDouble("conto"));
            }
            else {
                //non esistono riche-> lanciamo un eccezione
            }

            DAOSettings.closeStatement(st);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cliente;
    }

    //Andaimo ad identificare l'accessibilit� di una camera tramite il numero di camera (ovvero il suo id)
    @Override
    public int setAccess(int access,int id) throws DAOException {
        boolean accesso;
        String sql = "UPDATE camera SET accessibilita='"+access+"'  where idcamera '" + id + "';";
        try {
            Statement st=DAOSettings.getStatement();
            ResultSet rs= st.executeQuery(sql);
            rs.next();
            access=rs.getInt("accessibilita");
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return access;
    }

    /*Controlla le camere libere*/
/*Ritorna le camere libere*/
    public ArrayList<Room> getCamereLibereDB(Date dataArrivoDesiderata, Date dataPartenzaDesiderata) {
        ArrayList<Room> lista = new ArrayList<Room>();

        String queryCamereLibere = "select c.idcamera, c.tipo, c.prezzo, c.stato, c.accessibilit�, c.addetto_pulizia_idaddetto_pulizia" +
                " FROM camera c join prenotazioni_has_camera pp on(c.idcamera=pp.camera_idcamera) join prenotazioni as p on (p.idprenotazioni=pp.prenotazioni_idprenotazioni)" +
                "  where (p.data_arrivo>='"+dataPartenzaDesiderata+"' or p.data_partenza<='"+dataArrivoDesiderata+"')";
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryCamereLibere);

            while (rs.next()) {
                lista.add(new Room(rs.getInt("idcamera"), rs.getString("tipo"), rs.getDouble("prezzo"), rs.getInt("stato"),
                        rs.getInt("accessibilit�"), rs.getInt("addetto_pulizia_idaddetto_pulizia")));
                System.out.println(rs.getString("tipo"));
            }


            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    //Ritorna il tipo delle camere libere
    public ArrayList<Room> getCamereLibereTipoDB(String tip,Date dataArrivoDesiderata, Date dataPartenzaDesiderata) {
        ArrayList<Room> lista = new ArrayList<Room>();

        String queryCamereLibere = "select c.idcamera, c.tipo, c.prezzo, c.stato, c.accessibilit�, c.addetto_pulizia_idaddetto_pulizia" +
                " FROM camera c join prenotazioni_has_camera pp on(c.idcamera=pp.camera_idcamera) join prenotazioni as p on (p.idprenotazioni=pp.prenotazioni_idprenotazioni)" +
                "  where ((p.data_arrivo>='"+dataPartenzaDesiderata+"' or p.data_partenza<='"+dataArrivoDesiderata+"') AND c.tipo='"+tip+"');";
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryCamereLibere);

            while (rs.next()) {
                lista.add(new Room(rs.getInt("idcamera"), rs.getString("tipo"), rs.getDouble("prezzo"), rs.getInt("stato"),
                        rs.getInt("accessibilit�"), rs.getInt("addetto_pulizia_idaddetto_pulizia")));
            }

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

