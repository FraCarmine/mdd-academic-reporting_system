package Carmine.ProgettoMD.Model;

import java.time.LocalDate;

public class PatrimonioCulturale extends Attività {

    private String luogoEsposizione;

    //COSTRUTTORE SEMPLICE
    public PatrimonioCulturale() {
        super(); // Chiama il costruttore semplice di attività
    }

    //COSTRUTTORE COMPLETO
    public PatrimonioCulturale(String titolo, String descrizione, LocalDate dataInizio, LocalDate dataFine, StatoAttività stato, Utente promotore, String luogoEsposizione) {

    	//costruttore completo del padre
        super(titolo, descrizione, dataInizio, dataFine, stato, promotore);
        this.luogoEsposizione = luogoEsposizione;
    }

    //GETTER E SETTER 
    public String getLuogoEsposizione() { return luogoEsposizione; }
    public void setLuogoEsposizione(String luogoEsposizione) { this.luogoEsposizione = luogoEsposizione; }
}