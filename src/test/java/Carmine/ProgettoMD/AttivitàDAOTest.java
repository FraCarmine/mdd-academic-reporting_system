package Carmine.ProgettoMD;

import org.junit.jupiter.api.*;

import Carmine.ProgettoMD.DAO.AttivitàDAOIMPL;
import Carmine.ProgettoMD.Model.Allegato;
import Carmine.ProgettoMD.Model.Attività;
import Carmine.ProgettoMD.Model.PatrimonioCulturale;
import Carmine.ProgettoMD.Model.ProgettoInnovazione;
import Carmine.ProgettoMD.Model.PublicEngagement;
import Carmine.ProgettoMD.Model.StatoAttività;
import Carmine.ProgettoMD.Model.Utente;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AttivitàDAOTest {

    private static AttivitàDAOIMPL dao;
    private static Utente promotore;

    // tiene traccia degli id creati in ogni test - solo quelli vengono cancellati
    private List<Integer> idDaCancellare = new ArrayList<>();

    @BeforeAll
    static void setup() throws Exception {
        dao = new AttivitàDAOIMPL();
        promotore = new Utente();
        promotore.setMatricola("M12345"); // matricola esistente nel DB
    }

    @AfterEach
    void cleanDB() throws Exception {
        if (idDaCancellare.isEmpty()) return;
        Connection conn = DatabaseManager.getInstance().getConnection();
        for (int id : idDaCancellare) {
            conn.createStatement().executeUpdate("DELETE FROM attivita WHERE id = " + id);
        }
        idDaCancellare.clear();
    }

    // -----------------------------------------------------------------------
    // BRANCH: tipo attività (3 rami dell'if instanceof)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Create PatrimonioCulturale - ID deve essere impostato")
    void testCreatePatrimonioCulturale() {
        PatrimonioCulturale attivita = new PatrimonioCulturale(
            "Mostra Arte", "Descrizione mostra",
            LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 1),
            StatoAttività.BOZZA, promotore, "Museo di Milano"
        );

        dao.create(attivita);
        idDaCancellare.add(attivita.getId());

        assertTrue(attivita.getId() > 0, "L'ID deve essere > 0 dopo la create");
        Attività trovata = dao.findById(attivita.getId());
        assertNotNull(trovata);
        assertEquals("Mostra Arte", trovata.getTitolo());
        assertInstanceOf(PatrimonioCulturale.class, trovata);
        assertEquals("Museo di Milano", ((PatrimonioCulturale) trovata).getLuogoEsposizione());
    }

    @Test
    @DisplayName("Create ProgettoInnovazione - ID deve essere impostato")
    void testCreateProgettoInnovazione() {
        ProgettoInnovazione attivita = new ProgettoInnovazione(
            "Progetto AI", "Descrizione progetto",
            LocalDate.of(2025, 2, 1), LocalDate.of(2025, 8, 1),
            StatoAttività.BOZZA, promotore, "Google Italia"
        );

        dao.create(attivita);
        idDaCancellare.add(attivita.getId());

        assertTrue(attivita.getId() > 0);
        Attività trovata = dao.findById(attivita.getId());
        assertNotNull(trovata);
        assertInstanceOf(ProgettoInnovazione.class, trovata);
        assertEquals("Google Italia", ((ProgettoInnovazione) trovata).getPartnerAziendale());
    }

    @Test
    @DisplayName("Create PublicEngagement - ID deve essere impostato")
    void testCreatePublicEngagement() {
        PublicEngagement attivita = new PublicEngagement(
            "Evento Pubblico", "Descrizione evento",
            LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 31),
            StatoAttività.BOZZA, promotore, 150
        );

        dao.create(attivita);
        idDaCancellare.add(attivita.getId());

        assertTrue(attivita.getId() > 0);
        Attività trovata = dao.findById(attivita.getId());
        assertNotNull(trovata);
        assertInstanceOf(PublicEngagement.class, trovata);
        assertEquals(150, ((PublicEngagement) trovata).getNumeroPartecipanti());
    }

    // -----------------------------------------------------------------------
    // BRANCH: allegati (lista non vuota vs lista vuota)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Create con allegati - gli allegati devono essere salvati")
    void testCreateConAllegati() {
        PatrimonioCulturale attivita = new PatrimonioCulturale(
            "Mostra con Allegati", "Descrizione",
            LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 1),
            StatoAttività.BOZZA, promotore, "Galleria Brera"
        );

        attivita.addAllegato(new Allegato("bando.pdf", "/uploads/bando.pdf", 1024, attivita));
        attivita.addAllegato(new Allegato("relazione.docx", "/uploads/relazione.docx", 2048, attivita));

        dao.create(attivita);
        idDaCancellare.add(attivita.getId());

        assertTrue(attivita.getId() > 0);
        Attività trovata = dao.findById(attivita.getId());
        assertNotNull(trovata);
        assertEquals(2, trovata.getAllegati().size(), "Devono esserci 2 allegati");
    }

    @Test
    @DisplayName("Create senza allegati - la lista deve essere vuota")
    void testCreateSenzaAllegati() {
        PatrimonioCulturale attivita = new PatrimonioCulturale(
            "Mostra senza Allegati", "Descrizione",
            LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 1),
            StatoAttività.BOZZA, promotore, "Palazzo Reale"
        );

        dao.create(attivita);
        idDaCancellare.add(attivita.getId());

        Attività trovata = dao.findById(attivita.getId());
        assertNotNull(trovata);
        assertTrue(trovata.getAllegati().isEmpty(), "Non ci devono essere allegati");
    }

    // -----------------------------------------------------------------------
    // BRANCH EXTRA: verifica ID univoci
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Create due attività - devono avere ID diversi")
    void testCreateDueAttivitàIDDiversi() {
        PatrimonioCulturale a1 = new PatrimonioCulturale(
            "Prima", "desc", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 1),
            StatoAttività.BOZZA, promotore, "Luogo 1"
        );
        PatrimonioCulturale a2 = new PatrimonioCulturale(
            "Seconda", "desc", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 7, 1),
            StatoAttività.BOZZA, promotore, "Luogo 2"
        );

        dao.create(a1);
        dao.create(a2);
        idDaCancellare.add(a1.getId());
        idDaCancellare.add(a2.getId());

        assertNotEquals(a1.getId(), a2.getId(), "Due attività devono avere ID diversi");
    }
    
    
 // -----------------------------------------------------------------------
    // BRANCH: rowInsert non funziona per lancio di try catch 
    
    
    
}