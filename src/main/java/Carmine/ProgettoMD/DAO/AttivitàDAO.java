package Carmine.ProgettoMD.DAO;
import Carmine.ProgettoMD.Model.Attività;
import Carmine.ProgettoMD.Model.Utente;
import Carmine.ProgettoMD.Model.StatoAttività;

import java.sql.SQLException;
import java.util.List;

public interface AttivitàDAO {

    boolean create(Attività attivita);

    boolean update(Attività attivita);

    boolean delete(Attività attivita);

    // METODO PER POPOLARE NEL DETTAGLIO
    Attività findById(int id) ;

    // I METODI CON FILTRI
    List<Attività> findByAutore(Utente utente, Integer anno, String titolo, List<StatoAttività> stati);

    List<Attività> findFilterd(String struttura, List<StatoAttività> statoAttivita, Integer anno, String titolo, List<String> tipoAtt);
}