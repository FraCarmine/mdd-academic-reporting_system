package Carmine.ProgettoMD.Model;

import java.time.LocalDate;

/**
 * 
 */
public class PublicEngagement extends Attività {
	private int numeroPartecipanti;
    
	
	/**
     * Default constructor
     */
    public PublicEngagement() {
    	super();
    }

  //COSTRUTTORE COMPLETO
    public PublicEngagement(String titolo, String descrizione, LocalDate dataInizio, LocalDate dataFine, StatoAttività stato, Utente promotore, int numeroPartecipanti) {
    	//costruttore completo del padre
        super(titolo, descrizione, dataInizio, dataFine, stato, promotore);
    	this.numeroPartecipanti= numeroPartecipanti;
    }

    
    
    //GETTER E SETTERs
	public int getNumeroPartecipanti() {
		return numeroPartecipanti;
	}

	public void setNumeroPartecipanti(int numeroPartecipanti) {
		this.numeroPartecipanti = numeroPartecipanti;
	}
    

}