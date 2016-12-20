package it.albergodeifiori.project.dao;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Nicola on 06/12/2016.
 */
public class DAOSettings {

    /**
     * Vado a dichiarare delle costanti che mi serviranno durante la connessione al DB.
     * DRIVERNAME -> Permette di creare la connessione al DB;
     * HOST -> Ci dice dove si trova il nostro DB e a cosa ci connettiamo;
     * USERNAME,PWD,SCHEMA -> Sono le credenziali del DB che si useranno per la connessione.
     **/
    public final static String DRIVERNAME = "com.mysql.jdbc.Driver";
    public final static String HOST = "localhost";
    public final static String USERNAME = "albergo";
    public final static String PWD = "albergo";
    public final static String SCHEMA = "albergo";

    static{
        //Si gestisce l'eccezione che si presenta.
        try {
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     *Si crea un metodo che permette di creare la connessione al DB usufruendo delle credenziali e dei dati
     * forniti in precedenza.
     * L'eccezione che si verifica la risolvo mandandola al chiamante.
     */
    public static Statement getStatement() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + HOST  + "/" + SCHEMA, USERNAME, PWD).createStatement();
    }

    /**
     *Si va a cancellare, a chiudere, la connessione creatasi con il DD e tale operazione è sempre buona norma effettuarla.
     * L'eccezione che si verifica la risolvo mandandola al chiamante.
     */
    public static void closeStatement(Statement st) throws SQLException{
        st.getConnection().close();
        st.close();
    }
}
