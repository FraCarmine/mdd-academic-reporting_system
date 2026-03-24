package Carmine.ProgettoMD.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Carmine.ProgettoMD.DatabaseManager;
import Carmine.ProgettoMD.Model.Allegato;
import Carmine.ProgettoMD.Model.Attività;
import Carmine.ProgettoMD.Model.Feedback;
import Carmine.ProgettoMD.Model.PatrimonioCulturale;
import Carmine.ProgettoMD.Model.ProgettoInnovazione;
import Carmine.ProgettoMD.Model.PublicEngagement;
import Carmine.ProgettoMD.Model.StatoAttività;
import Carmine.ProgettoMD.Model.Utente;

public class AttivitàDAOIMPL implements AttivitàDAO {

	public AttivitàDAOIMPL() {
		
	}
	
	@Override
	public boolean create(Attività attivita) {// @TODO pensare se debba essere un void i crud o se ha senso che ritorni
											// qualcosa solitamente il boolean
		Connection conn = DatabaseManager.getInstance().getConnection();
		String sql = "INSERT INTO attivita (titolo, descrizione, dataInizio, dataFine, stato, autore_matricola, tipoAtt, luogoEsposizione, partnerAziendale, numeroPartecipanti) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean successo = false; // default falso
		try {
			conn.setAutoCommit(false); // tolgo autocommit perchè ci sono piu transazioni

			try (PreparedStatement insertState = conn.prepareStatement(sql)) {
				// parametri comuni 1-6
				insertState.setString(1, attivita.getTitolo());
				insertState.setString(2, attivita.getDescrizione());
				insertState.setDate(3, java.sql.Date.valueOf(attivita.getDataInizio()));
				insertState.setDate(4, java.sql.Date.valueOf(attivita.getDataFine()));
				insertState.setString(5, attivita.getStato().name());
				insertState.setString(6, attivita.getPromotore().getMatricola());
				// 3 tipi
				if (attivita instanceof PatrimonioCulturale) {
					insertState.setString(7, "PatrimonioCulturale");
					insertState.setString(8, ((PatrimonioCulturale) attivita).getLuogoEsposizione());
					insertState.setNull(9, java.sql.Types.VARCHAR);
					insertState.setNull(10, java.sql.Types.INTEGER);
				} else if (attivita instanceof ProgettoInnovazione) {
					insertState.setString(7, "ProgettoInnovazione");
					insertState.setNull(8, java.sql.Types.VARCHAR);
					insertState.setString(9, ((ProgettoInnovazione) attivita).getPartnerAziendale());
					insertState.setNull(10, java.sql.Types.INTEGER);
				} else if (attivita instanceof PublicEngagement) {
					insertState.setString(7, "PublicEngagement");
					insertState.setNull(8, java.sql.Types.VARCHAR);
					insertState.setNull(9, java.sql.Types.VARCHAR);
					insertState.setInt(10, ((PublicEngagement) attivita).getNumeroPartecipanti());
				}

				int rowInsert = insertState.executeUpdate();
				System.out.println("Attività creata. Righe modificate: " + rowInsert);
				if (rowInsert > 0) {
					// --- RECUPERO ID (Sempre dentro la transazione) ---
					String sqlId = "SELECT LAST_INSERT_ID()";
					try (PreparedStatement idStmt = conn.prepareStatement(sqlId);
							ResultSet rsId = idStmt.executeQuery()) {// provo a fare entrambe le cose con il try
						if (rsId.next()) {
							attivita.setId(rsId.getInt(1));
						}
					}
					// --------------------------------------------------------ALLEGATI----------------------------------------------
					// Salvo eventuali allegati
					List<Allegato> allegati = attivita.getAllegati();
					if (attivita.getAllegati() != null && !attivita.getAllegati().isEmpty()) {
						String sqlAllegato = "INSERT INTO allegato (nomeOriginale, percorsoServer, dimensione, attivita_id) VALUES (?, ?, ?, ?)";
						try (PreparedStatement allegatoStmt = conn.prepareStatement(sqlAllegato)) {
							for (Allegato allegato : allegati) {
								allegatoStmt.setString(1, allegato.getNomeOriginale());
								allegatoStmt.setString(2, allegato.getPercorsoServer());
								allegatoStmt.setInt(3, allegato.getDimensione());
								allegatoStmt.setInt(4, attivita.getId());
								allegatoStmt.executeUpdate();
							}
						}
					}
					// _--------------------------------------fine allegati-------------------------------------------------------------
					conn.commit();
					successo = true;//tutto a buon fine
					System.out.println("Transazione completata.");
				}
			}
		} catch (SQLException ex) {
			try {
				conn.rollback();
				System.out.println("Rollback eseguito.");
			} catch (SQLException rollbackEx) {
				System.out.println("Errore durante rollback: " + rollbackEx.getMessage());
			}
			System.out.println("SqlException " + ex.getMessage());
			System.out.println("Sql State " + ex.getSQLState());
			System.out.println("VendorError " + ex.getErrorCode());
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("Errore ripristino autocommit: " + e.getMessage());
			}
		}
		return successo;
	}

	@Override
	public boolean update(Attività attivita) {
		return true;
		// TODO Auto-generated method stub

	}

	@Override
	public boolean delete(Attività attivita) {// semplice per la polita on delete cascade
		Connection conn = DatabaseManager.getInstance().getConnection();
		String sql = "DELETE FROM attivita WHERE id = ?";
		boolean successo=false;

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, attivita.getId());
			int rowDelete = stmt.executeUpdate();
			System.out.println("Attività eliminata. Righe modificate: " + rowDelete);
		} catch (SQLException ex) {
			System.out.println("SqlException " + ex.getMessage());
			System.out.println("Sql State " + ex.getSQLState());
			System.out.println("VendorError " + ex.getErrorCode());
		}
		return successo;
	}

	@Override
	public Attività findById(int id) {
		Connection conn = DatabaseManager.getInstance().getConnection();

		// --- QUERY 1: attività principale ---
		String sqlAttivita = "SELECT * FROM attivita WHERE id = ?";
		// --- QUERY 2: allegati ---
		String sqlAllegati = "SELECT * FROM allegato WHERE attivita_id = ?";
		// --- QUERY 3: feedback ---
		String sqlFeedback = "SELECT * FROM feedback WHERE attivita_id = ?";

		try {
			Attività attivita = null;

			// COSTRUISCO ATTIVITà BASE
			try (PreparedStatement stmt = conn.prepareStatement(sqlAttivita)) {
				stmt.setInt(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						String tipo = rs.getString("tipoAtt");
						String titolo = rs.getString("titolo");
						String descrizione = rs.getString("descrizione");
						LocalDate dataInizio = rs.getDate("dataInizio").toLocalDate();
						LocalDate dataFine = rs.getDate("dataFine").toLocalDate();
						StatoAttività stato = StatoAttività.valueOf(rs.getString("stato"));
						Utente promotore = new Utente();
						promotore.setMatricola(rs.getString("autore_matricola"));// @TODO controllare il metodo e se
																					// ha senso quello che ho fatto.

						if ("PatrimonioCulturale".equals(tipo)) {
							attivita = new PatrimonioCulturale(titolo, descrizione, dataInizio, dataFine, stato,
									promotore, rs.getString("luogoEsposizione"));
						} else if ("ProgettoInnovazione".equals(tipo)) {
							attivita = new ProgettoInnovazione(titolo, descrizione, dataInizio, dataFine, stato,
									promotore, rs.getString("partnerAziendale"));
						} else if ("PublicEngagement".equals(tipo)) {
							attivita = new PublicEngagement(titolo, descrizione, dataInizio, dataFine, stato, promotore,
									rs.getInt("numeroPartecipanti"));
						}
						attivita.setId(rs.getInt("id"));
					}
				}
			}

			// se non esiste mi fermo
			if (attivita == null)
				return null;

			// aggiungo gli allegati
			try (PreparedStatement stmt = conn.prepareStatement(sqlAllegati)) {
				stmt.setInt(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						Allegato allegato = new Allegato(rs.getInt("id"), rs.getString("nomeOriginale"),
								rs.getString("percorsoServer"), rs.getInt("dimensione"), attivita);
						attivita.addAllegato(allegato);
					}
				}
			}

			// BLOCCO 3 - aggiungo i feedback
			try (PreparedStatement stmt = conn.prepareStatement(sqlFeedback)) {
				stmt.setInt(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {// chiamo il costruttore per il dao e poi aggiungo ad attività
						Feedback feedback = new Feedback(rs.getInt("id"),
								rs.getTimestamp("timestamp").toLocalDateTime(), rs.getString("testoNote"),
								rs.getBoolean("esito"), attivita);
						attivita.addFeedback(feedback);
					}
				}
			}

			return attivita;

		} catch (SQLException ex) {
			System.out.println("SqlException " + ex.getMessage());
			System.out.println("Sql State " + ex.getSQLState());
			System.out.println("VendorError " + ex.getErrorCode());
			return null;// non ho messo il commit tanto è uguale
		}
	}

	@Override
	public List<Attività> findByAutore(Utente utente, Integer anno, String titolo, List<StatoAttività> stati) {
		Connection conn = DatabaseManager.getInstance().getConnection();
		List<Attività> risultato = new ArrayList<>();

		// costruisco la query dinamicamente in base ai filtri presenti
		StringBuilder sql = new StringBuilder("SELECT * FROM attivita WHERE autore_matricola = ?");

		if (anno != null) {
			sql.append(" AND YEAR(dataInizio) = ?");
		}
		if (titolo != null && !titolo.isEmpty()) {
			sql.append(" AND titolo LIKE ?");
		}
		if (stati != null && !stati.isEmpty()) {
			StringBuilder placeholders = new StringBuilder();

			// Ciclo su tutti gli elementi della lista
			for (int i = 0; i < stati.size(); i++) {
				placeholders.append("?"); // Aggiungo un punto interrogativo

				// Se NON sono all'ultimo elemento, aggiungo anche una virgola e uno spazio
				if (i < stati.size() - 1) {
					placeholders.append(", ");
				}
			}

			// Alla fine unisco tutto alla query principale
			sql.append(" AND stato IN (").append(placeholders.toString()).append(")");
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			int index = 1;

			// parametro fisso
			stmt.setString(index++, utente.getMatricola());

			// parametri opzionali nello stesso ordine in cui li ho aggiunti
			if (anno != null) {
				stmt.setInt(index++, anno);
			}
			if (titolo != null && !titolo.isEmpty()) {
				stmt.setString(index++, "%" + titolo + "%"); // LIKE %titolo%
			}
			if (stati != null && !stati.isEmpty()) {
				for (StatoAttività stato : stati) {
					stmt.setString(index++, stato.name());
				}
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String tipo = rs.getString("tipoAtt");
					LocalDate dataInizio = rs.getDate("dataInizio").toLocalDate();
					LocalDate dataFine = rs.getDate("dataFine").toLocalDate();
					StatoAttività stato = StatoAttività.valueOf(rs.getString("stato"));
					Utente promotore = new Utente();
					promotore.setMatricola(rs.getString("autore_matricola"));
					String titoloDB = rs.getString("titolo");
					String descrizione = rs.getString("descrizione");

					Attività attivita = null;
					if ("PatrimonioCulturale".equals(tipo)) {
						attivita = new PatrimonioCulturale(titoloDB, descrizione, dataInizio, dataFine, stato,
								promotore, rs.getString("luogoEsposizione"));
					} else if ("ProgettoInnovazione".equals(tipo)) {
						attivita = new ProgettoInnovazione(titoloDB, descrizione, dataInizio, dataFine, stato,
								promotore, rs.getString("partnerAziendale"));
					} else if ("PublicEngagement".equals(tipo)) {
						attivita = new PublicEngagement(titoloDB, descrizione, dataInizio, dataFine, stato, promotore,
								rs.getInt("numeroPartecipanti"));
					}

					if (attivita != null) {
						attivita.setId(rs.getInt("id"));
						risultato.add(attivita);
					}
				}
			}

		} catch (SQLException ex) {
			System.out.println("SqlException " + ex.getMessage());
			System.out.println("Sql State " + ex.getSQLState());
			System.out.println("VendorError " + ex.getErrorCode());
			return null; // errore del db diverso da lista vuota
		}

		return risultato;
	}

	@Override
	public List<Attività> findFilterd(String struttura, List<StatoAttività> statoAttivita, Integer anno, String titolo, List<String> tipoAtt){
		Connection conn = DatabaseManager.getInstance().getConnection();
		List<Attività> risultato = new ArrayList<>();

		StringBuilder sqlBase  = new StringBuilder("SELECT a.* FROM attivita a ");
		StringBuilder sqlWhere = new StringBuilder("WHERE 1=1");

		if (struttura != null && !struttura.isEmpty()) {
			sqlBase.append("JOIN utente u ON u.matricola = a.autore_matricola ");
			sqlWhere.append(" AND u.struttura_nome = ?");
		}
		if (anno != null) {
			sqlWhere.append(" AND YEAR(dataInizio) = ?");
		}
		if (titolo != null && !titolo.isEmpty()) {
			sqlWhere.append(" AND titolo LIKE ?");
		}
		if (statoAttivita != null && !statoAttivita.isEmpty()) {
			StringBuilder placeholders = new StringBuilder();
			for (int i = 0; i < statoAttivita.size(); i++) {
				placeholders.append("?");
				if (i < statoAttivita.size() - 1)
					placeholders.append(", ");
			}
			sqlWhere.append(" AND stato IN (").append(placeholders).append(")");
		}
		if (tipoAtt != null && !tipoAtt.isEmpty()) {
			StringBuilder placeholders = new StringBuilder();
			for (int i = 0; i < tipoAtt.size(); i++) {
				placeholders.append("?");
				if (i < tipoAtt.size() - 1)
					placeholders.append(", ");
			}
			sqlWhere.append(" AND tipoAtt IN (").append(placeholders).append(")");
		}
		StringBuilder sql = sqlBase.append(sqlWhere);

		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			int index = 1;

			if (struttura != null && !struttura.isEmpty()) {
				stmt.setString(index++, struttura);
			}
			if (anno != null) {
				stmt.setInt(index++, anno);
			}
			if (titolo != null && !titolo.isEmpty()) {
				stmt.setString(index++, "%" + titolo + "%");
			}
			if (statoAttivita != null && !statoAttivita.isEmpty()) {
				for (StatoAttività stato : statoAttivita) {
					stmt.setString(index++, stato.name());
				}
			}
			if (tipoAtt != null && !tipoAtt.isEmpty()) {
				for (String tipo : tipoAtt) {
					stmt.setString(index++, tipo);
				}
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String tipo = rs.getString("tipoAtt");
					LocalDate dataInizio = rs.getDate("dataInizio").toLocalDate();
					LocalDate dataFine = rs.getDate("dataFine").toLocalDate();
					StatoAttività stato = StatoAttività.valueOf(rs.getString("stato"));
					Utente promotore = new Utente();
					promotore.setMatricola(rs.getString("autore_matricola"));

					Attività attivita = null;
					if ("PatrimonioCulturale".equals(tipo)) {
						attivita = new PatrimonioCulturale(rs.getString("titolo"), rs.getString("descrizione"),
								dataInizio, dataFine, stato, promotore, rs.getString("luogoEsposizione"));
					} else if ("ProgettoInnovazione".equals(tipo)) {
						attivita = new ProgettoInnovazione(rs.getString("titolo"), rs.getString("descrizione"),
								dataInizio, dataFine, stato, promotore, rs.getString("partnerAziendale"));
					} else if ("PublicEngagement".equals(tipo)) {
						attivita = new PublicEngagement(rs.getString("titolo"), rs.getString("descrizione"), dataInizio,
								dataFine, stato, promotore, rs.getInt("numeroPartecipanti"));
					}

					if (attivita != null) {
						attivita.setId(rs.getInt("id"));
						risultato.add(attivita);
					}
				}
			}

		} catch (SQLException ex) {
			System.out.println("SqlException " + ex.getMessage());
			System.out.println("Sql State " + ex.getSQLState());
			System.out.println("VendorError " + ex.getErrorCode());
			return null; // errore DB, diverso da lista vuota
		}

		return risultato;
	}

}
