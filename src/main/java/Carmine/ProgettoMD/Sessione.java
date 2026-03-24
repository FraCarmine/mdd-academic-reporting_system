package Carmine.ProgettoMD;

import Carmine.ProgettoMD.Model.Ruolo;
import Carmine.ProgettoMD.Model.Utente;

public class Sessione {

    // unica istanza
    private static Sessione istanza;

    // variabili della sessione
    private Utente utente;
    private Ruolo ruoloAttivo;

    
    //costruttore privato
    private Sessione() {
        this.utente = null;
        this.ruoloAttivo = null;
    }

   
    public static Sessione getInstance() {
        if (istanza == null) {
            istanza = new Sessione();
        }
        return istanza;
    }

    
    //loggo l'utente
    public void login(Utente utente, Ruolo ruoloAtt) {
        this.utente = utente;
        this.ruoloAttivo = ruoloAtt;
    }

    //pulisco 
    public void logout() {
        this.utente = null;
        this.ruoloAttivo = null;
    }

    
    //cambio il ruolo attivo senza fare il logout e login
    public void cambiaRuolo(Ruolo nuovoRuolo) {
        if (this.utente != null && this.utente.getRuoliAssegnati().contains(nuovoRuolo)) {
            this.ruoloAttivo = nuovoRuolo;
        } else {
            System.out.println("Errore: L'utente non possiede questo ruolo.");
           //@TODO una eccezione per la GUI
        }
    }


    // --- GETTER ---

    public Utente getUtente() {
        return this.utente;
    }

    public Ruolo getRuoloAttivo() {
        return this.ruoloAttivo;
    }
}