package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.PersonalePulizie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * * Si va ad implementare i possibili metodi che può effetturae la classe PersonalePulizie.
 */

public class PersonalePulizieDaoImpl implements PersonalePulizieDAO {
    /**
     *Andiamo ad implementare una classe Singleton.
     * Il Singleton viene usato nella programmazione ad oggetti e costringe il programmatore ad usare un sola istanza
     * della classe creata poichè si eliminano i costruttori.
     * Essa viene usata per creare una classe che mantiene tutti i dettagli del DB.
     */

    private PersonalePulizieDaoImpl(){}

    private static PersonalePulizieDAO dao = null;

    //Unico metodo presente nel DAO che ci istanzia un elemento se non lo si ha, altrimenti ci ridà quello esisente
    public static PersonalePulizieDAO getInstance(){
        if (dao == null){
            dao = new PersonalePulizieDaoImpl();
        }
        return dao;
    }

    /**
     *Questo metodo permette di visualizzare tutto il personale pulizie presente nell'albergo
     */
    @Override //operazione di riscrittura di un metodo ereditato.
    public List<PersonalePulizie> getAllPersonalePulizie() throws DAOException
    {
        ArrayList<PersonalePulizie> lista =new ArrayList<PersonalePulizie>();
        //Dichiariamo la chiave che ci permette di trovare il personale delle pulizie interessato.
        // PersonalePulizie chiaveRicerca = new PersonalePulizie("", "", ""); // Cerca tutti gli elementi
        // return getAddetti(chiaveRicerca);

        String queryGetAlladdetti = "SELECT * FROM addetto_pulizia";
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetAlladdetti);
            while (rs.next()){
                lista.add(new PersonalePulizie(rs.getInt("idaddetto_pulizia"),
                        rs.getString("nome"), rs.getString("cognome")));
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }


        return lista;

    }

    /**
     *Questo metodo permette di restituire un determinato addetto presente nell'albergo considerando il suo parametro
     * identificativo.
     */
    @Override
    public PersonalePulizie getAddetto(int cod) throws DAOException
    {
        //Verifichiamo se l' id inserito è valido per procedere con il metodo get.
        if(cod == 0)
        {
            throw new DAOException("Codice inserito non valido!");
        }

        // Query che permette di visualizzare le caratteristiche dell' addetto alle pulizie tramite l'id.
        String query = "SELECT *FROM addetto_pulizia WHERE idaddetto_pulizia = '"+cod+"'";

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                //L' _ va tolto porco dio! se no da errore (data_nascita)
                return new PersonalePulizie(rs.getInt("idaddetto_pulizia"), rs.getString("nome"), rs.getString("cognome"));
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
     *Questo metodo permette di visualizzare più addetti alle pulizie che possiedono una caratteristica comune.
     */
    @Override
    public List<PersonalePulizie> getAddetti(PersonalePulizie a) throws DAOException
    {
        ArrayList<PersonalePulizie> lista = new ArrayList<PersonalePulizie>();
        try{
            //Verifica della validità dei campi considerati.
            if (a == null ){
                throw new DAOException("In getAddetti: any field can be null");
            }

            Statement st = DAOSettings.getStatement();

            String sql = "select * from addetto_pulizia where idaddetto_pulizia like '";
            sql += a.getCodDipPul() + "%' and nome like '" + a.getNome();
            sql += "%' and cognome like '" + a.getCognome() + "%'";

            ResultSet rs = st.executeQuery(sql);
            //Si aggiunge alla lista l'addetto di interesse che si vuol vedere.
            while(rs.next()){
                lista.add(new PersonalePulizie(rs.getInt("idaddetto_pulizia"), rs.getString("nome"), rs.getString("cognome")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In getAllAddetti(): " + sq.getMessage());
        }
        return lista;
    }

    /**
     *Questo metodo permette di fare l'inserimento nel DB di un nuovo addetto
     */
    @Override
    public void insertPersonalePulizie(PersonalePulizie a) throws DAOException
    {
        String query = "INSERT INTO addetto_pulizia (nome,cognome) VALUES (' " + a.getNome() + "','" + a.getCognome()+"')";

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
     *Questo metodo permette di eliminare un addetto delle pulizie presente nel DB.
     */
    @Override
    public void deletePersonalePulizie(int a) throws DAOException
    {
        String query = "DELETE  FROM addetto_pulizia WHERE idaddetto_pulizia='"+a+"';";

        try {

            Statement st = DAOSettings.getStatement();

            //Si fa per effettuare query di modifica al DB
            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
}

