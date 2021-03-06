package com.example.joshuahales.notetakingapp.activity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NotesProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    //Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMathcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Note";

    //Static initializer
    static {
        uriMathcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMathcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        //Calling this methode will open database
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    //This will get data from table in the notes database(Can return whole table or single rows)
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (uriMathcher.match(uri) == NOTES_ID) {
            selection = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS, selection, null, null, null, DBOpenHelper.NOTE_CREATED + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_NOTES, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES, values, selection, selectionArgs);
    }
}
