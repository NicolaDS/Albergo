package it.albergodeifiori.project.dao;



import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.Ordinazione;
import it.albergodeifiori.project.entity.PersonaleRistoro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by Domenico on 08/12/2016.
 */

public class OrdinazioneDAOImp implements OrdinazioneDAO {
    /*Costruttore vuoto*/
    private OrdinazioneDAOImp(){};    //deve essere privato poichè istanzierà un oggetto all'interno di una singleton

    private static OrdinazioneDAO dao = null;

    /*Singleton: fa in modo che venga realizzata una sola istanza dell'interfaccia*/
    public static OrdinazioneDAO getIstance(){
        if (dao == null){
            dao = new OrdinazioneDAOImp();
        }
        return dao;
    }


    @Override
    public ArrayList<Ordinazione> getAllOrdinazioniPagare() {
        ArrayList<Ordinazione> lista = new ArrayList<Ordinazione>();
        String queryGetAllOrdinazioni = "SELECT * FROM ordinazioni  WHERE stato = '" + Ordinazione.STATIORDINE[2] + "'";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetAllOrdinazioni);
            while (rs.next()) {
                lista.add(new Ordinazione(rs.getInt("idordinazioni"), rs.getInt("serviziocamera"),
                        rs.getInt("camera"), rs.getInt("tavolo"), rs.getString("stato"), rs.getDouble("conto")));
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return lista;
    }
    @Override
    public ArrayList<Ordinazione> getAllOrdinazioniPreparazione() {
        ArrayList<Ordinazione> lista =new ArrayList<Ordinazione>();
        String queryGetAllOrdinazioni = "SELECT * FROM ordinazioni JOIN ordinazioni_has_personale_ristoro" +
                " WHERE stato = '"+Ordinazione.STATIORDINE[1]+"'";


        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetAllOrdinazioni);
            while (rs.next()){
                lista.add(new Ordinazione( rs.getInt("serviziocamera"),
                        rs.getInt("camera"), rs.getInt("tavolo"), rs.getString("stato"), rs.getDouble("conto"),
                        rs.getInt("presonale_ristoro_idpersonale_ristoro")));
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return lista;
    }

    @Override
    public ArrayList<Ordinazione> getAllOrdinazioniAttesa() {
        ArrayList<Ordinazione> lista =new ArrayList<Ordinazione>();
        String queryGetAllOrdinazioni = "SELECT * FROM ordinazioni JOIN ordinazioni_has_personale_ristoro" +
                " WHERE stato = '"+Ordinazione.STATIORDINE[0]+"'";


        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetAllOrdinazioni);
            while (rs.next()){
                lista.add(new Ordinazione( rs.getInt("serviziocamera"),
                        rs.getInt("camera"), rs.getInt("tavolo"), rs.getString("stato"), rs.getDouble("conto"),
                        rs.getInt("presonale_ristoro_idpersonale_ristoro")));
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return lista;
    }

    /*Il metodo sottostante effettua un access al db sulla tabella Ordinazione:
            * si va a prelevare l'id MINIMO dell'ordine in stato "attesa";
            * ritorna l'id dell'ordine a piu alta priorità secondo la cronologia.
            * */
    @Override
    public int getOrdineCronologico() {
        int id;// conterrà l'id dell'ordinazione a più altra priorità -> l'ordinazione in attesa con id minimo

        String queryGetIdMin = "SELECT MIN(idordinazioni) FROM (SELECT * FROM ordinazioni WHERE stato = 'attesa');";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetIdMin);
            if (rs.next()){
                id = rs.getInt("idordinazioni");
                return id;
            }else{

            }
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void insertOrdineConCamera(Ordinazione ordine, PersonaleRistoro cameriere) {
        int idCliente;

        if (ordine == null || ordine.getStatoOrdine() == null){
            //COMPLETARE: lancio un eccezione
        }
        /*Creazione della query per l'inserimento dei valori nella tabella ordinazione*/
        String queryInsertOrdine = "INSERT INTO `albergo`.`ordinazioni`" +
                "(`idordinazioni`,`serviziocamera`,`camera`,`tavolo`,`stato`,`conto`)VALUES"+
                "('"+ordine.getIdOrdine()+
                "','"+ordine.getServizioCamera()+
                "','"+ordine.getCamera()+
                "','"+ordine.getTavolo()+
                "','"+ordine.getStatoOrdine()+
                "','"+ordine.getContoOrdine()+"');";
        /*Creazione  della query per l'inserimento dei valori nella tabella ordinazione_has_personale_ristoro*/
        String queryInsertCamerierOrdine = "INSERT INTO ordinazioni_has_personale_ristoro" +
                " (ordinazioni_idordinazioni,personale_ristoro_idpersonale_ristoro) " +
                "VALUES ('"+ordine.getIdOrdine()+"','"+cameriere.getCodDipRist()+"');";


        String queryGetIdClienteDaCamera = "SELECT cliente_idcliente FROM cliente_has_prenotazioni as cp" +
                " JOIN prenotazioni_has_camera as pc on(cp.prenotazioni_idprenotazioni=pc.prenotazioni_idprenotazioni) " +
                "JOIN camera as c on(pc.camera_idcamera=c.idcamera) WHERE (c.idcamera = "+ordine.getCamera()+");";


        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryGetIdClienteDaCamera);
            if (rs.next()){
                idCliente = rs.getInt("cliente_idcliente");

                String queryInsertClienteOrdine = "INSERT INTO cliente_has_ordinazioni" +
                        "(cliente_idcliente, ordinazioni_idordinazioni) VALUES" +
                        "('"+idCliente+"','"+ordine.getIdOrdine()+"')";

                st.executeUpdate(queryInsertOrdine);       //inserimento valori nel db
                st.executeUpdate(queryInsertCamerierOrdine);
                st.executeUpdate(queryInsertClienteOrdine);
            }else{
                //COMPLETARE: lancio eccezione x dire che non ho trovato un cliente

            }

            for(int i=0; i<ordine.listaProdotti.size(); i++) {
                String queryInsertOrdineProdotto = "INSERT INTO albergo.ordinazioni_has_prodotto (ordinazioni_idordinazioni," +
                        "prodotto_idprodotto) VALUES  ('"+ordine.getIdOrdine()+"','"+ordine.listaProdotti.get(i).getCodProdotto()+"')";
                st.executeUpdate(queryInsertOrdineProdotto);
            }
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /* Osserva: vado ad inserire anche l'id del cameriere e l'id dell'ordine nella tabella
     * "ordinazioni_has_personale_ristoro" in modo da consentire un JOIN in altri metodi.
     */
    @Override
    public void insertOrdine(Ordinazione ordine, PersonaleRistoro cameriere) {
        if (ordine == null || ordine.getStatoOrdine() == null){
            //COMPLETARE: lancio un eccezione
        }
        /*Creazione della query per l'inserimento dei valori nella tabella ordinazione*/
        String queryInsertOrdine = "INSERT INTO ordinazioni" +
                "(serviziocamera,camera,tavolo,stato,conto)VALUES"+
                "('"+ordine.getIdOrdine()+
                "','"+ordine.getServizioCamera()+
                "','"+ordine.getCamera()+
                "','"+ordine.getTavolo()+
                "','"+ordine.getStatoOrdine()+
                "','"+ordine.getContoOrdine()+"');";
        /*Creazione  della query per l'inserimento dei valori nella tabella ordinazione_has_personale_ristoro*/
        String queryInsertCamerierOrdine = "INSERT INTO ordinazioni_has_personale_ristoro " +
                "(ordinazioni_idordinazioni,personale_ristoro_idpersonale_ristoro) VALUES"+
                "('"+ordine.getIdOrdine()+
                "','"+cameriere.getCodDipRist()+"');";

        try {
            Statement st = DAOSettings.getStatement();
            st.executeUpdate(queryInsertOrdine);       //inserimento valori nel db
            st.executeUpdate(queryInsertCamerierOrdine);

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void updateContoOrdine(Ordinazione ordine, double conto) {
        /*Vado ad aggiornare il conto relativo ad un dato ordine, ricavo l'ordine mediante l'id.
        * Il conto lo calcolo nella classe ordinazione: rappresenta la somma dei prezzi di tutti i prodotti ordinati.
        * */
        String queryUpdateConto = "UPDATE ordinazioni" +
                "SET conto = '"+conto+"' WHERE idordinazioni = '"+ordine.getIdOrdine()+"';";
        try {
            Statement st = DAOSettings.getStatement();
            st.executeUpdate(queryUpdateConto);

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void updateContoTotaleCliente(Ordinazione ordine, double conto) {
       /*Mediante l' id dell'ordine risalgo al cliente alloggiatore e ne aggiorno il conto*/
        String aggiornaConto = "UPDATE cliente "+
                "SET conto = '"+conto+"'"+
                "WHERE idcliente = (SELECT cliente_idcliente " +
                "FROM cliente_has_ordinazioni" +
                "WHERE ordinazioni_idordinazioni = '"+ordine.getIdOrdine()+"');";

        try {
            Statement st = DAOSettings.getStatement();
            st.executeUpdate(aggiornaConto);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void deleteOrdine(int idOrdine) {
        /*La cancellazione si fa passando la chiave primaria della tabella su cui si vuole eliminare una riga*/
        String cancellaOrdine = "DELETE FROM ordinazioni WHERE idordinazioni = '"+idOrdine+"'";

        try {
            Statement st = DAOSettings.getStatement();
            st.executeUpdate(cancellaOrdine);
            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getMaxIdOrdine() {
        //restituisce il massimo id degli ordini
        String queryMaxIdOrdine = "SELECT MAX(idordinazioni) FROM ordinazioni ;";

        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(queryMaxIdOrdine);
            if(rs.next()){
                return rs.getInt("MAX(idordinazioni)");
            }else{

            }

            DAOSettings.closeStatement(st);
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return 0;
    }

    public ArrayList<Ordinazione> getOrdiniCliente(String nDoc) throws SQLException {
        //restituisce una lista di ordinazioni relativi a un cliente che ha effetuato servio in camera
        String query = "SELECT ord.idordinazioni, ord.serviziocamera, ord.camera, ord.tavolo, ord.stato, ord.conto FROM ordinazioni as ord " +
                "JOIN cliente_has_ordinazioni as co " +
                "on (ord.idordinazioni=co.ordinazioni_idordinazioni) JOIN cliente c on(co.cliente_idcliente=c.idcliente)" +
                "WHERE (c.numdoc='" + nDoc + "' AND ord.serviziocamera=1);";
        ArrayList<Ordinazione> lista= new ArrayList<Ordinazione>();
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {lista.add(new Ordinazione(
                    rs.getInt("idordinazioni"),
                    rs.getInt("serviziocamera"),
                    rs.getInt("camera"),
                    rs.getInt("tavolo"),
                    rs.getString("stato"),
                    rs.getDouble("conto")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Ordinazione> getOrdiniCameriere(int id) throws SQLException {
        //restituisce una lista di ordinazioni relative a un cameriere identificato dal proprio id
        String query = "SELECT ord.idordinazioni, ord.serviziocamera, ord.camera, ord.tavolo, ord.stato, ord.conto" +
                " FROM ordinazioni as ord JOIN ordinazioni_has_personale_ristoro as cpr on" +
                " (ord.idordinazioni=cpr.ordinazioni_idordinazioni) JOIN personale_ristoro pr on" +
                "(cpr.personale_ristoro_idpersonale_ristoro=pr.idpersonale_ristoro)" +
                "WHERE (pr.idpersonale_ristoro='" + id + "');";
        ArrayList<Ordinazione> lista= new ArrayList<Ordinazione>();
        try {
            Statement st = DAOSettings.getStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {lista.add(new Ordinazione(
                    rs.getInt("idordinazioni"),
                    rs.getInt("serviziocamera"),
                    rs.getInt("camera"),
                    rs.getInt("tavolo"),
                    rs.getString("stato"),
                    rs.getDouble("conto")));
            }
            DAOSettings.closeStatement(st);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
