package it.albergodeifiori.project.dao;

        import it.albergodeifiori.project.entity.Cliente;
        import it.albergodeifiori.project.entity.PersonalePulizie;

        import java.util.List;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * * il DAO (Data Access Object) è un pattern architetturale per la gestione della persistenza: si tratta
 * fondamentalmente di una classe con relativi metodi che rappresenta un'entità tabellare di un DB.
 */

/**
 * Qui trovo tutte le azioni che svolgerà la classe PersonalePulizie.
 */
public interface PersonalePulizieDAO {
    public List<PersonalePulizie> getAllPersonalePulizie() throws DAOException;
    public PersonalePulizie getAddetto(int cod) throws DAOException;
    public List<PersonalePulizie> getAddetti(PersonalePulizie a) throws DAOException;
    public void insertPersonalePulizie(PersonalePulizie a) throws DAOException;
    public void deletePersonalePulizie(int a) throws DAOException;
}
