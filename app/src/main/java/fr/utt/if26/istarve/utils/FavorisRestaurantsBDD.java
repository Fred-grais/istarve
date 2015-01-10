package fr.utt.if26.istarve.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.utt.if26.istarve.models.Restaurant;

public class FavorisRestaurantsBDD {


    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "restaurants.db";

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


    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    public FavorisRestaurantsBDD(Context context){
        //On créer la BDD et sa table

        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertRestaurant(Restaurant restaurant){
        if(!ExistRestaurant(restaurant.getmId())) {

            ContentValues values = new ContentValues();
            values.put(COL_mId, restaurant.getmId());
            values.put(COL_mlat, restaurant.getmLat());
            values.put(COL_mLon, restaurant.getmLon());
            values.put(COL_mAddress, restaurant.getmAddress());
            values.put(COL_mName, restaurant.getmName());
            values.put(COL_mThumbnailImgUrl, restaurant.getmThumbnailImgUrl());
            values.put(COL_mUrl, restaurant.getmUrl());
            values.put(COL_mTypeId, restaurant.getmTypeId());
            values.put(COL_mRatingsAverage, restaurant.getmRatingsAverage());
            //on insère l'objet dans la BDD via le ContentValues

            return bdd.insert(TABLE_FAVORIS_RESTAURANTS, null, values);
        }
        else return 0;
    }

    public int removeRestaurantWithID(int id){
        //Suppression d'un restaurant de la BDD grâce à l'ID
        return bdd.delete(TABLE_FAVORIS_RESTAURANTS, COL_mId + " = " +id, null);
    }

    public Restaurant getRestaurantWithId(int id){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_FAVORIS_RESTAURANTS, null,  COL_mId + "=" + id, null, null, null, null);
        return cursorToRestaurant(c);
    }

    public boolean ExistRestaurant(int id){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_FAVORIS_RESTAURANTS, null,  COL_mId + "=" + id, null, null, null, null);
        if(c.getCount()==0)
            return false;
        else
        return true;
    }

    public void deleteFirstRow()
    {
        Cursor cursor = bdd.query(TABLE_FAVORIS_RESTAURANTS, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            String rowId = cursor.getString(cursor.getColumnIndex(COL_mId));

            bdd.delete(TABLE_FAVORIS_RESTAURANTS, COL_mId + "=?",  new String[]{rowId});
        }
    }

    public ArrayList<Restaurant> getAllRestaurants() {
        Cursor c = bdd.query(TABLE_FAVORIS_RESTAURANTS, null, null, null, null,null, null);
        return cursorToRestaurants(c);
    }

    public int nbRow() {
        Cursor c = bdd.query(TABLE_FAVORIS_RESTAURANTS, null, null, null, null,null, null);
        return c.getCount();
    }



    //Cette méthode permet de convertir un cursor en un livre
    private Restaurant cursorToRestaurant(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un restaurant
        Restaurant restaurant = new Restaurant(c.getInt(0), c.getFloat(1), c.getFloat(2), c.getString(3), c.getString(4), c.getString(5),c.getString(6), c.getInt(7), c.getInt(8));
        //On ferme le cursor
        c.close();

        //On retourne le restaurant
        return restaurant;
    }

    private ArrayList<Restaurant> cursorToRestaurants(Cursor c) {
// Si la requête ne renvoie pas de résultat
        if (c.getCount() == 0)
            return new ArrayList<Restaurant>(0);
        ArrayList<Restaurant> retRestaurants = new ArrayList<Restaurant>(c.getCount());
        c.moveToFirst();
        do {
            Restaurant restaurant = new Restaurant(c.getInt(0), c.getFloat(1), c.getFloat(2), c.getString(3), c.getString(4), c.getString(5),c.getString(6), c.getInt(7), c.getInt(8));
            retRestaurants.add(restaurant);
        } while (c.moveToNext());
// Ferme le curseur pour libérer les ressources
        c.close();
        return retRestaurants;
    }

}
