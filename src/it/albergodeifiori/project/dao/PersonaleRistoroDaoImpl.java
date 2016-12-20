package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.PersonaleRistoro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * Si va ad implementare i possibili metodi che può effetturae la classe PersonaleRistoro.
 */
public class PersonaleRistoroDaoImpl implements PersonaleRistoroDAO {

    /**
     *Andiamo ad implementare una classe Singleton.
     * Il Singleton viene usato nella programmazione ad oggetti e costringe il programmatore ad usare un sola istanza
     * della classe creata poichè si eliminano i costruttori.
     * Essa viene usata per creare una classe che mantiene tutti i dettagli del DB.
     */

    private PersonaleRistoroDaoImpl(){}

    private static PersonaleRistoroDAO dao = null;

    //Unico metodo presente nel DAO che ci istanzia un elemento se non lo si ha, altrimenti ci ridà quello esisente
    public static PersonaleRistoroDAO getInstance(){
        if (dao == null){
            dao = new PersonaleRistoroDaoImpl();
        }
        return dao;
    }

    /**
     *Questo metodo permette di visualizzare tutto il personale ristoro presente nell'albergo
     */
    @Override //operazione di riscrittura di un metodo ereditato.
    public List<PersonaleRistoro> getAllPersonaleRistoro() throws DAOException
    {
        //Dichiariamo la chiave che ci permette di trovare il personale ristoro interessato.
        //PersonaleRistoro chiaveRicerca = new PersonaleRistoro("", "", "",""); // Cerca tutti gli elementi
        // return getAddetti(chiaveRicerca);
        ArrayList<PersonaleRistoro> lista =new ArrayList<PersonaleRistoro>();
        //Dichiariamo la chiave che ci permette di trovare il personale delle pulizie interessato.
        // PersonalePulizie chiaveRicerca = new PersonalePulizie("", "", ""); // Cerca tutti gli elementi
        // return getAddetti(chiaveRicerca);

        String queryGetAllpersonale = "SELECT * FROM personale_ristoro";
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetAllpersonale);
            while (rs.next()){
                lista.add(new PersonaleRistoro(rs.getInt("idpersonale_ristoro"),
                        rs.getString("nome"), rs.getString("cognome"),rs.getString("ruolo")));
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
            //COMPLETARE
        }


        return lista;

    }

    /**
     *Questo metodo permette di restituire un determinato addetto presente nell'albergo considerando il suo parametro
     * identificativo.
     */
    @Override
    public PersonaleRistoro getAddetto(int cod) throws DAOException
    {
        //Verifichiamo se l' id inserito è valido per procedere con il metodo get.
        if(cod == 0)
        {
            throw new DAOException("Codice inserito non valido!");
        }

        // Query che permette di visualizzare le caratteristiche dell' addetto ristoro tramite l'id.
        String query = "SELECT *FROM personale_ristoro WHERE idpersonale_ristoro = '"+cod+"'";

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                //L' _ va tolto porco dio! se no da errore (data_nascita)
                return new PersonaleRistoro(rs.getInt("idpersonale_ristoro"), rs.getString("nome"),
                        rs.getString("cognome"),rs.getString("ruolo"));
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
     *Questo metodo permette di visualizzare più addetti al ristoro che possiedono una caratteristica comune.
     */
    @Override
    public List<PersonaleRistoro> getAddetti(PersonaleRistoro a) throws DAOException
    {
        ArrayList<PersonaleRistoro> lista = new ArrayList<PersonaleRistoro>();
        try{
            //Verifica della validità dei campi considerati.
            if (a == null
                    || a.getNome() == null
                    || a.getCognome() == null
                    || a.getRuolo() == null){
                throw new DAOException("In getAddetti: any field can be null");
            }

            Statement st = DAOSettings.getStatement();

            String sql = "select * from personale_ristoro where idpersonale_ristoro like '";
            sql += a.getCodDipRist() + "%' and nome like '" + a.getNome();
            sql += "%' and cognome like '" + a.getCognome() + "%'";
            sql += "%' and ruolo like '" + a.getRuolo() + "%'";

            ResultSet rs = st.executeQuery(sql);
            //Si aggiunge alla lista l' addetto di interesse che si vuol vedere.
            while(rs.next()){
                lista.add(new PersonaleRistoro(rs.getInt("idpersonale_ristoro"), rs.getString("nome"), rs.getString("cognome"),rs.getString("ruolo")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In getAllAddetti(): " + sq.getMessage());
        }
        return lista;
    }

    /**
     *Questo metodo permette di modificare le caratteristiche di un determinato addetto presente nel DB.
     */
    @Override
    public void updatePersonaleRistoro(PersonaleRistoro a,int cod) throws DAOException
    {
        String query = "UPDATE personale_ristoro SET nome='"+a.getNome()+"'," +
                "cognome='"+a.getCognome()+"'," + "ruolo='"+a.getRuolo()+"' WHERE idpersonale_ristoro='"+cod+"';";
        try {
            Statement st = DAOSettings.getStatement();

            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     *Questo metodo permette di fare l'inserimento nel DB di un nuovo addetto
     */
    @Override
    public void insertPersonaleRistoro(PersonaleRistoro a) throws DAOException
    {
        String query = "INSERT INTO personale_ristoro (nome,cognome,ruolo) VALUES (' " + a.getNome() + "','" + a.getCognome()+"','" + a.getRuolo()+"')";

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
     *Questo metodo permette di eliminare un addetto al ristoro nel DB.
     */
    @Override
    public void deletePersonaleRistoro(PersonaleRistoro a) throws DAOException
    {
        String query = "DELETE * FROM personale_ristoro WHERE idpersonale_ristoro='"+a.getCodDipRist()+"';";

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
