package fr.utt.if26.istarve.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is responsible for creating the database
 */
public class MaBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_DERNIERS_RESTAURANTS = "table_derniers_restaurants";
    private static final String TABLE_FAVORIS_RESTAURANTS = "table_favoris_restaurants";

    private static final String COL_mId = "mId";
    private static final String COL_mlat = "lat";
    private static final String COL_mLon = "mLon";
    private static final String COL_mAddress = "mAddress";
    private static final String COL_mName = "name";
    private static final String COL_mThumbnailImgUrl = "mThumbnailImgUrl";
    private static final String COL_mUrl = "mUrl";
    private static final String COL_mTypeId = "mTypeId";
    private static final String COL_mRatingsAverage = "mRatingsAverage";

    //Request to create last restaurants database
    private static final String CREATE_BDD_DERNIERS_RESTAURANTS = "CREATE TABLE " + TABLE_DERNIERS_RESTAURANTS + " ("
            + COL_mId + " INTEGER PRIMARY KEY NOT NULL, "
            + COL_mlat + " REAL NOT NULL, "
            + COL_mLon + " REAL NOT NULL, "
            + COL_mAddress + " TEXT NOT NULL, "
            + COL_mName + " TEXT NOT NULL, "
            + COL_mThumbnailImgUrl + " TEXT NOT NULL, "
            + COL_mUrl + " TEXT NOT NULL, "
            + COL_mTypeId + " INTEGER NOT NULL, "
            + COL_mRatingsAverage + " REAL NOT NULL"+
    ");";

    //Request to create favorite restaurant database
    private static final String CREATE_BDD_FAVORIS_RESTAURANTS = "CREATE TABLE " + TABLE_FAVORIS_RESTAURANTS + " ("
            + COL_mId + " INTEGER PRIMARY KEY NOT NULL, "
            + COL_mlat + " REAL NOT NULL, "
            + COL_mLon + " REAL NOT NULL, "
            + COL_mAddress + " TEXT NOT NULL, "
            + COL_mName + " TEXT NOT NULL, "
            + COL_mThumbnailImgUrl + " TEXT NOT NULL, "
            + COL_mUrl + " TEXT NOT NULL, "
            + COL_mTypeId + " INTEGER NOT NULL, "
            + COL_mRatingsAverage + " REAL NOT NULL"+
            ");";

    public MaBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Called when the database version is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //delete and recreate tables
        db.execSQL("DROP TABLE " + TABLE_DERNIERS_RESTAURANTS + ";");
        db.execSQL("DROP TABLE " + TABLE_FAVORIS_RESTAURANTS + ";");
        onCreate(db);
    }

    //Called on the first creation of the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BDD_DERNIERS_RESTAURANTS);
        sqLiteDatabase.execSQL(CREATE_BDD_FAVORIS_RESTAURANTS);
    }
}
