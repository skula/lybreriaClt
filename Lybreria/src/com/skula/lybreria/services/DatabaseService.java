package com.skula.lybreria.services;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.skula.lybreria.models.ExplorerItem;

public class DatabaseService {
	private static final String DATABASE_NAME = "lybreria.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME_FAVORITES = "favorites";

	private Context context;
	private SQLiteDatabase database;
	private SQLiteStatement statement;

	public DatabaseService(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.database = openHelper.getWritableDatabase();
	}

	public void bouchon() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITES);
		database.execSQL("CREATE TABLE " + TABLE_NAME_FAVORITES + "(id INTEGER PRIMARY KEY, path TEXT, name TEXT)");
		
		
		insertFavorite("/home/slown/Videos","Nemesis > videos");
		insertFavorite("/home/slown/Musique","Nemesis > musiques");
		insertFavorite("/media/EXODUS/Musique","Exodus > musiques");
		insertFavorite("/media/EXODUS/Videos","Exodus > videos");
		insertFavorite("/media/Twonky/Musiques","Twonky > musiques");
		insertFavorite("/media/Twonky/Videos","Twonky > videos");
		insertFavorite("/media","Medias");
	}

	public void insertFavorite(String path, String name) {
		String sql = "insert into " + TABLE_NAME_FAVORITES + "(path, name) values (?,?)";
		statement = database.compileStatement(sql);
		statement.bindString(1, path);
		statement.bindString(2, name);
		statement.executeInsert();
	}

	public void deleteFavorite(String path) {
		database.delete(TABLE_NAME_FAVORITES, "path='" + path + "'", null);
	}

	public List<ExplorerItem> getFavorites() {
		Cursor cursor = database.query(TABLE_NAME_FAVORITES, new String[] { "path", "name" }, null, null, null, null, "name asc");
		List<ExplorerItem> list = new ArrayList<ExplorerItem>();

		if (cursor.moveToFirst()) {
			do {
				String path = cursor.getString(0);
				String name = cursor.getString(1);
				list.add(new ExplorerItem(path, name, true, false));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public void updateFavorite(String oldPath, String newPath, String newName) {
		ContentValues args = new ContentValues();
		args.put("path", newPath);
		args.put("name", newName);
		database.update(TABLE_NAME_FAVORITES, args, "path='" + oldPath + "'", null);
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME_FAVORITES + "(id INTEGER PRIMARY KEY, path TEXT, name TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITES);
			onCreate(db);
		}
	}
}