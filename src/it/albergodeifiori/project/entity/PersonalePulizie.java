package it.albergodeifiori.project.entity;

import it.albergodeifiori.project.dao.CameraDaoImpl;
import it.albergodeifiori.project.dao.DAOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PersonalePulizie {

    final public static String MESS_ERRORE_ORA = "Error Calendar: non è possibile specificare il turno";

    private int codDipPul;
    private String nome;
    private String cognome;
    private int[] listaCamereSegnalate;


    //public ArrayList<Camere>  Pulisce; è la relazione di personale pulizia per camera. Non so a cosa serve. Vedere successivamente

    //COSTRUTTORI

    public void PersonalePulizie() {
        this.codDipPul=0;
        this.nome="";
        this.cognome="";
        listaCamereSegnalate=new int[0];
    }

    public  PersonalePulizie( int id,  String n,  String c) {
        this.codDipPul=id;
        this.nome=n;
        this.cognome=c;
        listaCamereSegnalate=new int[0];
    }
    //segnala la camera da pulire, in base al fatto se la camera è occupata o meno

    public void segnalaCamera() {
        String turno = null;
        Calendar c = Calendar.getInstance();
        List<Room> c1;
        int ora = c.get(Calendar.HOUR_OF_DAY);
        if (ora != 0) {
            if (ora >= 9 && ora <= 13)
                turno = "mattina";
            else if (ora >= 14 && ora <= 19)
                turno = "pomeriggio";

            try {
                c1 = CameraDaoImpl.getInstance().getAllCamera();//ho istanziato un oggetto camere
                for (int i = 0; i < c1.size(); i++) {
                    int access = c1.get(i).getAccessibilita();
                    if (access==1 && turno == "mattina") {
                        listaCamereSegnalate[i] = c1.get(i).getIdCamera();
                        c1.get(i).setAccessibilitàDB(0, c1.get(i).getIdCamera());

                    } else if (access==1 && turno == "pomeriggio") {
                        listaCamereSegnalate[i] = c1.get(i).getIdCamera();
                        c1.get(i).setAccessibilitàDB(0, c1.get(i).getIdCamera());
                    }
                }
            } catch (DAOException e) {
                e.getMessage();
            }
        }
    }

    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setCodDipPul( int idPersonalePul) {
        codDipPul=idPersonalePul;
    }

    //Metodo get servono per restituire il valore dal campo di interesse
    public int getCodDipPul() {
        return codDipPul;
    }

    public void setNome( String n) {
        nome=n;
    }

    public String getNome() {return nome;}

    public void setCognome( String c) {cognome=c;}

    public String getCognome() { return cognome;}


}