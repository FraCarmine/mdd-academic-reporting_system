package Carmine.ProgettoMD.Service;

import Carmine.ProgettoMD.Model.*;
import java.io.*;
import java.util.*;

/**
 * 
 */
public class WorkflowService {

    /**
     * Default constructor
     */
    public WorkflowService() {
    }



    /**
     * @param attivitàId
     */
    public void inviaAttività(int attivitàId) {
        // TODO implement here
    }

    /**
     * @param attivitàId 
     * @param responsabile
     */
    public void approvaAttività(int attivitàId, Utente responsabile) {
        // TODO implement here
    }

    /**
     * @param attivitàId 
     * @param autoreRifiuto 
     * @param motivazione
     */
    public void rifiutaAttività(int attivitàId, Utente autoreRifiuto, String motivazione) {
        // TODO implement here
    }

    /**
     * @param attivitàId 
     * @param admin
     */
    public void validaAttività(int attivitàId, Utente admin) {
        // TODO implement here
    }

}