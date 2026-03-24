package Carmine.ProgettoMD.Controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Carmine.ProgettoMD.Sessione;
import Carmine.ProgettoMD.Model.*;
import Carmine.ProgettoMD.Service.GestioneAttivitàService;

public class GestioneAttivitaController {

    // --- FXML ---
    @FXML private VBox boxDatiSpecifici;
    @FXML private VBox boxFeedback;
    @FXML private HBox boxLuogo;
    @FXML private HBox boxPartecipanti;
    @FXML private HBox boxPartner;
    @FXML private Button btnAggiungiAllegato;
    @FXML private Button btnRimuoviAllegato;
    @FXML private Button btnSalva;
    @FXML private Button btnElimina;
    @FXML private ComboBox<String> cmbTipoAttivita;
    @FXML private DatePicker dpDataFine;
    @FXML private DatePicker dpDataInizio;
    @FXML private Label lblErrore;
    @FXML private ListView<String> listAllegati;
    @FXML private ListView<String> listFeedback;
    @FXML private TextArea txtDescrizione;
    @FXML private TextField txtLuogo;
    @FXML private TextField txtPartecipanti;
    @FXML private TextField txtPartner;
    @FXML private TextField txtStato;
    @FXML private TextField txtTitolo;

    
    // --- STATO INTERNO ---
    private List<File> allegatiTemp = new ArrayList<>();
    private List<Integer> allegatiRim = new ArrayList<>();
    private GestioneAttivitàService service = new GestioneAttivitàService();


    // se null siamo in creazione, se valorizzato siamo in modifica
    private Attività attivitaInModifica = null;

    
    // ------------------------------------INITIALIZE - chiamato da JavaFX dopo il caricamento dell'FXML -----------------------------------------------------------------------
  

    public void initialize() {
        cmbTipoAttivita.getItems().addAll(
            "PATRIMONIO_CULTURALE",
            "PROGETTO_INNOVAZIONE",
            "PUBLIC_ENGAGEMENT"
        );

        // stato default BOZZA per nuova attività
        txtStato.setText(StatoAttività.BOZZA.name());
        btnElimina.setVisible(false);
        btnElimina.setManaged(false);
        
    }

    
    // -----------------------------------METODI PUBBLICI - chiamati da chi apre questa schermata-----------------------------------------------

    /**
     * Se chiamato: modalità MODIFICA-> popola il form con i dati esistenti.
     * Se NON chiamato: modalità CREAZIONE-> form vuoto, stato BOZZA.
     */
    public void mostraAttività(int id) {
    	Attività attivita = service.getAttivitàById(id);
    	
        this.attivitaInModifica = attivita;
        if (attivita == null) {
            mostraErrore("L'attività richiesta non è più presente nel database.");
            btnSalva.setDisable(true);
            btnElimina.setDisable(true);
            btnAggiungiAllegato.setDisable(true);
            btnRimuoviAllegato.setDisable(true);
            return;
        }


        btnElimina.setVisible(true);
        btnElimina.setManaged(true);
        
        // popolo i campi comuni
        txtTitolo.setText(attivita.getTitolo());
        txtDescrizione.setText(attivita.getDescrizione());
        dpDataInizio.setValue(attivita.getDataInizio());
        dpDataFine.setValue(attivita.getDataFine());
        txtStato.setText(attivita.getStato().name());

        // popolo il campo specifico in base al tipo
        if (attivita instanceof PatrimonioCulturale pc) {
            cmbTipoAttivita.setValue("PATRIMONIO_CULTURALE");
            txtLuogo.setText(pc.getLuogoEsposizione());
        } else if (attivita instanceof ProgettoInnovazione pi) {
            cmbTipoAttivita.setValue("PROGETTO_INNOVAZIONE");
            txtPartner.setText(pi.getPartnerAziendale());
        } else if (attivita instanceof PublicEngagement pe) {
            cmbTipoAttivita.setValue("PUBLIC_ENGAGEMENT");
            txtPartecipanti.setText(String.valueOf(pe.getNumeroPartecipanti()));
        }

        // allegati esistenti nella listview
        for (Allegato a : attivita.getAllegati()) {
            listAllegati.getItems().add(a.getNomeOriginale() + " (" + a.getDimensione() / 1024 + " KB)");
        }

        // feedback - visibile solo in modifica
        if (attivita.getFeedbackList() != null && !attivita.getFeedbackList().isEmpty()) {
            boxFeedback.setVisible(true);
            boxFeedback.setManaged(true);
            for (Feedback f : attivita.getFeedbackList()) {
                String riga = f.getTimeStamp() + " | "
                    + (f.isEsito() ? "APPROVATO" : "RIFIUTATO")
                    + " | " + f.getTestoNote();
                listFeedback.getItems().add(riga);
            }
        }
    }

    
    // -------------------------------METODI PRIVATI DI SUPPORTO--------------------------------------------
   

    private void aggiornaCampoSpecifico(String tipo) {
        boxPartecipanti.setVisible(false); boxPartecipanti.setManaged(false);
        boxPartner.setVisible(false);      boxPartner.setManaged(false);
        boxLuogo.setVisible(false);        boxLuogo.setManaged(false);

        if (tipo == null) return;

        switch (tipo) {
            case "PUBLIC_ENGAGEMENT"    -> { boxPartecipanti.setVisible(true); boxPartecipanti.setManaged(true); }
            case "PROGETTO_INNOVAZIONE" -> { boxPartner.setVisible(true);      boxPartner.setManaged(true); }
            case "PATRIMONIO_CULTURALE" -> { boxLuogo.setVisible(true);        boxLuogo.setManaged(true); }
        }
    }

    /**
     * Legge i campi del form e costruisce l'oggetto Attività.
     * Ritorna null e mostra errore se i campi obbligatori mancano.
     */
    private Attività costruisciAttivitàDaForm() {
        String tipo = cmbTipoAttivita.getValue();

        if (tipo == null || txtTitolo.getText().isBlank() ||
            dpDataInizio.getValue() == null || dpDataFine.getValue() == null) {
            mostraErrore("Compila tutti i campi obbligatori.");
            return null;
        }

        if (dpDataInizio.getValue().isBefore(LocalDate.now())) {
            mostraErrore("La data di inizio non può essere nel passato.");
            return null;
        }

        // data fine non può essere prima della data inizio
        if (dpDataFine.getValue().isBefore(dpDataInizio.getValue())) {
            mostraErrore("La data di fine non può essere prima della data di inizio.");
            return null;
        }
        
        StatoAttività stato = StatoAttività.BOZZA;

     // controllo campo specifico in base al tipo selezionato
        switch (tipo) {
            case "PATRIMONIO_CULTURALE" -> {
                if (txtLuogo.getText().isBlank()) {
                    mostraErrore("Inserisci il luogo di esposizione.");
                    return null;
                }
            }
            case "PROGETTO_INNOVAZIONE" -> {
                if (txtPartner.getText().isBlank()) {
                    mostraErrore("Inserisci il partner aziendale.");
                    return null;
                }
            }
            case "PUBLIC_ENGAGEMENT" -> {
                if (txtPartecipanti.getText().isBlank()) {
                    mostraErrore("Inserisci il numero di partecipanti.");
                    return null;
                }
                // controllo aggiuntivo: deve essere un numero valido
                try {
                    int n = Integer.parseInt(txtPartecipanti.getText());
                    if (n <= 0) {
                        mostraErrore("Il numero di partecipanti deve essere maggiore di zero.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    mostraErrore("Il numero di partecipanti deve essere un numero intero.");
                    return null;
                }
            }
        }
        //switch per il return
        return switch (tipo) {
            case "PATRIMONIO_CULTURALE" -> new PatrimonioCulturale(
                txtTitolo.getText(), txtDescrizione.getText(),
                dpDataInizio.getValue(), dpDataFine.getValue(),
                stato, Sessione.getInstance().getUtente(),
                txtLuogo.getText()
            );
            case "PROGETTO_INNOVAZIONE" -> new ProgettoInnovazione(
                txtTitolo.getText(), txtDescrizione.getText(),
                dpDataInizio.getValue(), dpDataFine.getValue(),
                stato, Sessione.getInstance().getUtente(),
                txtPartner.getText()
            );
            case "PUBLIC_ENGAGEMENT" -> new PublicEngagement(
                txtTitolo.getText(), txtDescrizione.getText(),
                dpDataInizio.getValue(), dpDataFine.getValue(),
                stato, Sessione.getInstance().getUtente(),
                Integer.parseInt(txtPartecipanti.getText())
            );
            default -> null;
        };
    }

    private void mostraErrore(String messaggio) {
        lblErrore.setText(messaggio);
        lblErrore.setVisible(true);
        lblErrore.setManaged(true);
    }

    private void nascondiErrore() {
        lblErrore.setVisible(false);
        lblErrore.setManaged(false);
    }

    public void eliminaAllegatoTemp(int index) {
        if (index >= 0 && index < allegatiTemp.size()) {
            allegatiTemp.remove(index);
            listAllegati.getItems().remove(index);
        }
    }

    private void tornaAllaDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Carmine/ProgettoMD/view/Main.fxml"));
            Parent root = loader.load();

            MainController mainC = loader.getController();
            mainC.caricaDash(); // aggiorna la lista dopo la modifica
            Stage stage = (Stage) btnSalva.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestione Attività - Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Impossibile tornare alla schermata principale.");
        }
    }

    // -----------------------------------------------------------------------
    // HANDLER FXML
    // -----------------------------------------------------------------------

    @FXML
    public void handleSfogliaFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona allegati");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File ammessi (PDF, JPG, PNG)", "*.pdf", "*.jpg", "*.png"));

        List<File> fileSelezionati = fileChooser.showOpenMultipleDialog(btnAggiungiAllegato.getScene().getWindow());

        if (fileSelezionati != null) {
            for (File file : fileSelezionati) {
                if (!allegatiTemp.contains(file) && service.validaFile(file)) {
                    allegatiTemp.add(file);
                    listAllegati.getItems().add(file.getName() + " (" + file.length() / 1024 + " KB)");
                    nascondiErrore();
                }
                else {
                	mostraErrore("Il file non è adeguato.");
                }
            }
        }
    }

    @FXML
    public void handleRimuoviAllegato(ActionEvent event) {
        int index = listAllegati.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        if (attivitaInModifica != null && index < attivitaInModifica.getAllegati().size()) {
            // allegato esistente nel DB
            allegatiRim.add(attivitaInModifica.getAllegati().get(index).getId());
           // attivitaInModifica.rimuoviAllegato(attivitaInModifica.getAllegati().get(index).getId());
        } else {
            // file nuovo non ancora salvato
            int indexNuovo = (attivitaInModifica != null)//operatore ternario 
                ? index - attivitaInModifica.getAllegati().size()//se in modifica 
                : index;
            allegatiTemp.remove(indexNuovo);
        }
        listAllegati.getItems().remove(index);
    }

    @FXML
    public void handleSalvaBozza(ActionEvent event) {
        nascondiErrore();
        Attività attivita = costruisciAttivitàDaForm();
        if (attivita == null) return;

        if (attivitaInModifica == null) {
            service.creaBozza(attivita, allegatiTemp, Sessione.getInstance().getUtente());
        } else {
            attivita.setId(attivitaInModifica.getId());
            attivita.getAllegati().addAll(attivitaInModifica.getAllegati());
            attivita.getFeedbackList().addAll(attivitaInModifica.getFeedbackList());
            service.modificaAttività(attivita, allegatiTemp, allegatiRim);
        }
        tornaAllaDashboard();
    }


    @FXML
    public void handleAnnulla(ActionEvent event) {
        tornaAllaDashboard();
    }
    
    @FXML
    public void handleTipoChanged(ActionEvent event) {
        aggiornaCampoSpecifico(cmbTipoAttivita.getValue());
    }
    
    @FXML
    public void handleElimina(ActionEvent event) {
        if (attivitaInModifica == null) return; // non ha senso in creazione
        if (attivitaInModifica.getStato() != StatoAttività.BOZZA) return; // solo bozze

        service.eliminaAttività(attivitaInModifica);
        tornaAllaDashboard();
    }
}