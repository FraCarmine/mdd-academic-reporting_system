package Carmine.ProgettoMD.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

import Carmine.ProgettoMD.Sessione;
import Carmine.ProgettoMD.Model.*;
import Carmine.ProgettoMD.Service.GestioneAttivitàService;

public class MainController {

    // --- FXML ---
    @FXML private TableView<Attività> tableAttivita;
    @FXML private TableColumn<Attività, String> colTitolo;
    @FXML private TableColumn<Attività, String> colTipo;
    @FXML private TableColumn<Attività, String> colStato;
    @FXML private TableColumn<Attività, String> colDataInizio;
    @FXML private TableColumn<Attività, String> colDataFine;
    @FXML private TableColumn<Attività, String> colPromotore;
    @FXML private Label lblUtente;
    @FXML private Label lblRuolo;
    @FXML private Label lblContatore;
    @FXML private Label lblStatus;
    @FXML private TextField txtRicercaTitolo;
    @FXML private ComboBox<String> cmbStato;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<Integer> cmbAnno;
    @FXML private Button btnAmministrazione;
    @FXML private Button btnNuova;
    @FXML private Button btnModifica;

    private GestioneAttivitàService service = new GestioneAttivitàService();

    //
    // -------------------- INITIALIZE-----------------------------------------------------------------------
    // 

    public void initialize() {
        // collego le colonne ai campi dell'oggetto Attività
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colDataInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFine"));

        //lambda
        colTipo.setCellValueFactory(cellData -> {
            Attività a = cellData.getValue();
            String tipo;
            if (a instanceof PatrimonioCulturale) {
                tipo = "Patrimonio Culturale";
            } else if (a instanceof ProgettoInnovazione) {
                tipo = "Progetto Innovazione";
            } else if (a instanceof PublicEngagement) {
                tipo = "Public Engagement";
            } else {
                tipo = "Sconosciuto";
            }
            return new javafx.beans.property.SimpleStringProperty(tipo);//serve per poter ritornare una stringa in
        });

        // stato - getStato() ritorna un enum, serve .name()
        colStato.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStato().name())
        );

        // promotore - getPromotore() ritorna Utente
        colPromotore.setCellValueFactory(cellData -> {
            Utente p = cellData.getValue().getPromotore();
            return new javafx.beans.property.SimpleStringProperty(p.getMatricola());
        });

        // info utente e ruolo dalla sessione
        lblUtente.setText(Sessione.getInstance().getUtente().getNome());
        lblRuolo.setText("Ruolo: " + Sessione.getInstance().getRuoloAttivo().name());

        // configuro visibilità bottoni in base al ruolo
        //configuraInterfacciaPerRuolo(Sessione.getInstance().getRuoloAttivo());

        // carico la dashboard
        //caricaDash(); chiamato sempre
    }

    // 
    // -------------------------------CARICA DASH -----------------------------------------------------------------------
    // 

    public void caricaDash() {
        Utente utente = Sessione.getInstance().getUtente();
        Ruolo ruolo = Sessione.getInstance().getRuoloAttivo();
        configuraInterfacciaPerRuolo(ruolo);
        
        // delego al service che gestisce la logica per ruolo
        List<Attività> lista = service.getAttivitàPerRuolo(utente, ruolo, null, null, null); //null perchè non ho la ricerca

        // BREAK: lista vuota o null
        if (lista == null || lista.isEmpty()) {
            mostraErrDash("Nessuna attività presente");
            return;
        }

        popolaDash(lista);
    }

    private void popolaDash(List<Attività> att) {
        ObservableList<Attività> items = FXCollections.observableArrayList(att);
        tableAttivita.setItems(items);
        lblContatore.setText("(" + att.size() + " risultati)");
        lblStatus.setText("");
        lblStatus.setStyle("-fx-text-fill: #27ae60;");
    }

    private void mostraErrDash(String err) {
        lblContatore.setText("(0 risultati)");
        lblStatus.setText(err);
        lblStatus.setStyle("-fx-text-fill: red;");
    }

    private void aggiornaDash() {
        tableAttivita.getItems().clear();
        caricaDash();
    }

    //
    //  ------------------------------CONFIGURA INTERFACCIA PER RUOLO-----------------------------------------------------------------------
    // 

    private void configuraInterfacciaPerRuolo(Ruolo ruoloAt) {
        boolean isAdmin = ruoloAt == Ruolo.AMMINISTRATORE;
        btnAmministrazione.setVisible(isAdmin);
        btnAmministrazione.setManaged(isAdmin);
        
     // solo il DOCENTE può creare nuove attività
        boolean isDocente = ruoloAt == Ruolo.DOCENTE;
        btnNuova.setVisible(isDocente);
        btnNuova.setManaged(isDocente);
        btnModifica.setVisible(isDocente);
        btnModifica.setManaged(isDocente);
        
        
    }

    private void popolaMenu(List<Ruolo> lista) {
        // TODO: popolare menu selezione ruolo
    }

    public void selezionaRuolo() {
        // TODO: dialog per selezionare il ruolo attivo
    }

    // 
    // ---------------------------------HANDLER DETTAGLIO, MODIFICA, NUOVA -----------------------------------------------------------------------
    //

    public void handleclickDettagli() {
        Attività selezionata = tableAttivita.getSelectionModel().getSelectedItem();
        if (selezionata == null) return;

        // carico il dettaglio completo con allegati e feedback
        //DettaglioAttivitaController controller = loader.getController();
        //controller.mostraAttività(selezionata.id);
        // TODO: aprire DettaglioAttivita.fxml quando sarà implementato
        System.out.println("Dettaglio: ");
    }

    public void handleModifica() {
        Attività selezionata = tableAttivita.getSelectionModel().getSelectedItem();
        if (selezionata == null) return;

        // solo le attività in BOZZA possono essere modificate
        if (selezionata.getStato() != StatoAttività.BOZZA) {
            mostraErrDash("Solo le attività in stato BOZZA possono essere modificate.");
            return;
        }
        
        // carico il dettaglio completo con allegati e feedback
        //Attività dettaglio = service.getAttivitàById(selezionata.getId());
        /*
        if (dettaglio == null) {
            mostraErrDash("Errore nel caricamento dell'attività.");
            return;
        }*/

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Carmine/ProgettoMD/view/CreaAttivita.fxml"));
            Parent root = loader.load();

            GestioneAttivitaController controller = loader.getController();
            controller.mostraAttività(selezionata.getId()); //modalità modifica

            Stage stage = (Stage) tableAttivita.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            mostraErrDash("Errore apertura modifica: " + e.getMessage());
        }
    }

    public void handleNewAtt() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Carmine/ProgettoMD/view/CreaAttivita.fxml"));
            Parent root = loader.load();

            // non chiamo mostraAttività  il controller parte in modalità CREAZIONE
            Stage stage = (Stage) tableAttivita.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            mostraErrDash("Errore apertura nuova attività: " + e.getMessage());
        }
    }

    // 
    //---------------------------------------- HANDLER MENU E ALTRI-----------------------------------------------------------------------
    // 

    public void handleMenuAttivita() {
        aggiornaDash();
    }

    public void handleMenuNotifiche() {
        // TODO
    }

    public void handleMenuAmministrazione() {
        // TODO
    }

    public void handleMenuReport() {
        // TODO
    }

    public void handleLogout() {
        //Sessione.getInstance().logout(); da decomementare ma ora mi dava errore dopo
        // TODO: tornare alla schermata di login
    }

    public void handleCambiaRuolo() {
        selezionaRuolo();
    }

    public void hadleRicercaAtt() {
        // TODO: chiamare service.getAttivitàPerRuolo con i valori dei filtri
        // txtRicercaTitolo, cmbStato, cmbTipo, cmbAnno
    }
}