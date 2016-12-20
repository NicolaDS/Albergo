package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicola on 06/12/2016.
 * Si va ad implementare i possibili metodi che può effetturae la classe Cliente.
 */
public class ClienteDaoImpl implements ClienteDAO {

    /**
     *Andiamo ad implementare una classe Singleton.
     * Il Singleton viene usato nella programmazione ad oggetti e costringe il programmatore ad usare un sola istanza
     * della classe creata poichè si eliminano i costruttori.
     * Essa viene usata per creare una classe che mantiene tutti i dettagli del DB.
     */
    private ClienteDaoImpl(){}

    private static ClienteDAO dao = null;

    //Unico metodo presente nel DAO che ci istanzia un elemento se non lo si ha, altrimenti ci ridà quello esisente
    public static ClienteDAO getInstance(){
        if (dao == null){
            dao = new ClienteDaoImpl();
        }
        return dao;
    }

    /**
     *Questo metodo permette di visualizzare tutti i clienti presenti nell'albergo
     */

    @Override //operazione di riscrittura di un metodo ereditato.
    public List<Cliente> getAllClienti() throws DAOException
    {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        //Dichiariamo la chiave che ci permette di trovare i clienti interessati.
      /*  Cliente chiaveRicerca = new Cliente(); // Cerca tutti gli elementi
        return getClienti(chiaveRicerca);*/

        String querySelectAll = "SELECT * FROM camera";
        try {

            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(querySelectAll);
            while (rs.next()) {
                lista.add(new Cliente(rs.getString("nome"),
                        rs.getString("cognome"), rs.getInt("soggiorno"), rs.getString("numdoc"),
                        rs.getString("data_nascita"), rs.getDouble("conto")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            e.printStackTrace();
            //COMPLETARE
        }
        return lista;

    }

    /**
     *Questo metodo permette di restituire un determinato cliente presente nell'albergo considerando il suo parametro
     * identificativo.
     */

    @Override
    public Cliente getCliente(int id) throws DAOException
    {
        //Verifichiamo se l' id inserito è valido per procedere con il metodo get.
        if(id == 0)
        {
            throw new DAOException("Id inserito non valida!");
        }

        // Query che permette di visualizzare le caratteristiche di un cliente tramite l'id.
        String query = "SELECT *FROM cliente WHERE idcliente = '"+id+"'"; //DA RIVEDERE

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                //L' _ va tolto porco dio! se no da errore (data_nascita)
                return new Cliente(rs.getString("nome"), rs.getString("cognome"), rs.getInt("soggiorno"),rs.getString("numdoc"),rs.getString("data_nascita"),rs.getDouble("conto"));
            } else {
                return null;
            }
        }
        catch (SQLException e) {
            //Nel caso si verifichi l'eccezione
            throw new DAOException(e.getMessage());
        }
    }

    /**
     *Questo metodo permette di visualizzare più clienti che possiedono una caratteristica comune.
     */

    @Override
    public List<Cliente> getClienti(Cliente a) throws DAOException
    {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try{
            //Verifica della validità dei campi considerati.
            if (a == null || a.getNome() == null
                    || a.getCognome() == null
                    || a.getNumDoc() == null
                    || a.getDataNascita() == null
                    || a.getConto() == .0){
                throw new DAOException("In getClienti: any field can be null");
            }

            Statement st = DAOSettings.getStatement();

            String sql = "select * from cliente where cognome like '";
            sql += a.getCognome() + "%' and nome like '" + a.getNome();
            sql += " and soggiorno like '" + a.getSoggiorno() + "%'";
            sql += " and numdoc like '" + a.getNumDoc() + "%'";
            sql += " and data_nascita like '" + a.getDataNascita() + "%'";
            sql += " and conto like '" + a.getConto() + "%'";

            ResultSet rs = st.executeQuery(sql);
            //Si aggiunge alla lista il cliente di interesse che si vuol vedere.
            while(rs.next()){
                lista.add(new Cliente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getInt("soggiorno"),
                        rs.getString("numdoc"),
                        rs.getString("data_nascita")
                        ,rs.getDouble("conto")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In getAllClienti(): " + sq.getMessage());
        }
        return lista;
    }

    /**
     *Questo metodo permette di modificare le caratteristiche di un determinato cliente presente nel DB.
     */

    @Override
    public void updateClienti(Cliente a) throws DAOException
    {
        String query = "UPDATE cliente SET nome='"+a.getNome()+"'," +
                "cognome='"+a.getCognome()+"',soggiorno='"+a.getSoggiorno()+"',numdoc='"+a.getNumDoc()+"',data_nascita='"+a.getDataNascita()+
                "',conto='"+a.getConto()+"' WHERE idCliente='"+a.getIdCliente()+"';";
        try {
            Statement st = DAOSettings.getStatement();

            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     *Questo metodo permette di fare l'inserimento nel DB di un nuovo cliente
     */

    @Override
    public void insertCliente(Cliente a) throws DAOException
    {
        String query = "INSERT INTO cliente (nome,cognome,soggiorno,numdoc,data_nascita,conto) VALUES (' " + a.getNome() + "','" + a.getCognome()+"','"+
                a.getSoggiorno()+"','"+a.getNumDoc()+"','" + a.getDataNascita() + "','" + a.getConto()+"')";

        try {

            Statement st = DAOSettings.getStatement();

            //Si fa per effettuare query di modifica al DB
            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     *Questo metodo permette di eliminare un cliente presente nel DB.
     */

    @Override //da modificare eventualmente
    public void deleteCliente(Cliente a) throws DAOException
    {
        int id = a.getIdCliente();
        String query = "DELETE FROM cliente WHERE idCliente='"+id+"';";

        try {

            Statement st = DAOSettings.getStatement();

            //Si fa per effettuare query di modifica al DB
            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    //Restituisce l'id del cliente partendo dal numero di documento di quest'ultimo
    @Override
    public int getIdCliente(String nDoc) {
        int idCliente = 0;
        String queryGetIdCliente = "SELECT idcliente FROM cliene WHERE numdoc = "+nDoc+";";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetIdCliente);
            if(rs.next()){
                idCliente = rs.getInt("idcliente");
            }else{
                //se non esiste niente lancia eccezione
            }

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCliente;
    }

    //Ci restituisce l' ultimo cliente immesso nel DB
    @Override
    public int getIdCliente() {
        int idCliente = 0;
        //Usiamo il MAX id perchè,esseno autoincrement, l'id più alto è associato all'ultimo cliente
        String queryGetIdCliente = "SELECT MAX(idcliente) FROM cliente ;";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetIdCliente);
            if(rs.next()){
                idCliente = rs.getInt("MAX(idcliente)");
            }else{
                //se non esiste niente lancia eccezione
            }

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCliente;
    }

    //Ci permette di modificare il conto del cliente
    public void updateContoClienti(Cliente a) throws DAOException {
        String queryAggiornaConto = "UPDATE cliente SET conto = '"+a.getConto()+"' WHERE " +
                "idcliente = '"+a.getIdCliente()+"';";

        try {
            Statement st = DAOSettings.getStatement();
            st.executeUpdate(queryAggiornaConto);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    //Ci permette di eliminare un cliente dal documento di identità
    public void deleteClienti(String nDoc) throws DAOException{
        String query="DELETE FROM cliente WHERE numdoc='"+nDoc+"';";
        try {
            Statement st = DAOSettings.getStatement();
            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
