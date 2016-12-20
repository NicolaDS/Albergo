package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    final private String HOST = "localhost";
    final private String PORTA = "5555";

    /*INTERFACCIA CLIENTE*/
    @FXML
    private DatePicker datePickerArrivo;

    @FXML
    private DatePicker datePickerPartenza;

    @FXML
    private TextField textFielNomeCliente;

    @FXML
    private TextField textFieldCognomeCliente;

    @FXML
    private TextField textFielDocumentoCliente;

    @FXML
    private TextField textFieldDataNascitaCliente;

    @FXML
    private ListView listViewOrdinazioniEffettuate;

    @FXML
    private ListView listViewCamereCliente;

    @FXML
    private ListView listViewMenuBarCliente;

    @FXML
    private ListView listViewMenuRistoranteCliente;

    @FXML
    private TextField textFieldTotaleOrdinazioneCliente;

    @FXML
    private TextArea textAreaInfoCamereCliente;

    @FXML
    private TextField textFieldPrezzoCameraClienti;

    @FXML
    private ListView listViewProdottiSelezionatiCliente;

    @FXML
    private TextArea textAreaInfoPrenotazioniCliente;

    @FXML
    private TextArea textAreaMenuDelGiorno;

    @FXML
    private TextField textFieldNumeroCameraCliente;

    @FXML
    private TextField textFieldNumeroCameraCaricaOrdini;

    /*Roba giacomo*/
    @FXML
    private TextField NcameraTextField;
    @FXML
    private TextField idCameriereTextField;
    @FXML
    private TextField NtavoloTextField;
    @FXML
    private ListView MenudelgiornoListView;
    @FXML
    private ListView menubarListView;
    @FXML
    private ListView MenuristoranteListView;
    @FXML
    private ListView ProdottitavoloListView;
    @FXML
    private ListView OrdinazionifatteListView;

    @FXML
    private TextField textFieldIdCameriereCaricaOrdini;


    /*Fine */

    /*Camere a disposizione*/
    final String DOPPIA = "doppia";
    final String SINGOLA = "singola";
    final String SUITE = "suite";
    final String TRIPLA = "tripla";

    double contoCamere;
    /*Serve per mantenere una lista dei prodotti selezionari*/
    ArrayList<String> listaProdotti = new ArrayList<String>();
    /*Mantiene una lista delle camere da prenotare*/
    ArrayList<String> listaCamere = new ArrayList<String>();
    /*Informazioni relative alle camere*/
    final String infoSingola = "Camera singola: \n\n"+"- 1 letto singolo\n"+"- Tv digitale terrestre\n"+
            "- Cassetta di sicurezza\n\n"+"- Wi-fi\n\n"+"- Frigorifero portabevande";
    final String infoDoppia = "Camera doppia: \n\n"+"- 1 letto matrimoniale\n"+"- Tv digitale terrestre\n"+
            "- Balcone vista mare\n"+"- Cassetta di sicurezza\n\n"+"- Wi-fi\n\n"+"- Frigorifero portabevande";
    final String infoTripla = "Camera tripla: \n\n"+"- 1 letto matrimoniale\n"+"- 1 letto singolo\n"+"- Tv digitale terrestre\n"+
            "- Balcone vista mare\n"+"- Cassetta di sicurezza\n\n"+"- Wi-fi\n\n"+"- Frigorifero portabevande";
    final String infoSuite = "Camera suite: \n\n"+"- 1 letto matrimoniale ad acqua\n"+"- Tv digitale terrestre, sky, mediaset premium\n"+
            "- Terrazzo vista mare\n"+"- Cassetta di sicurezza\n\n"+"- Wi-fi\n\n"+"- Vasca idromassaggio";

    /*Prezzo menu del giorno*/
    double prezzoTotMenuGiorno ;  //prezzo totale del menu del giorno
    /*Serve per la visualizzazione dei prodotti selezionati*/
    ObservableList<String> itemsProdottiSelezionati = FXCollections.observableArrayList();
    /*prezzo menu del giorno*/
    double prezzoTotOrdine = 0;

    final String OK = "ok";
    final String FREE = "0.0 Euro";
    final String SERV_CAMERA = "1";
    final String CAMERIERE_SERV_CAM = "103";

    /*Alert per la stampa dei messaggi di errore, di warning, di successo:
    * crea una finestra chiamata "titoloMessaggio". L'intestazione del messaggio è "HeaderMess" e
     * la descrizione è "messaggio"*/
    private void visualizzaAlert(String titoloMessaggio,String headerMess ,String messaggio){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titoloMessaggio);
        alert.setHeaderText(headerMess);
        alert.setContentText(messaggio);

        alert.showAndWait();
    }

    @FXML
    private void controllaDisponibilitàCamera(ActionEvent e){
        final String QUREY_ROOM_LIBERE = "req_room_lib";


        ArrayList<String> listaCamere = new ArrayList<String>();
        String stringa, ok, prezzoDoppia = null, prezzoSuite = null, prezzoSingola = null, prezzoTripla = null;
        int doppia = 0, singola = 0, tripla =0, suite =0; //permette di tenere traccia del numero di camere da prenotare
        String stringaDaInviare = null;
        /* Prelevo le date e compongo una stringa da inviare al server */
        LocalDate dataArrivo = datePickerArrivo.getValue();
        LocalDate dataPartenza = datePickerPartenza.getValue();

        /*Se non ho selezionato nessuna data visualizzo un messaggio di Warning*/
        if (dataArrivo == null || dataPartenza == null){
            visualizzaAlert("Warning","Controlla le date", "Date non valide. Inserisci correttamente una data di arrivo ed" +
                    "una di partenza" );
        }else{
            /*Compongo la stringa da inviare al server*/
            stringaDaInviare = QUREY_ROOM_LIBERE+"\n"+dataArrivo+"\n"+dataPartenza;

            try {
                /*Apro una connessione con il server*/
                Socket s = new Socket(HOST, Integer.parseInt(PORTA));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                /*Invio una stringa al server, esso cercherà le camere libere. Per tanto tornerà una lista di camere libere secondo la
                 * formattazione: numero,tipo,prezzo\n . Cioè ritornerà solo la tipologia delle camere libere.*/
                out.println(stringaDaInviare);

                /*Il server risponde con un ok seguito dalle info da elaborare*/
                ok = in.readLine();
                System.out.println(ok);

                /*Elaboro la risposta dal server*/
                /*Andro a settare questi elementi nella listView relativa alle camere libere*/
                stringa = in.readLine();

                while (stringa.isEmpty() != true){
                    String [] splitta = stringa.split(",");
                    listaCamere.add(splitta[0]+" "+splitta[1]+" "+splitta[2]);
                    stringa = in.readLine();
                }

                s.close(); // chiudo la connessione

              /*Conto il numero delle camere libere */
                for (int i=0; i<listaCamere.size(); i++){
                    String camera = listaCamere.get(i);
                    String [] splitta = listaCamere.get(i).split(" ");

                    if (camera.contains(DOPPIA)){
                        doppia++;
                        prezzoDoppia = splitta[2];
                    }
                    else if (camera.contains(SINGOLA)){
                        singola++;
                        prezzoSingola = splitta[2];
                    }
                    else if (camera.contains(TRIPLA)){
                        tripla++;
                        prezzoTripla = splitta[2];
                    }else{
                        suite++;
                        prezzoSuite = splitta[2];
                    }
                }


              /*Creo la lista da visualizzare nella list view*/
                ObservableList<String> items = FXCollections.observableArrayList (
                        doppia+" "+DOPPIA+"  "+prezzoDoppia+" Euro",
                        singola+" "+SINGOLA+"  "+prezzoSingola+" Euro",
                        tripla+" "+TRIPLA+"  "+prezzoTripla+" Euro",
                        suite+" "+SUITE+"  "+prezzoSuite+" Euro");

                listViewCamereCliente.setItems(items);

            } catch (IOException e1) {
                e1.printStackTrace();
                visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
            }
        }

    }

    @FXML /*quando premo il tasto "aggiungi vado ad inserire in lista camere le camere da prenotare"*/
    private void aggiungiCameraAllaPrenotazione(ActionEvent e){
        contoCamere = 0; //è una variabile globale e bisogna aggiornarla con il prezzo delle camere scelte in un periodo
        listaCamere.add(listViewCamereCliente.getSelectionModel().getSelectedItem().toString());

        /*Calcolo il prezzo totale della prenotazione*/
        //con il for sommo il costo di tutte le camere prenotate
        for (int i=0; i<listaCamere.size(); i++){
            String [] splitta = listaCamere.get(i).split(" ");
            textAreaInfoPrenotazioniCliente.setText(splitta[0]+" "+splitta[1]+"\n");
            contoCamere += Double.parseDouble(splitta[3]);
        }

        //qui il costo delle camere viene moltiplicato per il numero di giorni su cui si estende la prenotazione
        LocalDate dataArrivo = datePickerArrivo.getValue();
        LocalDate dataPartenza = datePickerPartenza.getValue();

        int nGiorni = dataPartenza.getDayOfMonth()- dataArrivo.getDayOfMonth();

        contoCamere += contoCamere*nGiorni;

        /*Vado a settare la textView del testo*/
        textFieldPrezzoCameraClienti.setText(String.valueOf(contoCamere)+" Euro");
    }

    /*visualizza semplicemente delle info relative alla camera (wifi, letto ecc..).
    * Tali info sono presenti nella parte soprastante di questo file sotto la forma di stinghe costanti.*/
    @FXML
    private void informazioniSulTipoDiCameraSelezionata (ActionEvent e){
        String [] splitta = listViewCamereCliente.getSelectionModel().getSelectedItems().toString().split(" ");

        switch (splitta[1]) {
            case DOPPIA:
                textAreaInfoCamereCliente.setText(infoDoppia);
                break;
            case SINGOLA:
                textAreaInfoCamereCliente.setText(infoSingola);
                break;
            case TRIPLA:
                textAreaInfoCamereCliente.setText(infoTripla);
                break;
            case SUITE:
                textAreaInfoCamereCliente.setText(infoSuite);
                break;
        }

    }

    /*Permette di prenotare una camera. Per farlo, invio due richieste al server:
    * - QUREY_CLIENTE_INS permette di salvare un cliente nel DB;
    * - QUERY_PRENOTAZIONE_INS permette di inserire la prenotazione nel DB;
    * Si sono effettuate due richieste poiché bisogna effettuare prima gli inserimenti nelle tabelle principali e
    * poi in quelle che fungono da relazione */
    @FXML
    private void prenotaCamera(ActionEvent e){
        final String QUREY_CLIENTE_INS = "req_cliente_ins";
        final String QUERY_PRENOTAZIONE_INS = "req_prenotazione_ins";

        String stringaDaInviare = null;
        String stringaRicevuta = null;
        String nome = textFielNomeCliente.getText();
        String cognome = textFieldCognomeCliente.getText();
        String nDoc = textFielDocumentoCliente.getText();
        String dataNascita = textFieldDataNascitaCliente.getText();

        /*Se dimentichiamo di inserire qualche campo il sistema visualizza un alert*/
        if (nome == null || cognome == null || nDoc == null || dataNascita == null || listaCamere.isEmpty() == true ){
            visualizzaAlert("Warning", "Attenzione ", " Qualche campo relativo alle info sul cliente è nullo oppure non è " +
                    "stata selezionata nessuna camera da prenotare.");
        }else{
            /*Inizio a comporre la stringa da inviare*/
            stringaDaInviare = QUREY_CLIENTE_INS+"\n"+"nome:"+nome+"\n"+"cognome:"+cognome+"\n"
                    +"nDoc:"+nDoc+"\n"+"dataNascita:"+dataNascita+"\n";

            try {
                /*Apro una connessione con il server*/
                Socket s = new Socket(HOST, Integer.parseInt(PORTA));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                /*Le date sono necessarie per calcolare il prezzo del soggiorno da inviare al Server,
                * quando viene inserito un cliente bisogna aggiornare conto con il prezzo del soggiorno*/
                LocalDate dataArrivo = datePickerArrivo.getValue();
                LocalDate dataPartenza = datePickerPartenza.getValue();

                int nGiorni = dataPartenza.getDayOfMonth()- dataArrivo.getDayOfMonth();

                if (dataArrivo == null || dataPartenza == null) {
                    visualizzaAlert("Warning", "Controlla le date", "Date non valide. Inserisci correttamente una data di arrivo ed" +
                            "una di partenza");
                }
                else {
                    String prenotazioneCamere = QUERY_PRENOTAZIONE_INS +"\n"+listaCamere.size()+"\n";

                    //Dalla lista delle camere aggiunte alla prenotazione prelevo il tipo e costruisco
                    //la stringa da inviare al server
                    for (int i=0;i<listaCamere.size();i++){
                        prenotazioneCamere += listaCamere.get(i)+"\n";
                    }

                    //Una volta aggiunte tutte le camere vado ad aggiungere le date per la prenotazione
                    prenotazioneCamere += dataArrivo+"\n"+dataPartenza;
                    stringaDaInviare += (nGiorni*contoCamere)+"\n";

                    out.println(stringaDaInviare+prenotazioneCamere); //invio la richiesta al server

                    /*Dal server mi aspetto un "ok"*/
                    stringaRicevuta = in.readLine();

                    /*Visualizzo un alert in caso di errore. Se avviene un errore nel server, esso risponderà con "er"*/
                    if(stringaRicevuta.contains(OK)){
                        visualizzaAlert("Alert", "Prenotato", "La prenotazione è andata a buon fine!");
                    }
                    else{
                        visualizzaAlert("Errore", "Errore sulla prenotazione", "La prenotazione non è andata a buon fine!");
                    }
                    //chiudo la connessione
                    s.close();

                }

            } catch (IOException e1) {
                e1.printStackTrace();
                visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
            }

        }
    }

    @FXML //premendo il bottone "apri" nell'interfaccia cliente tab "ordinazioni" chiedo al server un menu
    private void riceviMenuBar(ActionEvent e){
        final String QUERY_MENU_BAR_REQ = "req_menu_bar";
            /*Preparo una struttura che mi permetterà di visualizzare*/
        ObservableList<String> items = FXCollections.observableArrayList();
        String dalServer = null;

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out.println(QUERY_MENU_BAR_REQ);
            String ok =in.readLine(); //mi aspetto l'ok del server
            System.out.println(ok);

                /*Il server invia una stringa composta da più righe, i campi di ogni riga sono divisi da una virgola.
                * Per tanto il codice sottostante elabora la stringa traendone le info necessarie:
                * Esempio risposta del server: prodotto,prezzo\n*/
            dalServer = in.readLine();
            while (dalServer.isEmpty() != true){
                String [] splitta = dalServer.split(",");
                items.add(splitta[0]+" "+splitta[1]+" Euro"); //compongo la lista da visualizzare
                dalServer = in.readLine();
            }

            //chiudo la connessione
            s.close();

            listViewMenuBarCliente.setItems(items);//visualizzo la lista precedentemente composta

        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }

    }

    @FXML  //vedi ricevi menuBar
    private void riceviMenuRistorante(ActionEvent e){
        final String QUERY_MENU_RISTORANTE_REQ = "req_menu_rist";
        ObservableList<String> items = FXCollections.observableArrayList();
        String dalServer = null;

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out.println(QUERY_MENU_RISTORANTE_REQ);
            String ok =in.readLine();
            System.out.println(ok);
            dalServer = in.readLine();

            while (dalServer.isEmpty() != true){
                String [] splitta = dalServer.split(",");
                items.add(splitta[0]+" "+splitta[1]+" Euro");
                dalServer = in.readLine();
            }

            //chiudo la connessione
            s.close();
            listViewMenuRistoranteCliente.setItems(items);

        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }

    }

    @FXML //Bottone che aggiunge i prodotti del bar alla lista da inviare
    private void aggiungiAllaListaProdottiPerBar (ActionEvent e){
        String prodottoSelezionato = null;
        prezzoTotOrdine = 0;
        /*pulisce i prodotti già selezionati, questi sono mantenuti in un ArrayList "listaProdotti" dichiarato globale,
        * su cui si effettueranno le eventuali elaborazioni.*/
        itemsProdottiSelezionati.clear();

        if (listViewMenuBarCliente == null){ //se la list view è vuota
            visualizzaAlert("Errore", "Errore sull'aggiunta del prodotto", "Menu vuoto. Bisogna prima aprire il menu.");
        }else{ //se la list view è piena prelevo il prodotto selezionato
            prodottoSelezionato = listViewMenuBarCliente.getSelectionModel().getSelectedItem().toString();

            listaProdotti.add(prodottoSelezionato); //aggiungo un prodotto selezionato alla lista dei prodotti

            /*Vado a popolare nuovamente la listview elaborando le stringhe contenute in "listaProdotti"*/
            for (int i=0; i<listaProdotti.size();i++){
                String [] splitta = listaProdotti.get(i).split(" ");
                itemsProdottiSelezionati.add(splitta[0]+" "+splitta[1]+" Euro");
                prezzoTotOrdine += Double.parseDouble(splitta[1]);
            }
            listViewProdottiSelezionatiCliente.setItems(itemsProdottiSelezionati);
            textFieldTotaleOrdinazioneCliente.setText(String.valueOf(prezzoTotOrdine));
        }

    }

    @FXML //Bottone che aggiunge i prodotti del ristorante alla lista da inviare ---> vedi aggiungiAllaListaProdottiPerBar
    private void aggiungiAllaListaProdottiPerRistorante (ActionEvent e){
        String prodottoSelezionato = null;
        prezzoTotOrdine = 0;
        itemsProdottiSelezionati.clear();

        if (listViewMenuRistoranteCliente == null){ //se la list view è vuota
            visualizzaAlert("Errore", "Errore sull'aggiunta del prodotto", "Menu vuoto. Bisogna prima aprire il menu.");
        }else{ //se la list view è piena prelevo il prodotto selezionato
            prodottoSelezionato = listViewMenuRistoranteCliente.getSelectionModel().getSelectedItem().toString();
            listaProdotti.add(prodottoSelezionato);

            for (int i=0; i<listaProdotti.size();i++){
                String [] splitta = listaProdotti.get(i).split(" ");
                itemsProdottiSelezionati.add(splitta[0]+" "+splitta[1]+" Euro");
                prezzoTotOrdine += Double.parseDouble(splitta[1]);
            }

            listViewProdottiSelezionatiCliente.setItems(itemsProdottiSelezionati);
            textFieldTotaleOrdinazioneCliente.setText(String.valueOf(prezzoTotOrdine));
        }

    }
/*
    @FXML
    private void inviaLaListaDeiProdottiOrdinati(ActionEvent e)  {
        final String QUERY_INVIA_PRODOT = "req_ord_ins";
        /*Mando un 1 per dire che è un servizio in camera ed il 3 è il cameriere addetto al servizio in camera*/
/*        String prodottiOrdinati = QUERY_INVIA_PRODOT+"\n"+listaProdotti.size()+"\n"+"0"+"\n"+"ttextidCam"+"\n";

        if (listaProdotti.size() == 0 ){
            visualizzaAlert("Warning", "Errore sulla lista prodotti selezionati", "Non sono presenti elementi nella lista.");
        }else if (textFieldNumeroCameraCliente == null){
            visualizzaAlert("Warning", "Errore sul numero di camera", "Bisogna inserire un numero di camera!");
        }else{
            //preparo l'elenco di prodotti ordinati
            String [] splitta = listaProdotti.get(0).split(" ");
            prodottiOrdinati += splitta[0]+"\n";
            for (int i=1; i<listaProdotti.size();i++){
                splitta = listaProdotti.get(i).split(" ");
                prodottiOrdinati += splitta[0]+"\n";
            }
            System.out.print(prodottiOrdinati);
            /*invio la lista*/
 /*           try {
                Socket s = new Socket(HOST, Integer.parseInt(PORTA));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                out.println(prodottiOrdinati+0+"\n"+textFieldNumeroCameraCliente.getText()+"\n");//invio al server la lista dei prodotti ordinati

                //attendo una risposta dal server
                String rispServer =in.readLine();
                if (rispServer.contains(OK)){
                    visualizzaAlert("Alert", "Ordinazione avvenuta con successo", "I prodotti da lei ordinati saranno presto serviti.\n" +
                            "E' possibile consultare le ordinazioni effettuate nel tab 'Ordinazioni effettuate' per monitorarne lo stato.");
                }
                else{
                    visualizzaAlert("Errore", "Errore sull'ordinazione", "La procedura di invio NON è stata completata con successo!");
                }

                //chiudo la connessione
                s.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
            }


        }
    }
*/

    @FXML // Bottone che permette di inviare la lista dei prodotti selezionati al server
    private void inviaLaListaDeiProdottiOrdinatiDaCamera(ActionEvent e)  {
        final String QUERY_INVIA_PRODOT = "req_ord_ins";
        /*Mando un 1 per dire che è un servizio in camera ed il 103 è il cameriere addetto al servizio in camera.
        * Per tanto: SERV_CAMERA è una costante ad 1 e CAMERIERE_SERV_CAM è una costante a 103*/
        String prodottiOrdinati = QUERY_INVIA_PRODOT+"\n"+listaProdotti.size()+"\n"+SERV_CAMERA+"\n"+CAMERIERE_SERV_CAM+"\n";

        if (listaProdotti.size() == 0 ){ //se provo ad inviare una lista vuota esce una finestra di avvertimento
            visualizzaAlert("Warning", "Errore sulla lista prodotti selezionati", "Non sono presenti elementi nella lista.");
        }else if (textFieldNumeroCameraCliente == null){
            visualizzaAlert("Warning", "Errore sul numero di camera", "Bisogna inserire un numero di camera!");
        }else{
            /*preparo l'elenco di prodotti ordinati elaborando le stringhe, osserva che le stringhe sono divise da uno spazio.*/
            String [] splitta = listaProdotti.get(0).split(" ");
            prodottiOrdinati += splitta[0]+"\n";
            for (int i=1; i<listaProdotti.size();i++){
                splitta = listaProdotti.get(i).split(" ");
                prodottiOrdinati += splitta[0]+"\n";
            }

            /*invio la lista*/
            try {
                Socket s = new Socket(HOST, Integer.parseInt(PORTA));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                //invio al server la lista dei prodotti ordinati
                out.println(prodottiOrdinati+0+"\n"+textFieldNumeroCameraCliente.getText()+"\n");

                //attendo una risposta dal server, un ok
                String rispServer =in.readLine();
                if (rispServer.contains(OK)){
                    visualizzaAlert("Alert", "Ordinazione avvenuta con successo", "I prodotti da lei ordinati saranno presto serviti.\n" +
                            "E' possibile consultare le ordinazioni effettuate nel tab 'Ordinazioni effettuate' per monitorarne lo stato.");
                }
                else{
                    visualizzaAlert("Errore", "Errore sull'ordinazione", "La procedura di invio NON è stata completata con successo!");
                }
                //chiudo la connessione
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
            }
        }
    }

    @FXML//Bottone Apri per il menu del giorno
    private void riceviMenuDelGiorno (ActionEvent e){
        final String QUERY_MENU_GIO_REQ = "req_menu_gio";
        String menu ;
        String dalServer = null;
        prezzoTotMenuGiorno = 0;

        try {
            /*Apro una connessione con server*/
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out.println(QUERY_MENU_GIO_REQ);
            String ok = in.readLine();

            System.out.println(ok);

            /*Il menu del giorno mi viene inviato dal server come un menu normale.
            * Esempio: prodotto,prezzo\n*/
            dalServer = in.readLine();
            String [] splitta = dalServer.split(",");
            menu = splitta[0]+" "+splitta[1]+" Euro"+"\n";
            /*inizializzo il prezzo del menudelgiorno con il prezzo del primo prodotto*/
            prezzoTotMenuGiorno = Double.parseDouble(splitta[1]);
            dalServer = in.readLine();
            while (dalServer.isEmpty() != true){
                /*ad ogni iterazione incremento il prezzo del menudelgiorno con il prezzo di un prodotto*/
                splitta = dalServer.split(",");
                menu += splitta[0]+" "+splitta[1]+" Euro"+"\n";
                prezzoTotMenuGiorno += Double.parseDouble(splitta[1]);
                dalServer = in.readLine();
            }

            //chiudo la connessione
            s.close();

            /*Visualizzo una lista di prodotti con tutti i prezzi dei singoli prodotti ed un prezzo totale come ultima riga*/
            menu +="\n\n"+"Prezzo: "+prezzoTotMenuGiorno+" Euro";
            textAreaMenuDelGiorno.setText(menu);

        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }
    }

    @FXML//bottone che aggiunge il menu del giorno alla lista prodotti, vedi sopra
    private void aggiungiIlMenuDelGiorno (ActionEvent e){
        prezzoTotOrdine = 0;
        itemsProdottiSelezionati.clear();

        if (listViewMenuRistoranteCliente == null){ //se la list view è vuota
            visualizzaAlert("Errore", "Errore sull'aggiunta del prodotto", "Menu del giorno vuoto. Bisogna prima aprire il menu.");
        }else{ //se è stato aperto il menu del giorno
            listaProdotti.add("Menu_del_giorno "+prezzoTotMenuGiorno+" Euro");

            for (int i=0; i<listaProdotti.size();i++){
                String [] splitta = listaProdotti.get(i).split(" ");
                itemsProdottiSelezionati.add(splitta[0]+" "+splitta[1]+" Euro");
                prezzoTotOrdine += Double.parseDouble(splitta[1]);
            }

            listViewProdottiSelezionatiCliente.setItems(itemsProdottiSelezionati);
            textFieldTotaleOrdinazioneCliente.setText(String.valueOf(prezzoTotOrdine));
        }

    }

    @FXML//bottone che permette di eliminare un prodotto già selezionato
    private void eliminaProdottoSelezionato (ActionEvent e){
        if (listViewProdottiSelezionatiCliente == null) { //se la list view è vuota visualizzo un alert
            visualizzaAlert("Errore", "Errore sull'eliminazione di un prodotto",
                    "La lista dei prodotti selezionati è vuota oppure non è stato selezionato un prodotto.");
        }else{
            /*Prelevo la posizione del prodotto selezionato*/
            int pos = listViewProdottiSelezionatiCliente.getSelectionModel().getSelectedIndex();
            listaProdotti.remove(pos);//rimuovo il prodotto dalla listaprodotti
            itemsProdottiSelezionati.remove(pos);//rimuvo il prodotto dalla lista da visualizzare
            //inizializzo a zero il prezzo dell'ordine, lo ricalcolo successivamente sulla lista dei prodotti selezionati
            prezzoTotOrdine = 0;

            /*Vado a ricalcolare il prezzo dell'ordinazione*/
            listViewProdottiSelezionatiCliente.setItems(itemsProdottiSelezionati);
            for (int i=0; i<listaProdotti.size();i++){
                String [] splitta = listaProdotti.get(i).split(" ");
                prezzoTotOrdine += Double.parseDouble(splitta[1]);
            }

            textFieldTotaleOrdinazioneCliente.setText(String.valueOf(prezzoTotOrdine));
        }
    }

    @FXML//bottone che permette di annullare un ordine prima di inviarlo al server
    private void annullaOrdinazioneInComposizione (ActionEvent e){
        /*Setta a zero i prezzi e pulisce la lista prodotti*/
        prezzoTotOrdine = 0;
        itemsProdottiSelezionati.clear();
        listaProdotti.clear();

        listViewProdottiSelezionatiCliente.setItems(itemsProdottiSelezionati);
        textFieldTotaleOrdinazioneCliente.setText(FREE);
    }

    /*bottone che ti permette di visualizzare le info relative alle ordinazioni da camera*/
    @FXML
    private void caricaOrdiniDaCamera(ActionEvent e){
        final String QUERY_REQ_TUTTI_ORDINI = "req_ord_cliente";

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            /*Se non viene specificato il documento di un cliente parte un alert*/
            if (textFieldNumeroCameraCaricaOrdini.getText() == null){
                visualizzaAlert("Warning", "Attenzione", "Bisogna inserire un numero di documento!");
            }else{
                /*Invio la richiesta al server*/
                out.println(QUERY_REQ_TUTTI_ORDINI+"\n"+textFieldNumeroCameraCaricaOrdini.getText() +"\n");
                String ok = in.readLine(); //attendo un "ok"
                System.out.println(ok);
            }

            /*Vado ad estrapolare le informazioni relative agli ordinazioni ricevute dal server per poi visualizzarle
            * in una listView*/
            String dalServer = in.readLine();
            ObservableList<String> itemsProdottiSelezionati = FXCollections.observableArrayList();
            while(dalServer.isEmpty() != true){
                itemsProdottiSelezionati.add(dalServer);
                dalServer = in.readLine();
            }

            listViewOrdinazioniEffettuate.setItems(itemsProdottiSelezionati); //setto la listView
        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }
    }

    /*Controlli Cuoco*/
    private void caricaProssimoOrdineRistorante (ActionEvent e){
        final String QUERY_ORDINE_MAX_PRIOR = "req_ord_max_pri";

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //invio al server la richiesta dell' ordine
            out.println(QUERY_ORDINE_MAX_PRIOR);

            //attendo la risposta
            String rispServer = in.readLine();

            /*NOTA: In ordinazioni ho realizzato una funzione che ritorna l'id dell'ordine da far visualizzare al cuoco.
            * Bisogna creare in prodotto una funzione che preleva tutti i prodotti a partire dall'id associato ad un ordine*/


        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }

    }

    /*Roba Giacomo*/
    // BAR
    @FXML
    private void AggiungiBarClick(ActionEvent e){
        ObservableList<String> items = FXCollections.observableArrayList
                (menubarListView.getSelectionModel().getSelectedItem().toString());
        ProdottitavoloListView.getItems().addAll(items);
    }

    @FXML// bottone apri menu del bar ---->vedi menu precedenti
    public void ListaProdottiBar()  {
        final String QUERY_MENU_BAR_REQ = "req_menu_bar";

        try {
            Socket s = new Socket(HOST,Integer.parseInt(PORTA));
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out=new PrintWriter(s.getOutputStream(), true);
            out.println(QUERY_MENU_BAR_REQ);
            String Ok = in.readLine();
            System.out.println(Ok);
            String Stringa_Ricevuta=in.readLine();
            ObservableList<String> items = FXCollections.observableArrayList();

            while(Stringa_Ricevuta.isEmpty()!=true)
            {
                String [] split=Stringa_Ricevuta.split(",");
                items.add(split[0]+"  "+split[1]+" Euro");
                Stringa_Ricevuta=in.readLine();
            }

            menubarListView.setItems(items);
        } catch (IOException e) {
            e.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e.getMessage()+".");
        }
    }

    // RISTORANTE
    @FXML
    private void AggiungiRistoranteClick(ActionEvent e){
        ObservableList<String> items = FXCollections.observableArrayList
                (MenuristoranteListView.getSelectionModel().getSelectedItem().toString());
        ProdottitavoloListView.getItems().addAll(items);
    }

    @FXML
    public void ListaProdottiRistorante()  {
        final String QUERY_MENU_RISTORANTE_REQ = "req_menu_rist";

        try {
            Socket s = new Socket(HOST,Integer.parseInt(PORTA));
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out=new PrintWriter(s.getOutputStream(), true);
            out.println(QUERY_MENU_RISTORANTE_REQ);

            String Ok = in.readLine();
            System.out.println(Ok);

            String Stringa_Ricevuta=in.readLine();
            ObservableList<String> items = FXCollections.observableArrayList();

            while(Stringa_Ricevuta.isEmpty()!=true)
            {
                String [] split=Stringa_Ricevuta.split(",");
                items.add(split[0]+"  "+split[1]+" Euro");
                Stringa_Ricevuta=in.readLine();
            }
            MenuristoranteListView.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e.getMessage()+".");
        }


    }

    // MENU DEL GIORNO
    @FXML
    private void AggiungiMenuGiornoClick(ActionEvent e) throws IOException {
        ObservableList<String> items = FXCollections.observableArrayList(MenudelgiornoListView.getItems());
        ProdottitavoloListView.getItems().addAll(items);
    }

    @FXML
    public void ListaProdottiMenuGiorno()  {
        final String QUERY_MENU_GIO_REQ = "req_menu_gio";

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println(QUERY_MENU_GIO_REQ);
            String Ok = in.readLine();
            System.out.println(Ok);

            String Stringa_Ricevuta = in.readLine();
            ObservableList<String> items = FXCollections.observableArrayList();

            while (Stringa_Ricevuta.isEmpty() != true) {
                String[] split = Stringa_Ricevuta.split(",");
                items.add(split[0] + " " + split[1] + " Euro");
                Stringa_Ricevuta = in.readLine();
            }

            MenudelgiornoListView.setItems(items);
        } catch (IOException e) {
            e.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e.getMessage()+".");
        }

    }


    // CANCELLARE UN ELEMENTO ORDINATO
    public void CancellaProdottoOrdinato(){
        ObservableList<String> items = FXCollections.observableArrayList(ProdottitavoloListView.getSelectionModel().getSelectedItem().toString());
        ProdottitavoloListView.getItems().remove(ProdottitavoloListView.getSelectionModel().getSelectedItem().toString());
    }


    // INVIA L'ORDINE
    public void InviaOrdineClick(){
        ObservableList<String> items = FXCollections.observableArrayList(ProdottitavoloListView.getItems());
        //  OrdinazionifatteListView.getItems().addAll(items);
        ProdottitavoloListView.getItems().removeAll(items);
        final String QUERY_INVIA_PRODOT = "req_ord_ins";
        /*Mando un 1 per dire che è un servizio in camera ed il 3 è il cameriere addetto al servizio in camera*/
        String prodottiOrdinati = QUERY_INVIA_PRODOT+"\n"+items.size()+"\n"+"0"+"\n" + idCameriereTextField.getText()+ "\n";
        if (items.size() == 0 ){
            visualizzaAlert("Warning", "Errore sulla lista prodotti selezionati", "Non sono presenti elementi nella lista.");
        }else if (NcameraTextField == null){
            visualizzaAlert("Warning", "Errore sul numero di camera", "Bisogna inserire un numero di camera!");
        }else{
            //preparo l'elenco di prodotti ordinati
            System.out.println(prodottiOrdinati);
            String [] splitta = items.get(0).split(" ");
            prodottiOrdinati += splitta[0]+"\n";
            for (int i=1; i<items.size();i++){
                splitta = items.get(i).split(" ");
                prodottiOrdinati += splitta[0]+"\n";
            }
            System.out.print(prodottiOrdinati);
            /*invio la lista*/
            try {
                Socket s = new Socket(HOST, Integer.parseInt(PORTA));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                out.println(prodottiOrdinati + NtavoloTextField.getText()+"\n"+NcameraTextField.getText()+ "\n");//invio al server la lista dei prodotti ordinati
                System.out.print(prodottiOrdinati+ NtavoloTextField.getText()+"\n"+NcameraTextField.getText()+ "\n");
                //attendo una risposta dal server
                String rispServer =in.readLine();
                if (rispServer.contains(OK)){
                    visualizzaAlert("Alert", "Ordinazione avvenuta con successo", "I prodotti da lei ordinati saranno presto serviti.\n" +
                            "E' possibile consultare le ordinazioni effettuate nel tab 'Ordinazioni effettuate' per monitorarne lo stato.");
                }
                else{
                    visualizzaAlert("Errore", "Errore sull'ordinazione", "La procedura di invio NON è stata completata con successo!");
                }

                //chiudo la connessione
                s.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
            }
        }
    }


    public void AnnullaOrdineButtonClick(){
        ObservableList<String> items = FXCollections.observableArrayList
                (OrdinazionifatteListView.getSelectionModel().getSelectedItem().toString());
        OrdinazionifatteListView.getItems().remove(OrdinazionifatteListView.getSelectionModel().getSelectedItem().toString());
    }

    /*FINE CODICE GIACOMO*/

    @FXML //permette al camerier di annullare un ordine se esso è in stato di "attesa"
    public void annullaOrdineCameriere(ActionEvent e){
        final String QUERY_ANNULLA_ORD = "req_ord_del";
        /*Prelevo l'ordine selezionato: l'obiettivo è ricavare l'id e lo stato */
        String [] ordine= OrdinazionifatteListView.getSelectionModel().getSelectedItem().toString().split(" ");
        String [] id = ordine[0].split(":"); //ricavo l'id
        String [] stato = ordine[3].split(":"); //ricavo lo stato

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            /*Invio il comando, l'id sell'ordine e lo stato al server*/
            System.out.println(ordine[1]);
            out.println(QUERY_ANNULLA_ORD+"\n"+id[1]+"\n"+stato[1]);
            /*Attendo una risposta dal server*/
            String ok = in.readLine();

            /*Se risponde con "ok" è andato a buon fine*/
            if (ok.contains(OK) == true){
                visualizzaAlert("Warning", "Ordine eliminato", "L'eliminazione dell'ordine è avvenuta con successo!");
            }else{
                visualizzaAlert("Errore", "Errore sull'eliminazione dell'ordine selezionato", "Ordine NON eliminato, controlla" +
                        "se lo stato dell'ordine è 'attesa'.");
            }

            OrdinazionifatteListView.getItems().remove(OrdinazionifatteListView.getSelectionModel().getSelectedIndex());

        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }
    }

    @FXML //permette ad un cliente di annullare l'ordinazione in camera se lo stato è "attesa"
    public void annullaOrdineInCamera(ActionEvent e){
        final String QUERY_ANNULLA_ORD = "req_ord_del";
        /*Prelevo l'ordine selezionato: l'obiettivo è ricavare l'id e lo stato */
        String [] ordine= listViewOrdinazioniEffettuate.getSelectionModel().getSelectedItem().toString().split(" ");
        String [] id = ordine[0].split(":");
        String [] stato = ordine[2].split(":");

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            /*Invio il comando, l'id sell'ordine e lo stato al server*/
            System.out.println(ordine[1]);
            out.println(QUERY_ANNULLA_ORD+"\n"+id[1]+"\n"+stato[1]);
            /*Attendo una risposta dal server*/
            String ok = in.readLine();

            if (ok.contains(OK) == true){
                visualizzaAlert("Warning", "Ordine eliminato", "L'eliminazione dell'ordine è avvenuta con successo!");
            }else{
                visualizzaAlert("Errore", "Errore sull'eliminazione dell'ordine selezionato", "Ordine NON eliminato, controlla" +
                        "se lo stato dell'ordine è 'attesa'.");
            }

            listViewOrdinazioniEffettuate.getItems().remove(listViewOrdinazioniEffettuate.getSelectionModel().getSelectedIndex());

        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }
    }

    @FXML //bottone che chiede le ordinazioni effettuate da parte di un cameriere
    private void caricaOrdini (ActionEvent e){
        final String QUERY_REQ_ORDINI = "req_ord_cameriere";

        try {
            Socket s = new Socket(HOST, Integer.parseInt(PORTA));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            if (textFieldIdCameriereCaricaOrdini.getText() == null){
                visualizzaAlert("Warning", "Attenzione", "Bisogna inserire il numero del cameriere!");
            }else{
                System.out.println(QUERY_REQ_ORDINI+"\n"+textFieldIdCameriereCaricaOrdini.getText() +"\n");
                out.println(QUERY_REQ_ORDINI+"\n"+textFieldIdCameriereCaricaOrdini.getText() +"\n");
                String ok = in.readLine();
                System.out.println(ok);
            }

            String dalServer = in.readLine();
            ObservableList<String> itemsProdottiSelezionati = FXCollections.observableArrayList();

            while(dalServer.isEmpty() != true){
                itemsProdottiSelezionati.add(dalServer);
                dalServer = in.readLine();
            }

            OrdinazionifatteListView.setItems(itemsProdottiSelezionati);

        } catch (IOException e1) {
            e1.printStackTrace();
            visualizzaAlert("Errore", "Errore invio informazioni al Server", "Ci sono problemi sulla socket:\n"+e1.getMessage()+".");
        }

    }
}
