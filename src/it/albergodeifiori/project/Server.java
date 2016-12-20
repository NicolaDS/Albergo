

package it.albergodeifiori.project;
import it.albergodeifiori.project.dao.*;
import it.albergodeifiori.project.entity.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicola on 06/12/2016.
 */
public class Server {

    /**
     * Andiamo a definire le costatnti che ci serviranno nella comunicazione tra client-server.
     */

    //Dati di PORTA ed HOST per stabilire la connessione
    public static String 	HOST = "localhost";
    public static int 		PORT = 5555;
    //Messaggi da inviare in caso di azione svolte correttamente o meno
    public static String    OK = "ok";
    public static String    ERROR = "er";
    //CLIENTE
    public static String    QUERY_CLIENTE_RIC="req_cliente_ric";
    public static String    QUERY_CLIENTE_INS="req_cliente_ins";
    public static String    QUERY_CLIENTE_UP="req_cliente_up";
    public static String    QUERY_CLIENTE_DEL="req_cliente_del";
    //CAMERA (indicata con ROOM perchè CAMERA è una funzione predefinita e si vogliono evitare conflitti)
    public static String    QUERY_ROOM_RIC="req_room_ric";
    public static String    QUERY_ROOM_INS="req_room_ins";
    public static String    QUERY_ROOM_UP="req_room_up";
    public static String    QUERY_ROOM_DEL="req_room_del";
    public static String    QUERY_ROOM_CLIENTE="req_room_cl";
    public static String    QUERY_ROOM_LIB="req_room_lib";
    //PERSONALE PULIZIE
    public static String    QUERY_PP_INS="req_pp_ins";
    public static String    QUERY_PP_DEL="req_pp_del";
    public static String    QUERY_PP_RIC="req_pp_ric";
    public static String    QUERY_PP_ROOM="req_pp_room";
    //PRODOTTI
    public static String    QUERY_BAR_RIC="req_menu_bar";
    public static String    QUERY_RIST_RIC="req_menu_rist";
    public static String    QUERY_MENU_GIORNO="req_menu_gio";
    //PRENOTAZIONE
    public static String    QUERY_PREN_INS="req_prenotazione_ins";
    //ORDINAZIONE
    public static String QUERY_ORD_INS = "req_ord_ins";
    public static String QUERY_ORD_CLIENTE = "req_ord_cliente";
    public static String QUERY_ORD_DEL = "req_ord_del";
    public static String QUERY_ORD_CAMERIERE = "req_ord_cameriere";
    public static String QUERY_ORD_RIC_PAGARE = "req_ord_tavolo";
    public static String QUERY_ORD_PROD = "req_ord_prod";

    /**
     *Indicando il main andremo a definire un break point.
     */
    public static void main(String[] args) throws Exception {

        // Si va a creare,tramite la socket, un canale di comunicazione tra client e server
        ServerSocket ss = new ServerSocket(PORT);
        String response = null;
        while (true) {
            System.out.println("Server in ascolto sulla porta 5555");
            Socket s = ss.accept();

            /**
             * Si effettua lo scambio di informazioni tra il client e server.
             */

            //Riceve informazione dal client
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //Manda informazione al client
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            //Converte in stringa ciò che il client manda
            String command = in.readLine();

            /**
             * Si vanno a definire le azioni da svolgere in base a ciò che comunica il client
             */

            //Ricerca del cliente
            if(command.equals(QUERY_CLIENTE_RIC)){

                String nome=in.readLine().replace("nome:","").replace("\n", "");
                String cognome=in.readLine().replace("cognome:","").replace("\n", "");
                String soggiorno=in.readLine().replace("soggiorno:","").replace("\n", "");
                String nDoc=in.readLine().replace("numdoc:","").replace("\n", "");
                String dataNas=in.readLine().replace("data_nascita","").replace("\n", "");
                String conto=in.readLine().replace("conto:","").replace("\n", "");

                //Si va a gestire l' eccezione che si crea, tale commento è valido ogni qualvolta si incontra un try-catch.
                try {

                    Cliente chiave = new Cliente(nome, cognome, Integer.valueOf(soggiorno), nDoc, dataNas, Double.valueOf(conto));
                    List<Cliente> lista = ClienteDaoImpl.getInstance().getClienti(chiave);

                    //Segnale di operazione effettuata con successo da parte del serer al client
                    response = OK + "\n";

                    //Stampa
                    for (Cliente item : lista) {
                        response += item.getNome() + ", ";
                        response += item.getCognome() + ", ";
                        response += item.getSoggiorno() + ", ";
                        response += item.getNumDoc() + ", ";
                        response += item.getDataNascita() + ", ";
                        response += item.getConto() + "\n";
                    }
                    response += "\n";
                    //Invio al client
                    out.println(response);
                } catch (Exception e){
                    //Lancio dell' eccezione nel caso in cui non si effettui tutto in modo corretto
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }
            //Inseriemento del cliente
            else if (command.equals(QUERY_CLIENTE_INS)){

                String nome=in.readLine().replace("nome:","").replace("\n", "");
                String cognome=in.readLine().replace("cognome:","").replace("\n", "");
                String nDoc=in.readLine().replace("nDoc:","").replace("\n", "");
                String dataNas=in.readLine().replace("dataNascita:","").replace("\n", "");
                String prezzoSogg=in.readLine();

                try{
                    Cliente chiave = new Cliente(nome, cognome, 0, nDoc, dataNas,Double.valueOf(prezzoSogg));

                    //Andiamo ad inseriere il cliente nel DB
                    chiave.inserisciClienteDB();
                    response= OK+"\n";
                    out.println(response);

                    //Procediamo, una volta inserito il cliente, ad inserire la prenotazione
                    if(in.readLine().equals(QUERY_PREN_INS)){

                        //Definiamo la quantità delle camere da prenotare e ne prendiamo il valore numerico
                        String quantitaCamera=in.readLine();
                        int n=Integer.valueOf(quantitaCamera);
                        ArrayList<Room> listaCamere=new ArrayList<Room>();
                        String[] tipo= new String[n];

                        //Andiamo a splitatre la stringa ricevuta
                        for(int i=0; i<n; i++) {
                            String [] splitta = in.readLine().split(" ");
                            String[] splitta1=splitta[1].split("  ");
                            tipo[i] = splitta1[0];
                        }
                        String dataArrivo=in.readLine();
                        String dataPartenza=in.readLine();
                        ArrayList<Room> listaCamereRichieste=new ArrayList<Room>();

                        /**
                         * Restituisce il tipo e la quantità di camere libere nel range di tempo indicato nel client.
                         */

                        for(int i=0; i<n; i++) {
                            listaCamere = CameraDaoImpl.getInstance().getCamereLibereTipoDB(tipo[i], Date.valueOf(dataArrivo), Date.valueOf(dataPartenza));
                            boolean flag=true;
                            int j=0;
                            while(flag && j<listaCamere.size()){
                                if(i==0){
                                    listaCamereRichieste.add(listaCamere.get(i));
                                }
                                if (listaCamere.get(i).getIdCamera() != listaCamereRichieste.get(j).getIdCamera()) {
                                    listaCamereRichieste.add(listaCamere.get(i));
                                    flag=false;
                                }
                                j++;
                            }
                        }

                        //Inseriamo la prenotazione
                        Prenotazione p1 = new Prenotazione(Date.valueOf(dataArrivo), Date.valueOf(dataPartenza),listaCamereRichieste);
                        p1.insertPrenotazione(chiave);
                        response = OK + "\n";
                        out.println(response);
                    }

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Modifica dei dati di un cliente
            else if(command.equals(QUERY_CLIENTE_UP)){

                String nome=in.readLine().replace("nome:","").replace("\n", "");
                String cognome=in.readLine().replace("cognome:","").replace("\n", "");
                String soggiorno=in.readLine().replace("soggiorno:","").replace("\n", "");
                String nDoc=in.readLine().replace("numdoc:","").replace("\n", "");
                String dataNas=in.readLine().replace("data_nascita","").replace("\n", "");
                String conto=in.readLine().replace("conto:","").replace("\n", "");

                try{
                    Cliente chiave = new Cliente(nome, cognome, Integer.valueOf(soggiorno), nDoc, dataNas, Double.valueOf(conto));
                    ClienteDaoImpl.getInstance().updateClienti(chiave);
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Cancellazione di un cliente
            else if (command.equals(QUERY_CLIENTE_DEL)){
                //L' eliminazione la basiamo sul numero di documento del cliente
                String nDoc=in.readLine().replace("numdoc:","").replace("\n", "");

                try{
                    Cliente.deleteCliente(nDoc);
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Ricerca di una camere dell'albergo
            else if(command.equals(QUERY_ROOM_RIC)){

                String numero=in.readLine().replace("idcamera:","").replace("\n", "");
                String tipo=in.readLine().replace("tipo:","").replace("\n", "");
                String prezzo=in.readLine().replace("prezzo:","").replace("\n", "");
                String stato=in.readLine().replace("stato:","").replace("\n", "");
                String accessibilita=in.readLine().replace("accessibilita","").replace("\n", "");
                String addetto=in.readLine().replace("addetto_pulizia_idaddetto_pulizia:","").replace("\n", "");

                try {

                    Room chiave = new Room(Integer.valueOf(numero),tipo, Double.valueOf(prezzo),Integer.valueOf(stato),
                            Integer.valueOf(accessibilita), Integer.valueOf(addetto));

                    //Considerando le informazioni sulla camera, date in precedenza, facciamo la ricerca
                    List<Room> lista = CameraDaoImpl.getInstance().getCamera(chiave);

                    response = OK + "\n";

                    for (Room item : lista) {
                        response += item.getIdCamera() + ", ";
                        response += item.getTipo()+ ", ";
                        response += item.getPrezzo() + ", ";
                        response += item.getStato()+ ", ";
                        response += item.getAccessibilita() + ", ";
                        response += item.getAddetto_pulizia_idaddetto_pulizia() + "\n";
                    }

                    response += "\n";

                    out.println(response);
                } catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Si procede con l'inserimento di una camera
            else if (command.equals(QUERY_ROOM_INS)){

                String numero=in.readLine().replace("idcamera:","").replace("\n", "");
                String tipo=in.readLine().replace("tipo:","").replace("\n", "");
                String prezzo=in.readLine().replace("prezzo:","").replace("\n", "");
                String stato=in.readLine().replace("stato:","").replace("\n", "");
                String accessibilita=in.readLine().replace("accessibilita","").replace("\n", "");
                String addetto=in.readLine().replace("addetto_pulizia_idaddetto_pulizia:","").replace("\n", "");

                try{
                    Room chiave = new Room(Integer.valueOf(numero),tipo, Double.valueOf(prezzo),Integer.valueOf(stato),
                            Integer.valueOf(accessibilita), Integer.valueOf(addetto));

                    //Considerando le informazioni sulla camera, date in precedenza, facciamo l'inserimento
                    CameraDaoImpl.getInstance().insertCamera(chiave);
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Effettua la modifica di una camera
            else if(command.equals(QUERY_ROOM_UP)){

                String numero=in.readLine().replace("idcamera:","").replace("\n", "");
                String tipo=in.readLine().replace("tipo:","").replace("\n", "");
                String prezzo=in.readLine().replace("prezzo:","").replace("\n", "");
                String stato=in.readLine().replace("stato:","").replace("\n", "");
                String accessibilita=in.readLine().replace("accessibilita","").replace("\n", "");
                String addetto=in.readLine().replace("addetto_pulizia_idaddetto_pulizia:","").replace("\n", "");

                try{
                    Room chiave = new Room(Integer.valueOf(numero),tipo, Double.valueOf(prezzo),Integer.valueOf(stato),
                            Integer.valueOf(accessibilita), Integer.valueOf(addetto));
                    CameraDaoImpl.getInstance().updateCamera(chiave,Integer.parseInt(numero));
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Cancellazione di una camera
            else if (command.equals(QUERY_ROOM_DEL)){
                //Procediamo con la cancellazione (rimozione nel DB) tenendo conto dell'id della camera, che corrisponde
                //al suo numero

                String numero=in.readLine().replace("idcamera:","").replace("\n", "");

                try{
                    CameraDaoImpl.getInstance().deleteCamera(Integer.valueOf(numero));
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Permette di trovare un cliente mediante la camera in cui soggiorna
            else if(command.equals(QUERY_ROOM_CLIENTE)){

                //La ricerca sarà effettuata prendendo in considerazione il numero della camera
                String numero=in.readLine().replace("idcamera:","").replace("\n", "");

                try{
                    Cliente cl=CameraDaoImpl.getInstance().getClienteDaNumeroCamera(Integer.valueOf(numero));

                    response = OK + "\n";
                    response += cl.getNome() + ", ";
                    response += cl.getCognome() + ", ";
                    response += cl.getSoggiorno() + ", ";
                    response += cl.getNumDoc() + ", ";
                    response += cl.getDataNascita() + ", ";
                    response += cl.getConto() + "\n";

                    out.println(response);
                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Restituisce le camere libere nell' arco di tempo richiesto
            else if(command.equals(QUERY_ROOM_LIB)){

                //Indico le date di arrivo e partenza che ci servono per determinare e camere
                String arrivo=in.readLine();
                String partenza=in.readLine();

                try {

                    List<Room> lista = CameraDaoImpl.getInstance().getCamereLibereDB(Date.valueOf(arrivo),Date.valueOf(partenza));

                    response = OK+"\n" ;

                    /**
                     * Da le caretteristiche delle camere nelle date indicate
                     */
                    for (Room item : lista) {
                        response += item.getIdCamera() + ",";
                        response += item.getTipo()+ ",";
                        response += item.getPrezzo() + "\n";
                    }

                    out.println(response);
                } catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Ricerca un addetto alle pulizie
            else if(command.equals(QUERY_PP_RIC))
            {
                String id=in.readLine().replace("idaddetto_pulizia:","").replace("\n", "");
                String nome=in.readLine().replace("nome:","").replace("\n", "");
                String cognome=in.readLine().replace("cognome:","").replace("\n", "");

                try {

                    PersonalePulizie chiave = new PersonalePulizie(Integer.valueOf(id),nome, cognome);

                    //Considerando le informazioni sull' addetto alle pulizie, dati in precedenza, facciamo la ricerca
                    List<PersonalePulizie> lista = PersonalePulizieDaoImpl.getInstance().getAddetti(chiave);

                    response = OK + "\n";

                    for (PersonalePulizie item : lista) {
                        response += item.getNome() + ", ";
                        response += item.getCognome()+ "\n";
                    }

                    response += "\n";

                    out.println(response);
                } catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Inserisce un addetto alle pulizie
            else if (command.equals(QUERY_PP_INS)){
                String id=in.readLine().replace("idaddetto_pulizia:","").replace("\n", "");
                String nome=in.readLine().replace("nome:","").replace("\n", "");
                String cognome=in.readLine().replace("cognome:","").replace("\n", "");

                try{
                    PersonalePulizie chiave = new PersonalePulizie(Integer.valueOf(id),nome, cognome);

                    //Considerando le informazioni sull' addetto alle pulizie, dati in precedenza, facciamo l'inserimento
                    PersonalePulizieDaoImpl.getInstance().insertPersonalePulizie(chiave);
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Rimozione di un addetto alle pulizie
            else if(command.equals(QUERY_PP_DEL)){
                String id=in.readLine().replace("idaddetto_pulizia:","").replace("\n", "");

                try{

                    //Consideriamo l'id dell'addetto alle pulizie per andare ad effettuare la cancellazione
                    PersonalePulizieDaoImpl.getInstance().deletePersonalePulizie(Integer.valueOf(id));
                    response = OK + "\n";
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Ricerca i prodotti del bar con relative caratteristiche
            else if(command.equals(QUERY_BAR_RIC)){

                try{
                    Prodotto p1=new Prodotto();

                    //Resituiamo tutti i prodotti del bar
                    ArrayList<Prodotto> lista=p1.getAllProdottiBar();
                    response = OK + "\n";

                    for (Prodotto item : lista) {
                        response += item.getProdotto() + ",";
                        response += item.getPrezzo()+"\n";
                    }
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Ricerca i prodotti del ristorante con relative caratteristiche
            else if(command.equals(QUERY_RIST_RIC)) {

                try {
                    Prodotto p1 = new Prodotto();

                    //Resituiamo tutti i prodotti del ristorante
                    ArrayList<Prodotto> lista = p1.getAllProdottiRist();
                    response = OK + "\n";

                    for (Prodotto item : lista) {
                        response += item.getProdotto() + ",";
                        response += item.getPrezzo() + "\n";
                    }
                    out.println(response);

                } catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Restituisce il menù del giorno
            else if(command.equals(QUERY_MENU_GIORNO)) {

                try {

                    //Preleviamo dall'ordinazione il menù del giorno
                    String menuDelGiorno = Ordinazione.prelevaMenuGiorno();
                    response = OK + "\n";
                    response += menuDelGiorno;

                    out.println(response);

                } catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }

            //Inserisce l' ordine tenendo anche conto dello stato dell' ordine (attesa,preparazione,pagamento,chiuso)
            else if(command.equals(QUERY_ORD_INS)){
                //Vediamo la quantità deli elementi che ordiniamo
                String quantitaProdotti=in.readLine();
                //Consideriamo gli ordini del servizio in camera
                String servizioCamera=in.readLine();
                //Consideriamo gli ordini del cameriere
                String cameriere=in.readLine();
                int n=Integer.valueOf(quantitaProdotti);
                //Consideriamo i prodotti presi in esame
                ArrayList<Prodotto> listaProdotto=new ArrayList<Prodotto>();
                String[] prodotto= new String[n];
                for(int i=0; i<n; i++) {
                    prodotto[i] = in.readLine();
                }
                //Si associa l' ordine alla camera o al tavolo che l' effettua
                String tavolo=in.readLine();
                String camera=in.readLine();

                try{

                    double conto=0.0;
                    Ordinazione ordine=new Ordinazione(Integer.valueOf(servizioCamera),Integer.valueOf(camera),Integer.valueOf(tavolo),
                            "attesa",conto,Integer.valueOf(cameriere));
                    ArrayList<Integer> id=new ArrayList<Integer>();
                    id=Prodotto.getIdProdotti(prodotto);
                    for(int i=0; i<n; i++){
                        ordine.addProdotto(new Prodotto(id.get(i),prodotto[i]));
                    }
                    ordine.insertOrdineDB();
                    response =OK +"\n";
                    out.println(response);

                }catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }

            }

            //Si associa un determinato ordine ad un cliente
            else if(command.equals(QUERY_ORD_CLIENTE)){
                try{

                    //Il ciente è identificato con il numero del documento
                    String nDoc=in.readLine();

                    //Riceviamo gli ordini di un relativo cliente
                    String ordini = Ordinazione.getOrdiniCliente(nDoc);
                    response = OK + "\n";
                    response += ordini;
                    out.println(response);

                }catch (Exception e){
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }
            //Cancella un ordine identificato dal proprio id
            else if (command.equals(QUERY_ORD_DEL)) {
                String id = in.readLine();
                //un ordine ha uno stato assegnato
                String stato = in.readLine();

                try {
                    //Qui si annulla l'ordine con i parametri passati
                    Ordinazione.annullaOrdine(Integer.valueOf(id), stato);
                    response = OK + "\n";
                    out.println(response);

                } catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }
            //Si ricercano gli ordini assegnati a un cameriere
            else if (command.equals(QUERY_ORD_CAMERIERE)) {
                //Un cameriere è identificato da un id
                String id = in.readLine();
                try {
                    //Qui si ottengono gli ordini assegnati al cameriere
                    String ordini = Ordinazione.getOrdiniCameriere(Integer.valueOf(id));
                    response = OK + "\n";
                    response += ordini;
                    out.println(response);

                } catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            }
            //Si vuole ottenere gli ordini che sono in stato di pagamento
            else if (command.equals(QUERY_ORD_RIC_PAGARE)) {

                try {
                    //Si ottiene una lista di tutti gli ordini che sono in stato da pagare
                    ArrayList<Ordinazione> lista = Ordinazione.getAllOrdiniInPagamento();
                    response = OK + "\n";
                    //Qui ottengo una stringa con i campi (idOrdine, camera, tavolo, ServizioCamera) relativi
                    // agli ordini da pagare
                    for (Ordinazione item : lista) {
                        response += item.getIdOrdine() + ", ";
                        response += item.getCamera() + ", ";
                        response += item.getTavolo() + ", ";
                        response += item.getServizioCamera() + "\n";
                    }
                    out.println(response);

                } catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);
                }
            } else if (command.equals(QUERY_ORD_PROD)) {//da completare

                String id = in.readLine().replace("[", "");
                System.out.println(id);
                response = OK + "\n";
                out.println(response);

                try {
                    String prodotti = Prodotto.getProdottiOrd(Integer.valueOf(id));
                    System.out.println(prodotti);
                    response += prodotti;
                    out.println(response);
                    System.out.println(response);


                } catch (Exception e) {
                    response = ERROR + "\n" + e.getMessage() + "\n\n";
                    out.println(response);

                }

            }


        }
    }
}
