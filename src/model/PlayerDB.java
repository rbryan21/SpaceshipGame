package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.MonsterCtrl;
import controller.PlayerCtrl;

/**
 * @author Robert
 *
 */
public class PlayerDB 
{

	//DB Connector dbc;
	private DBConnection dbc;

	/**
	 * 
	 */
	public PlayerDB() 
	{
		dbc = new DBConnection();
	}

	/** Name: PlayerCtrl
	 * Gets an already existing player from the Player table
	 * @return - A new PlayerCtrl object
	 */
	public PlayerCtrl getPlayer(int incomingPlayerID)
	{
		ResultSet rs = dbc.query(dbc, "SELECT * FROM player WHERE playerID = " + incomingPlayerID);
		int playerID = 0, locationID = 0, inventoryID = 0, playerHitpoints = 0;
		String playerName = null;
		try
		{
			while(rs.next())
			{
				playerID = rs.getInt("playerID");
				locationID =  rs.getInt("locationID");
				inventoryID = rs.getInt("inventoryID");
				playerHitpoints = rs.getInt("playerHitpoints");
				playerName = rs.getString("playerName");	
			}
		}
		catch(SQLException sqe)
		{
			System.out.println(sqe.getMessage());
		}
		return new PlayerCtrl(playerID, locationID, inventoryID, playerHitpoints, playerName);
	}


	/** Name: addPlayer
	 * Adds a new player to the Player table
	 * @return - void
	 */
	public void addPlayer(PlayerCtrl p)
	{
		int playerID = p.getPlayerID();
		int locationID = p.getLocationID();
		int inventoryID = p.getInventoryID();
		int playerHitpoints = p.getPlayerHitpoints();
		String playerName = p.getPlayerName();
		dbc.modData(dbc, "INSERT INTO PLAYER(playerID, locationID, inventoryID, playerHitpoints, playerName) "
				+ " VALUES(" + playerID +", " + locationID +", " + inventoryID +", " + playerHitpoints + ", '" + playerName +"')");	
	}

	/**
	 * Subtracts damage from player health
	 * @returns - The new player health
	 */
	public int takeDamage(int incomingPlayerHitpoints, int incomingDamage) 
	{		
		return incomingPlayerHitpoints - incomingDamage;
	}

	/** Name: setPuzzleCompleted
	 * Updates the Player Puzzle Interaction table to add the incomplete puzzles for a player
	 */
	public void addIncompletedPuzzles(int playerID, int puzzleID)
	{
		//TO-DO
		//Loop through every puzzle
		dbc.modData(dbc, "INSERT INTO PlayerPuzzleInteraction (playerID, puzzleID, isCompleted)"
				+ " VALUES(" + playerID +", " + puzzleID + ", 0");	
	}

	/** Name: setPuzzleCompleted
	 * Updates the PlayerPuzzleInteraction table to set the isCompleted value to 1 (for
	 * the specfied player/puzzle)
	 * @return - void
	 */
	public void setPuzzleCompleted(int playerID, int puzzleID) 
	{
		dbc.modData(dbc, "UPDATE PlayerPuzzleInteraction(playerID, puzzleID, isCompleted)" 
				+ " SET isCompleted = 1 WHERE playerID = " + playerID + "AND WHERE puzzleID = " + puzzleID); 
	}

	/** Name: addUndefeatedMonsters
	 * 
	 */
	public void addUndefeatedMonsters(int playerID) 
	{
		//Loop through every monster
		MonsterDB monsterDB = new MonsterDB();
		int[] monsterIDs = monsterDB.getAllMonsterIDs();

		for (int index = 0; index < monsterIDs.length; index++) 
		{
			dbc.modData(dbc, "INSERT INTO PlayerMonsterInteraction(playerID, monsterID, isDefeated)"
					+ " VALUES(" + playerID + ", " + monsterIDs[index] + ", 0)");	
		}
	}

	/** Name: setMonsterDefeated
	 * 
	 */
	public void setMonsterDefeated(int playerID, int monsterID)
	{
		dbc.modData(dbc, "UPDATE PlayerMonsterInteraction" 
				+ " SET isDefeated = 1 WHERE playerID = " + playerID + " AND monsterID = " + monsterID);
	}


	/** Name: addIncompleteLocations
	 * 
	 * @param playerID
	 * @param locationID
	 * @throws SQLException 
	 */
	public void addIncompleteLocations(int playerID) throws SQLException 
	{
		LocationDB locationDB = new LocationDB();
		int[] locationIDs = locationDB.getAllLocationIDs();
		for (int index = 0; index < locationIDs.length; index++)
		{
			dbc.modData(dbc, "INSERT INTO PlayerLocationInteraction(playerID, locationID, isCompleted)"
					+ " VALUES(" + playerID +", " + locationIDs[index] + ", 0)");
		}
	}

	/** Name: setLocationCompleted
	 * 
	 * @param playerID
	 */
	public void setLocationCompleted(int playerID, int locationID)
	{
		dbc.modData(dbc, "UPDATE PlayerLocationInteraction" 
				+ " SET isCompleted = 1 WHERE playerID = " + playerID + " AND locationID = " + locationID);
	}

	/** Name: getMaxIDOfPlayers
	 * Gets the max ID of players (for incrementing the playerID in the PlayerCtrl)
	 * @return maxID
	 * @throws SQLException
	 */
	public int getMaxIDOfPlayers() throws SQLException 
	{
		ResultSet rs = dbc.query(dbc, "SELECT playerID FROM player WHERE playerID = (SELECT MAX(playerID) FROM player)");
		int maxID = 0;
		try
		{
			while(rs.next())
			{
				maxID = rs.getInt("playerID");
			}
		}
		catch (SQLException sqle) {
			sqle.getMessage();
		}
		return maxID;
	}

	/** Name: getAllPlayerIDs
	 * Returns an arraylist of all the player ids
	 * @return playerIDAL
	 */
	public ArrayList<Integer> getAllPlayerIDs() 
	{
		ResultSet rs = dbc.query(dbc, "SELECT playerID FROM player");
		ArrayList<Integer> playerIDAL = new ArrayList<Integer>();
		try
		{
			while(rs.next())
			{
				playerIDAL.add(rs.getInt("playerID"));
			}
		}
		catch (SQLException sqle) 
		{
			sqle.getMessage();
		}
		return playerIDAL;
	}
}
