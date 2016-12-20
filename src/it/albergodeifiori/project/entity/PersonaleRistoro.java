package it.albergodeifiori.project.entity;


import java.util.ArrayList;
/**
 * Created by Giacomo on 7/12/2016.
 * Si va a creare una classe inserente al personale ristoro
 */
public class PersonaleRistoro {

    public String nome;
    public String cognome;
    public int codDipRist;
    public String ruolo;

    public PersonaleRistoro(){}

    //Costruttori
    public PersonaleRistoro(int idCameriere){
        nome = null;
        cognome = null;
        codDipRist = idCameriere;
        ruolo = null;
    }

    //Metodo get servono per restituire il valore dal campo di interesse
    public String getNome() {
        return nome;
    }

    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }


    public  PersonaleRistoro(  int codDipRist2,String nome2,  String cognome2,   String ruolo2) {
        //Costruttore parametrizzato; inizializza tutti i campi ad i parametri passati
        this.nome=nome2;
        this.cognome=cognome2;
        this.codDipRist=codDipRist2;
        this.ruolo=ruolo2;
    }

    public void setCodDipRist(int codice ) {
        this.codDipRist=codice;
    }

    public int getCodDipRist () {
        return codDipRist;
    }

}






