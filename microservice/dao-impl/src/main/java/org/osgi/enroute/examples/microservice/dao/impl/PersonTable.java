/**
 * 
 */
package org.osgi.enroute.examples.microservice.dao.impl;

/**
 * @author vivcrone
 *
 */
public interface PersonTable {
	
	String TABLE_NAME = "PERSONS";
	String SQL_SELECT_ALL_PERSONS = "SELECT * FROM " + TABLE_NAME;
	String SQL_DELETE_PERSON_BY_PK = "DELETE FROM " + TABLE_NAME + " where PERSON_ID=?";
	String SQL_SELECT_PERSON_BY_PK = "SELECT * FROM " + TABLE_NAME + " where PERSON_ID=?";
	String SQL_INSERT_PERSON = "INSERT INTO " + TABLE_NAME + "(FIRST_NAME,LAST_NAME) VALUE(?,?)";
	String SQL_UPDATE_PERSON_BY_PK = "UPDATE " + TABLE_NAME + " SET FIRST_NAME=?, LAST_NAME=? WHERE PERSON_ID=?";
	String PERSON_ID = "person_id";
	String FIRST_NAME ="first_name";
	String LAST_NAME ="last_name";
	String INIT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" //
			+ "person_id bigint GENERATED BY DEFAULT AS IDENTITY," //
			+ "first_name varchar(255) NOT NULL," //
			+ "last_name varchar(255) NOT NULL," //
			+ "PRIMARY KEY (person_id)" //
			+ ") ;";
}
