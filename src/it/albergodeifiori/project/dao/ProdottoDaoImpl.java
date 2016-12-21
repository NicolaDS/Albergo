package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Prodotto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * Si va ad implementare i possibili metodi che può effettuare la classe Prodotto.
 */
public class ProdottoDaoImpl implements  ProdottoDAO{

    /**
     *Andiamo ad implementare una classe Singleton.
     * Il Singleton viene usato nella programmazione ad oggetti e costringe il programmatore ad usare un sola istanza
     * della classe creata poichè si eliminano i costruttori.
     * Essa viene usata per creare una classe che mantiene tutti i dettagli del DB.
     */

    private ProdottoDaoImpl(){}

    private static ProdottoDAO dao = null;

    //Unico metodo presente nel DAO che ci istanzia un elemento se non lo si ha, altrimenti ci ridà quello esisente
    public static ProdottoDAO getInstance(){
        if (dao == null){
            dao = new ProdottoDaoImpl();
        }
        return dao;
    }

    /**
     *Questo metodo permette di visualizzare tutti i prodotti presenti nell'albergo
     */
    @Override //operazione di riscrittura di un metodo ereditato.
    public ArrayList<Prodotto> getAllProdotto() throws DAOException
    {
        //Dichiariamo la chiave che ci permette di trovare il prodotto interessato.
        // Prodotto chiaveRicerca = new Prodotto("", "", "","",""); // Cerca tutti gli elementi
        // return getProdotti(chiaveRicerca);
        ArrayList<Prodotto> lista =new ArrayList<Prodotto>();
        //Dichiariamo la chiave che ci permette di trovare il personale delle pulizie interessato.
        // PersonalePulizie chiaveRicerca = new PersonalePulizie("", "", ""); // Cerca tutti gli elementi
        // return getAddetti(chiaveRicerca);

        String queryGetAllprodotti = "SELECT * FROM prodotto";
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetAllprodotti);
            while (rs.next()){
                lista.add(new Prodotto(rs.getInt("idprodotto"),
                        rs.getString("nome"),rs.getInt("bar"),rs.getDouble("prezzo") ,rs.getString("giorno_settimana")));
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return lista;
    }

    @Override
    public Prodotto getProdotto(int id) throws DAOException {
        //Verifichiamo se l' id inserito è valido per procedere con il metodo get.
        if (id == 0) {
            throw new DAOException("Codice inserito non valido!");
        }

        // Query che permette di visualizzare le caratteristiche del prodotto tramite l'id.
        String query = "SELECT *FROM prodotto WHERE idprodotto = '" + id + "'";

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {

                return new Prodotto(rs.getInt("idprodotto"), rs.getString("nome"), rs.getInt("bar"), rs.getDouble("prezzo"), rs.getString("giorno_settimana"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            //Nel caso si verifichi l'eccezione
            throw new DAOException(e.getMessage());
        }
    }
    public ArrayList<Prodotto> getProdottoBar() throws DAOException {
        //Verifichiamo se l' id inserito è valido per procedere con il metodo get.

        // Query che permette di visualizzare le caratteristiche del prodotto tramite l'id.
        String query = "SELECT *FROM prodotto WHERE bar = '" +1+"'";
        ArrayList<Prodotto> lista =new ArrayList<Prodotto>();

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                lista.add(new Prodotto(rs.getInt("idprodotto"),
                        rs.getString("nome"),rs.getInt("bar"),rs.getDouble("prezzo") ,rs.getString("giorno_settimana")));
            }
        } catch (SQLException e) {
            //Nel caso si verifichi l'eccezione
            throw new DAOException(e.getMessage());
        }
        return lista;
    }
    public ArrayList<Prodotto> getProdottoRist() throws DAOException {
        //Verifichiamo se l' id inserito è valido per procedere con il metodo get.

        // Query che permette di visualizzare le caratteristiche del prodotto tramite l'id.
        String query = "SELECT *FROM prodotto WHERE bar = '" +0+"'";
        ArrayList<Prodotto> lista =new ArrayList<Prodotto>();

        try {

            //Apro la connessione
            Statement st = DAOSettings.getStatement();
            //Effettuo la query di lettura del DB
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                lista.add(new Prodotto(rs.getInt("idprodotto"),
                        rs.getString("nome"),rs.getInt("bar"),rs.getDouble("prezzo") ,rs.getString("giorno_settimana")));
            }
        } catch (SQLException e) {
            //Nel caso si verifichi l'eccezione
            throw new DAOException(e.getMessage());
        }
        return lista;
    }
    /**
     *Questo metodo permette di visualizzare più prodotti che possiedono una caratteristica comune.
     */
    @Override
    public List<Prodotto> getProdotti(Prodotto a) throws DAOException
    {
        ArrayList<Prodotto> lista = new ArrayList<Prodotto>();
        try{
            //Verifica della validità dei campi considerati.
            if (a == null ){
                throw new DAOException("In getProdotti: any field can be null");
            }

            Statement st = DAOSettings.getStatement();

            String sql = "select * from prodotti where idprodotto like '";
            sql += a.getCodProdotto() + "%' and nome like '" + a.getProdotto();
            sql += "%' and bar like '" + a.getOrdineBar() + "%'";
            sql += "%' and prezzo like '" + a.getPrezzo() + "%'";
            sql += "%' and giorno_settimana like '" + a.getGiorno() + "%'";

            ResultSet rs = st.executeQuery(sql);
            //Si aggiunge alla lista l' addetto di interesse che si vuol vedere.
            while(rs.next()){
                lista.add(new Prodotto(rs.getInt("idprodotto"), rs.getString("nome"), rs.getInt("bar"), rs.getDouble("prezzo"), rs.getString("giorno_settimana")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In getAllProdotti(): " + sq.getMessage());
        }
        return lista;
    }

    @Override
    public ArrayList<Prodotto> getProdotti(String giorno) throws DAOException
    {
        ArrayList<Prodotto> lista = new ArrayList<Prodotto>();

        try{
            //Verifica della validità dei campi con
            Statement st = DAOSettings.getStatement();

            String sql = "select * from prodotto where giorno_settimana like '" + giorno + "'";

            ResultSet rs = st.executeQuery(sql);
            //Si aggiunge alla lista l' addetto di interesse che si vuol vedere.
            while(rs.next()){
                lista.add(new Prodotto(rs.getInt("idprodotto"), rs.getString("nome"), rs.getInt("bar"),
                        rs.getDouble("prezzo"), rs.getString("giorno_settimana")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In getAllProdotti(): " + sq.getMessage());
        }
        return lista;
    }



    /**
     *Questo metodo permette di modificare le caratteristiche di un determinato prodotto presente nel DB.
     */
    @Override
    public void updateProdotto(Prodotto a,int id) throws DAOException
    {
        String query = "UPDATE prodotto SET nome='"+a.getProdotto()+"'," +
                "bar='"+a.getOrdineBar()+"'," + "prezzo='"+a.getPrezzo()+"' ," + "giorno_settimana='"+a.getGiorno()+"' WHERE idprodotto = '"+id+"';";
        try {
            Statement st = DAOSettings.getStatement();

            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     *Questo metodo permette di fare l'inserimento nel DB di un nuovo prodotto
     */
    @Override
    public void insertProdotto(Prodotto a) throws DAOException
    {
        String query = "INSERT INTO prodotto (nome,bar,prezzo,giorno_settimana) VALUES (' " + a.getProdotto() + "','" + a.getOrdineBar()+"','" + a.getPrezzo()+"','" + a.getGiorno()+"')";

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
     *Questo metodo permette di eliminare un predotto nel DB.
     */
    @Override
    public void deleteProdotto(Prodotto a) throws DAOException
    {
        String query = "DELETE * FROM prodotto WHERE idprodotto='"+a.getCodProdotto()+"';";

        try {

            Statement st = DAOSettings.getStatement();

            //Si fa per effettuare query di modifica al DB
            int n=st.executeUpdate(query);
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
    public int getIdProdottoDB(String prodotto) throws DAOException {
        int id=0;
        String query="SELECT idprodotto FROM prodotto WHERE nome='"+prodotto+"';";
        try {

            Statement st = DAOSettings.getStatement();
            ResultSet rs=st.executeQuery(query);
            //Si fa per effettuare query di modifica al DB
            rs.next();
            id=rs.getInt("idprodotto");
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return id;
    }

    //permette di prendere i prodotti relativi all'ordinazione
    public ArrayList<Prodotto> getProdottiOrd(int idOrdinazione) throws DAOException{

        ArrayList<Prodotto> lista = new ArrayList<Prodotto>();
        String query="SELECT p.idprodotto, p.nome, p.bar, p.prezzo, p.giorno_settimana FROM prodotto as p JOIN ordinazioni_has_prodotto" +
                " as op on (p.idprodotto=op.prodotto_idprodotto)" +
                "JOIN ordinazioni as o on(op.ordinazioni_idodinazioni=o.idordinazioni) WHERE p.idprodotto="+idOrdinazione+");";

        try{
            Statement st= DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                lista.add(new Prodotto(rs.getInt("idprodotto"), rs.getString("nome"), rs.getInt("bar"),
                        rs.getDouble("prezzo"), rs.getString("giorno_settimana")));
            }
            DAOSettings.closeStatement(st);
        }catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return lista;
    }
}

