package Carmine.ProgettoMD.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

import Carmine.ProgettoMD.DAO.AttivitàDAOIMPL;
import Carmine.ProgettoMD.Model.Allegato;
import Carmine.ProgettoMD.Model.Attività;
import Carmine.ProgettoMD.Model.Ruolo;
import Carmine.ProgettoMD.Model.StatoAttività;
import Carmine.ProgettoMD.Model.Struttura;
import Carmine.ProgettoMD.Model.Utente;

/**
 * 
 */
public class GestioneAttivitàService {

	AttivitàDAOIMPL attDao;
	
	
    /**
     * Default constructor
     */
    public GestioneAttivitàService() {
    	
    	attDao = new AttivitàDAOIMPL();

    }


    /**
     * @param dati
     * @param fileAllegati 
     * @param autore 
     * @return
     */
    public boolean creaBozza(Attività dati, List<File> fileAllegati, Utente autore) {
        dati.setStato(StatoAttività.BOZZA);
        dati.setPromotore(autore);

        if (fileAllegati != null && !fileAllegati.isEmpty()) {

            // rimuovo tutti i caratteri non validi per i nomi di cartella
            String titoloSicuro = dati.getTitolo().replaceAll("[^a-zA-Z0-9_\\-]", "_"); // sostituisce tutto ciò che non è lettera, numero, - o  con _

            String percorsoCartella = "uploads/" + autore.getCognome() + "/" + titoloSicuro + "/";
            //al posto della connessione ftp per caricare sul server simulo con un salvataggio in locale
            File cartella = new File(percorsoCartella);
            if (!cartella.exists()) {
                cartella.mkdirs();
            }

            for (File file : fileAllegati) {

                File destinazione = new File(percorsoCartella + file.getName());
                try {
                    Files.copy(file.toPath(), destinazione.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println("Errore salvataggio file: " + file.getName());
                    continue;
                }

                //aggiungo a lista che poi salvo su db
                Allegato a = new Allegato();
                a.setNomeOriginale(file.getName());
                a.setPercorsoServer(percorsoCartella + file.getName());
                a.setDimensione((int) file.length());
                dati.addAllegato(a);
            }
        }
        
        return attDao.create(dati);
    }

    /**
     * @param attivita
     */
    public void eliminaAttività(Attività attivita) {
        // TODO implement here
    }

    /**
     * @param autore 
     * @return
     */
    public List<Attività> getAttivitàPerAutore(Utente autore) {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public Attività getAttivitàById(int id) {
        return attDao.findById(id);
    }
    /**
     * @param attività 
     * @param nuoviFile 
     * @param allegatiDaRimuovere
     */
    /*@  requires attivita != null && attivita.getStato() == StatoAttività.BOZZA @*/
    public void modificaAttività(Attività attività, List<File> nuoviFile, List<Integer> allegatiDaRimuovere) {
        // TODO implement here
    }

    /**
     * @param utente 
     * @param ruoloAttivo 
     * @param anno 
     * @param titolo 
     * @param stato
     * @return 
     */
    public List<Attività> getAttivitàPerRuolo(Utente utente, Ruolo ruoloAttivo, Integer anno, String titolo, List<StatoAttività> stato) {

        // calcolo anno corrente se non passato 
        int annoCorrente = (anno != null) ? anno : LocalDate.now().getYear();

        if (ruoloAttivo == Ruolo.DOCENTE) {
            // vede solo le sue attività e di base dell'anno corrente in caso dovrebbe fare ricerca per ottenrne altre 
            return attDao.findByAutore(utente, annoCorrente, titolo, stato);

        } else if (ruoloAttivo == Ruolo.RESPONSABILE) {
            // vede le attività della sua struttura con stati INVIATA, APPROVATA, VALIDATA
            Struttura struttura = utente.getStruttura();
            List<StatoAttività> statiResp = (stato != null && !stato.isEmpty()) 
                ? stato 
                : List.of(StatoAttività.INVIATA, StatoAttività.APPROVATA, StatoAttività.VALIDATA);
            return attDao.findFilterd(struttura.getNome(), statiResp, annoCorrente, titolo, null);

        } else if (ruoloAttivo == Ruolo.AMMINISTRATORE) {
            // vede tutte le attività APPROVATE e VALIDATE
            List<StatoAttività> statiAdmin = (stato != null && !stato.isEmpty())
                ? stato
                : List.of(StatoAttività.APPROVATA, StatoAttività.VALIDATA);
            return attDao.findFilterd(null, statiAdmin, annoCorrente, titolo, null);
        }

        return null;
    }
    
    public boolean validaFile(File file) {
        // 1. null check
        if (file == null || !file.exists()) {
            System.out.println("File nullo o inesistente");
            return false;
        }

        // 2. dimensione massima 5MB
        long maxDimensione = 5 * 1024 * 1024; // 5MB in bytes
        if (file.length() > maxDimensione) {
            System.out.println("File troppo grande: " + file.getName());
            return false;
        }

        // 3. controllo estensione
        String nome = file.getName().toLowerCase();
        if (!nome.endsWith(".pdf") && !nome.endsWith(".jpg") && !nome.endsWith(".png")) {
            System.out.println("Formato non ammesso: " + file.getName());
            return false;
        }

        // 4. simulazione antivirus
        if (!scansiona(file)) {
            System.out.println("File non superato scansione antivirus: " + file.getName());
            return false;
        }

        return true;
    }

    private boolean scansiona(File file) {
        //simula la scansione del file
        System.out.println("Scansione antivirus simulata per: " + file.getName());
        return true;
    }

}