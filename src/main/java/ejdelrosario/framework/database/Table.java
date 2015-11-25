/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */

package ejdelrosario.framework.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public abstract class Table {
	
private SQLiteDatabase db;
	
	public abstract String getTableStructure();
	public abstract String getName();

	/**
	 * Inserts the values to the table
	 * @param values
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insert(ContentValues values) {
		db = DatabaseHelper.getInstance().getWritableDatabase();
		if(db != null) return db.insertOrThrow( getName(), null, values);
		return -1;

	}

	/**
	 * Updates the row if it is existing based on the filter but inserts them instead if row is not existing.
	 * @param values
	 * @param filter
	 * @return number of rows affected if such data is existing and the row ID of the newly inserted row, or -1 if an error occurred if not existing
	 */
	public long insertOrUpdate( ContentValues values , String filter ) {
		db = DatabaseHelper.getInstance().getWritableDatabase();
		int rowsAffected = db.update( getName() , values, filter , null );
		
		if(rowsAffected == 0 )
			return db.insertOrThrow( getName(), null, values);
		
		return rowsAffected;
	}

	/**
	 * updates the values set based on the filter on your whereClause parameter
	 * @param values
	 * @param whereClause where statement string
	 * @return
	 * @throws SQLiteException
	 * @throws SQLException
	 */
	public int update(ContentValues values, String whereClause) throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getWritableDatabase();
		if(db != null) return db.update( getName(), values, whereClause, null);
		return -1;
	}

	/**
	 * updates the values set based on the filter on your whereClause parameter
	 * @param values
	 * @param whereClause where statement string
	 * @param filterValues arguments for the whereClause
	 * @return
	 * @throws SQLiteException
	 * @throws SQLException
	 */
	public int update(ContentValues values, String whereClause, String[] filterValues) throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getWritableDatabase();
		if(db != null) return db.update( getName(), values, whereClause, filterValues );
		return -1;
	}

	/**
	 * deletes all the data in this table
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 * @throws SQLiteException
	 * @throws SQLException
	 */
	public int delete() throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getWritableDatabase();
		if(db != null) return db.delete( getName(), null, null);
		return -1;
	}
	
	public int delete(String whereClause, String[] filterValues) throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getWritableDatabase();
		if(db != null) return db.delete( getName(), whereClause, filterValues);
		return -1;
	}

	/**
	 * query all data in this table
	 * @return A Cursor object, which is positioned before the first entry.
	 * @throws SQLiteException
	 * @throws SQLException
	 */
	public Cursor select() throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getReadableDatabase();
		if(db != null) return db.rawQuery( "SELECT * FROM " + getName(), null);
		return null;
	}

	/**
	 * query all data with filters
	 * @param filter filter statement
	 * @param filterValues arguments for the filter parameter
	 * @return
	 * @throws SQLiteException
	 * @throws SQLException
	 */
	public Cursor select(String filter,String[] filterValues) throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getReadableDatabase();
		if(db != null) return db.rawQuery( "SELECT * FROM " + getName() + " where " + filter, filterValues );
		return null;
	}

	/**
	 * performs raw query
	 * @param query query string
	 * @param filterValues
	 * @return
	 * @throws SQLiteException
	 * @throws SQLException
	 */
	public Cursor rawQuery(String query, String[] filterValues) throws SQLiteException , SQLException {
		db = DatabaseHelper.getInstance().getReadableDatabase();
		if(db != null) return db.rawQuery( query , filterValues );
		return null;
	}

}
