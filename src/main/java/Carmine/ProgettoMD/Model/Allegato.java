package Carmine.ProgettoMD.Model;

public class Allegato {

    private int id;

    // --- ATTRIBUTI BASE ---
    private String nomeOriginale;
    private String percorsoServer;
    private int dimensione;

    //COMPOSIZIONE FORTE CON ATTIVITà
    private Attività attivita;

    //COSTRUTTORE DEFAULT
    public Allegato() {
    }

   //COSTRUTTORE CHE RICHIEDE UN ATTIVITà 
    public Allegato(String nomeOriginale, String percorsoServer, int dimensione, Attività attivita) {
        this.nomeOriginale = nomeOriginale;
        this.percorsoServer = percorsoServer;
        this.dimensione = dimensione;
        this.attivita = attivita;
    }

    //COSTRUTTORE DAO
    public Allegato(int id, String nomeOriginale, String percorsoServer, int dimensione, Attività attivita) {
        this.id = id;
        this.nomeOriginale = nomeOriginale;
        this.percorsoServer = percorsoServer;
        this.dimensione = dimensione;
        this.attivita = attivita;
    }

    // --- GETTER E SETTER ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeOriginale() { return nomeOriginale; }
    public void setNomeOriginale(String nomeOriginale) { this.nomeOriginale = nomeOriginale; }

    public String getPercorsoServer() { return percorsoServer; }
    public void setPercorsoServer(String percorsoServer) { this.percorsoServer = percorsoServer; }

    public int getDimensione() { return dimensione; }
    public void setDimensione(int dimensione) { this.dimensione = dimensione; }

    // Getter e Setter per il legame con l'Attività
    public Attività getAttività() { return attivita; }
    public void setAttività(Attività attivita) { this.attivita = attivita; }
}