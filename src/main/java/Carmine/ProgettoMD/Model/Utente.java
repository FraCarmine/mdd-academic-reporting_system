package Carmine.ProgettoMD.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utente {

    // CHIAVE PRIMARIA 
    private String matricola;

    // attributi
    private String nome;
    private String cognome; 
    private String email;
    private String password;

    // ASSOCIAZIONI 
    private Struttura struttura; 
    private List<Ruolo> ruoliAssegnati;
    private Set<Report> reportStilati; 
    
    
     //Costruttore di default
     
    public Utente() {
        // init delle collezzioni
        this.ruoliAssegnati = new ArrayList<Ruolo>();
        this.reportStilati = new HashSet<Report>();
    }

  
    public Utente(String matricola, String nome, String cognome, String email, String password, Struttura struttura) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.struttura = struttura;
        
        this.ruoliAssegnati = new ArrayList<Ruolo>();
        this.reportStilati = new HashSet<Report>();
    }


    public void addRuolo(Ruolo ruolo) {
        if (ruolo != null && !this.ruoliAssegnati.contains(ruolo)) {
            this.ruoliAssegnati.add(ruolo);
        }
    }

    public void removeRuolo(Ruolo ruolo) {
        if (ruolo != null) {
            this.ruoliAssegnati.remove(ruolo);
        }
    }

    public String getMatricola() { return matricola; }
    public void setMatricola(String matricola) { this.matricola = matricola; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Struttura getStruttura() { return struttura; }
    public void setStruttura(Struttura struttura) { this.struttura = struttura; }

    public List<Ruolo> getRuoliAssegnati() { return ruoliAssegnati; }
    public void setRuoliAssegnati(List<Ruolo> ruoliAssegnati) { this.ruoliAssegnati = ruoliAssegnati; }

    public Set<Report> getReportStilati() { return reportStilati; }
    public void setReportStilati(Set<Report> reportStilati) { this.reportStilati = reportStilati; }
}