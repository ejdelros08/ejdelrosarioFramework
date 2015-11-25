/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */

package ejdelrosario.framework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	private EngineDatabase mDatabase;
	public static DatabaseHelper dbManager;
	
	public DatabaseHelper(Context context, EngineDatabase database) {
		super(context, database.getName(), null, database.getVersion());
		mDatabase = database;
	}

	/**
	 * Creates a database instance. Variables in this method are static so you can just call this one time for your App's entire lifecycle
	 * @param context
	 * @param database your own extension of EngineDatabase class. See documentation file.
	 */
	public static void createDatabase(Context context, EngineDatabase database) {
		
		if(dbManager == null)
			dbManager = new DatabaseHelper( context, database);
		
	}

	/**
	 *
	 * @return the database instance
	 */
	public static DatabaseHelper getInstance() { 
		return dbManager;
	}
	
	public EngineDatabase getDatabaseInfo() {
		return mDatabase;
	}
	
	
	public void close() {
		dbManager = null;
		super.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		for(Table table : mDatabase.getTables()) {
			db.execSQL( table.getTableStructure() );
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		mDatabase.onUpgrade(db, oldVersion, newVersion);
		
	}
}
