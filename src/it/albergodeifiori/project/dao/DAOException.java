package it.albergodeifiori.project.dao;

/**
 * Created by Stefano Raimondo on 08/12/2016.
 * Questa classe permette di gestire le eccezioni che si verificano mandando a video un messaggio scelto
 * in modo opportuno dal programmatore.
 */
    public class DAOException extends Exception{
        public DAOException(String message){
            super(message);
        }
    }


