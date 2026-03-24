package Carmine.ProgettoMD;

import Carmine.ProgettoMD.Model.Ruolo;
import Carmine.ProgettoMD.Model.Struttura;
import Carmine.ProgettoMD.Model.Utente;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	Utente utenteTest = new Utente(); 
    	/*
    	//PROVA CON IL DOCENTE
    	utenteTest.setNome("Mario");
        utenteTest.setCognome("Rossi");
        utenteTest.setMatricola("M12345");
        utenteTest.addRuolo(Ruolo.DOCENTE); 
        utenteTest.setStruttura(new Struttura("informatica"));
        Sessione.getInstance().login(utenteTest,Ruolo.DOCENTE);
    	*/

         //PROVA CON IL RESPONSABILE 
    	utenteTest.setNome("responsabile");
        utenteTest.setCognome("responsabile");
        utenteTest.setMatricola("MAT002");
        utenteTest.addRuolo(Ruolo.RESPONSABILE); 
        utenteTest.setStruttura(new Struttura("informatica")); //aggiunta per l'errrore in findfiltered
        Sessione.getInstance().login(utenteTest,Ruolo.RESPONSABILE);

        
        /*
        //PROVA CON IL AMMINISTRAZIONE 
	   	utenteTest.setNome("amministratore");
		utenteTest.setCognome("amministratore");
	    utenteTest.setMatricola("MAT003");
	    utenteTest.addRuolo(Ruolo.AMMINISTRATORE); 
	    utenteTest.setStruttura(new Struttura("amministrazione")); //aggiunta per l'errrore in findfiltered
	    Sessione.getInstance().login(utenteTest,Ruolo.AMMINISTRATORE);
       */
        Parent root = FXMLLoader.load(getClass().getResource("view/CreaAttivita.fxml"));
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Gestione Attività - Creazione");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    @Override
    public void stop() throws Exception {
        DatabaseManager.getInstance().closeConnection();
        System.out.println("Connessione al database chiusa correttamente.");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}