package Carmine.ProgettoMD.Model;

import java.time.LocalDate;

/**
 * 
 */
public class ProgettoInnovazione extends Attività {

    private String partnerAziendale;
	
 
	public ProgettoInnovazione() {
        super(); // Chiama il costruttore vuoto di Attività (che prepara le liste!)
    }

	
	//COSTRUTTORE COMPLETO
    public ProgettoInnovazione(String titolo, String descrizione, LocalDate dataInizio, LocalDate dataFine, StatoAttività stato, Utente promotore, String partnerAziendale) {

    	//costruttore completo del padre
        super(titolo, descrizione, dataInizio, dataFine, stato, promotore);
        this.partnerAziendale = partnerAziendale;
    }
    

    //GETTER E SETTER
    public String getPartnerAziendale() { 
        return partnerAziendale; 
    }
    
    public void setPartnerAziendale(String partnerAziendale) { 
        this.partnerAziendale = partnerAziendale; 
    }
}