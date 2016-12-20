package it.albergodeifiori.project.entity;

import it.albergodeifiori.project.dao.CameraDAO;
import it.albergodeifiori.project.dao.CameraDaoImpl;
import it.albergodeifiori.project.dao.DAOException;

import java.util.ArrayList;

/**
 * Created by Domenico on 12/12/2016.
 */
public class Room {

    /**
     * Vado ad inizializzare tuttii gli elementi che contenuti nella nostra classe.
     */
    private int idCamera ; //numero della camera e chiave primaria della tabella camere
    private String tipo;
    private double prezzo;
    private int stato;
    private int accessibilita;
    private int Addetto_pulizia_idaddetto_pulizia;

    /**
     * Si crea il costruttore.
     */

    public Room(int idCamera, String tipo, double prezzo, int stato, int accessibilita, int addetto_pulizia_idaddetto_pulizia)
    {
        this.idCamera = idCamera;
        this.tipo = tipo;
        this.prezzo = prezzo;
        this.stato = stato;
        this.accessibilita = accessibilita;
        this.Addetto_pulizia_idaddetto_pulizia = addetto_pulizia_idaddetto_pulizia;
    }

    /**
     * Vado ad implementare i metodi get e set che sono importantissimi per l' utilizzo della classe, poichè mi
     * gli elementi che compongono la nostra istanza.
     */

    //Metodo get servono per restituire il valore dal campo di interesse
    public int getIdCamera() {return idCamera;}

    //Metodi set servono associare nuovi valori ai campi di interesse
    public void setIdCamera(int idCamera) {this.idCamera = idCamera;}

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getAccessibilita(){ return accessibilita;}

    public void setAccessibilita(int accessibilita) {

        this.accessibilita = accessibilita;
    }
    public void setAccessibilitàDB(int access, int id) throws DAOException {
        this.accessibilita=CameraDaoImpl.getInstance().setAccess(access , idCamera);
    }

    public int getAddetto_pulizia_idaddetto_pulizia() {
        return Addetto_pulizia_idaddetto_pulizia;
    }

    public void setAddetto_pulizia_idaddetto_pulizia(int addetto_pulizia_idaddetto_pulizia) {
        Addetto_pulizia_idaddetto_pulizia = addetto_pulizia_idaddetto_pulizia;
    }
}
