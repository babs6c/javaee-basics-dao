package com.exos.dao;

import static com.exos.dao.DAOUtilitaire.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.exos.beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao{

	private DAOFactory daoFactory;
	private final static String SQL_LISTE_MEMBERS="select * from membres";
	private final static String SQL_ADD_MEMBER="insert into membres(nom,email,pass,photo,date_inscription) values(?,?,?,?,NOW())";
	private final static String SQL_FIND_MEMBER="select * from membres where email=? and pass=?";

	
	UtilisateurDaoImpl(DAOFactory daoFactory)
	{
		this.daoFactory=daoFactory;
	}
	
	@Override
	public List<Utilisateur> lister() throws DAOException {
		
		List<Utilisateur> utilisateurs=new ArrayList<Utilisateur>();
		Connection connexion=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultat=null;
		
		try {
			connexion=daoFactory.getConnection();
			preparedStatement=initialisationRequetePreparee(connexion, SQL_LISTE_MEMBERS, false);
			resultat=preparedStatement.executeQuery();
			while(resultat.next())
			{
				Utilisateur user=map(resultat);
				utilisateurs.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException(e);
		}
		finally {
			fermeturesSilencieuses(resultat,preparedStatement,connexion);
		}
		
		return utilisateurs;
	}

	@Override
	public Utilisateur trouver(String email, String pass) throws DAOException {
		Utilisateur utilisateur=null;
		Connection connexion=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultat=null;
		
		try {
			connexion=daoFactory.getConnection();
			preparedStatement=initialisationRequetePreparee(connexion, SQL_FIND_MEMBER, false,email,pass);
			resultat=preparedStatement.executeQuery();
			if(resultat.next())
			{
				utilisateur=map(resultat);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException(e);
		}
		finally {
			fermeturesSilencieuses(resultat,preparedStatement,connexion);
		}
		
		return utilisateur;
	}

	@Override
	public void add(Utilisateur utilisateur) throws DAOException {
		Connection connexion=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connexion=daoFactory.getConnection();
			preparedStatement=DAOUtilitaire.initialisationRequetePreparee(connexion, SQL_ADD_MEMBER, false, utilisateur.getNom(),utilisateur.getEmail(),utilisateur.getPass(),utilisateur.getPhoto());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException(e);
		}
		finally {
			fermeturesSilencieuses(preparedStatement,connexion);
		}
		
	}
	
	/*
	 * Simple méthode utilitaire permettant de faire la correspondance (le
	 * mapping) entre une ligne issue de la table des utilisateurs (un
	 * ResultSet) et un bean Utilisateur.
	 */
	private static Utilisateur map( ResultSet resultat ) throws SQLException {
		Utilisateur user=new Utilisateur();
		user.setId(resultat.getInt("id"));
		user.setEmail(resultat.getString("email"));
		user.setNom(resultat.getString("nom"));
		user.setPass(resultat.getString("pass"));
		user.setPhoto(resultat.getString("photo"));
		user.setDateInscription(resultat.getTimestamp("date_inscription"));
	    return user;
	}

}