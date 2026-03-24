package Carmine.ProgettoMD.Model;
import java.time.LocalDate;

public class Incarico {

    // 1. I RIFERIMENTI 
    private Utente utente;
    private Struttura struttura;

    // 2. GLI ATTRIBUTI PROPRI 
    private LocalDate dataInizio;
    private LocalDate dataFine;


    public Incarico() {
    }

    /**
     * Costruttore completo
     */
    public Incarico(Utente utente, Struttura struttura, LocalDate dataInizio, LocalDate dataFine) {
        this.utente = utente;
        this.struttura = struttura;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    // --- GETTER E SETTER ---

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Struttura getStruttura() {
        return struttura;
    }

    public void setStruttura(Struttura struttura) {
        this.struttura = struttura;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }
}