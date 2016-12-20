package it.albergodeifiori.project.dao;

import it.albergodeifiori.project.entity.Cliente;
import it.albergodeifiori.project.entity.Room;
import javafx.scene.Camera;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * il DAO (Data Access Object) è un pattern architetturale per la gestione della persistenza: si tratta
 * fondamentalmente di una classe con relativi metodi che rappresenta un'entità tabellare di un DB.
 */

/**
 * Qui trovo tutte le azioni che svolgerà la classe Room.
 */
public interface CameraDAO {
    public List<Room> getAllCamera() throws DAOException;
    public Room getCamera(int num) throws DAOException;
    public List<Room> getCamera(Room a) throws DAOException;
    public void updateCamera(Room a, int num) throws DAOException;
    public void insertCamera(Room a) throws DAOException;
    public void deleteCamera(int a) throws DAOException;
    public Cliente getClienteDaNumeroCamera(int nCamera);
    public int setAccess(int access, int id) throws DAOException;
    public ArrayList<Room> getCamereLibereDB(Date dataArrivoDesiderata, Date dataPartenzaDesiderata)throws DAOException;
    public ArrayList<Room> getCamereLibereTipoDB(String tipo,Date dataArrivoDesiderata, Date dataPartenzaDesiderata);
}
