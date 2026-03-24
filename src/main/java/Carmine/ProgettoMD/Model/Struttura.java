package Carmine.ProgettoMD.Model;

import java.util.ArrayList;
import java.util.List;

public class Struttura {

    private String nome;

    private Ateneo ateneo;

    private List<Utente> responsabili;
    private List<Utente> membri;

    public Struttura() {
        this.responsabili = new ArrayList<Utente>();
        this.membri = new ArrayList<Utente>();
    }

    public Struttura(String nome) {
        this.nome = nome;
        this.responsabili = new ArrayList<Utente>();
        this.membri = new ArrayList<Utente>();
    }


    // --- GETTER E SETTER ---
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Ateneo getAteneo() { return ateneo; }
    public void setAteneo(Ateneo ateneo) { this.ateneo = ateneo; }

    public List<Utente> getResponsabili() { return responsabili; }
    public void setResponsabili(List<Utente> responsabili) { this.responsabili = responsabili; }
    
}