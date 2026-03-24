package Carmine.ProgettoMD.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class Attività {

    //  CHIAVE PRIMARIA 
    private int id;

    //  ATTRIBUTI BASE 
    private String titolo;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private StatoAttività stato; 
    private String noteRifiuto;

    
    // ASSOCIAZIONE 
    private Utente promotore;

    // COMPOSIZIONI FORTI 
    private List<Allegato> allegati;
    private List<Feedback> feedbackList;

   //@JML
   //@ invariant dataFine == null || dataInizio == null || dataFine.compareTo(dataInizio) >= 0;
    
    //COSTRUTTORE DI DEFAULT
    public Attività() {
        this.allegati = new ArrayList<Allegato>();
        this.feedbackList = new ArrayList<Feedback>();
    }

    public Attività(String titolo, String descrizione, LocalDate dataInizio, LocalDate dataFine, StatoAttività stato, Utente promotore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.stato = stato;
        this.promotore = promotore;
        

        
        this.allegati = new ArrayList<Allegato>();
        this.feedbackList = new ArrayList<Feedback>();
    }
    
    
    
    //GESTIONE DELLE COMPOSIZIONI FORTI
    public void addAllegato(Allegato a) {
        if (a != null && !this.allegati.contains(a)) {
            this.allegati.add(a);
        }
    }

    //lo cerco dentro nella lista e se lo trovo restituisco 
    public Allegato rimuoviAllegato(int idAllegato) {
        
        for (Allegato a : this.allegati) {
            
            if (a.getId() == idAllegato) {
                this.allegati.remove(a); 
                return a;                
            }
        }
        
        // Se il ciclo finisce senza aver trovato nulla, restituiamo null
        return null; 
    }

    public void addFeedback(Feedback f) {
        if (f != null && !this.feedbackList.contains(f)) {
            this.feedbackList.add(f);
            f.setAttività(this);
        }
    }

    public void approva() {  	
        this.stato = StatoAttività.APPROVATA; 
    }

    public void rifiuta(String motivazione) {
        this.stato = StatoAttività.BOZZA;
        this.noteRifiuto = motivazione;
    }

    public void valida() {
        this.stato = StatoAttività.VALIDATA;
    }

    public void invia() {
        this.stato = StatoAttività.INVIATA; 
    }


    public int calcolaDurata() {
        if (dataInizio != null && dataFine != null) {
            return (int) ChronoUnit.DAYS.between(dataInizio, dataFine);
        }
        return 0;
    }

    // --- GETTER E SETTER ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public StatoAttività getStato() { return stato; } // Getter con l'accento!
    public void setStato(StatoAttività stato) { this.stato = stato; } // Setter con l'accento!

    public String getNoteRifiuto() { return noteRifiuto; }
    public void setNoteRifiuto(String noteRifiuto) { this.noteRifiuto = noteRifiuto; }

    public Utente getPromotore() { return promotore; }
    public void setPromotore(Utente promotore) { this.promotore = promotore; }

    public List<Allegato> getAllegati() { return allegati; }
    public void setAllegati(List<Allegato> allegati) { this.allegati = allegati; }

    public List<Feedback> getFeedbackList() { return feedbackList; }
    public void setFeedbackList(List<Feedback> feedbackList) { this.feedbackList = feedbackList; }
}