package Carmine.ProgettoMD.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Feedback {

   
    private int id;

    // --- ATTRIBUTI BASE ---
    private LocalDateTime timeStamp; 
    private String testoNote;
    private boolean esito;       

    // IL LEGAME DI COMPOSIZIONE FORTE 
    private Attività attivita;


    public Feedback() {
    }

    //COSTRUTTORE PER UN NUOVO CON ID GENERATO DA DB
    public Feedback(LocalDateTime timeStamp, String testoNote, boolean esito, Attività attivita) {
        this.timeStamp = timeStamp;
        this.testoNote = testoNote;
        this.esito = esito;
        this.attivita = attivita;
    }

    
    public Feedback(int id, LocalDateTime timeStamp, String testoNote, boolean esito, Attività attivita) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.testoNote = testoNote;
        this.esito = esito;
        this.attivita = attivita;
    }

    // --- GETTER E SETTER ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public String getTestoNote() { return testoNote; }
    public void setTestoNote(String testoNote) { this.testoNote = testoNote; }

    public boolean isEsito() { return esito; } // Nota: per i boolean si usa 'is' invece di 'get'
    public void setEsito(boolean esito) { this.esito = esito; }

    public Attività getAttività() { return attivita; }
    public void setAttività(Attività attivita) { this.attivita = attivita; }
}