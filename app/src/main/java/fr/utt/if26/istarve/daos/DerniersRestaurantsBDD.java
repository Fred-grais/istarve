package fr.utt.if26.istarve.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fr.utt.if26.istarve.models.Restaurant;

/**
 *
 */
public class DerniersRestaurantsBDD {

   private static final int VERSION_BDD = 3;
   private static final String NOM_BDD = "restaurants.db";

    private static final String TABLE_DERNIERS_RESTAURANTS = "table_derniers_restaurants";
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

        public DerniersRestaurantsBDD(Context context){
            //Create the database
            maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);

        }

        public SQLiteDatabase getBDD(){
        return bdd;
        }

        public void open(){
            //open a writable database
            bdd = maBaseSQLite.getWritableDatabase();
        }

        public void close(){
            //close the database access
            bdd.close();
        }

        //Insert a restaurant object into the database
        public long insertRestaurant(Restaurant restaurant){
            //the maximum number of rows is 5
            if(!ExistRestaurant(restaurant.getmId())) {
                    if(nbRow()>=5){
                    deleteFirstRow();
                }
                //Create the values
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
                //insert the ContentValues into the table
                return bdd.insert(TABLE_DERNIERS_RESTAURANTS, null, values);
            }
            else return 0;
        }

        //Search with the id, if the restaurant is in the table
        public boolean ExistRestaurant(int id){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_DERNIERS_RESTAURANTS, null,  COL_mId + "=" + id, null, null, null, null);
        if(c.getCount()==0)
            return false;
        else
            return true;
        }

        //remove a restaurant with his id number
         public int removeRestaurantWithID(int id){
            //Suppression d'un restaurant de la BDD grâce à l'ID
            return bdd.delete(TABLE_DERNIERS_RESTAURANTS, COL_mId + " = " +id, null);
        }

        //get a restaurant with his id number
        public Restaurant getRestaurantWithId(int id){
            Cursor c = bdd.query(TABLE_DERNIERS_RESTAURANTS, null,  COL_mId + "=" + id, null, null, null, null);
            return cursorToRestaurant(c);
        }

        //delete the first row of the table
        public void deleteFirstRow()
        {
            Cursor cursor = bdd.query(TABLE_DERNIERS_RESTAURANTS, null, null, null, null, null, null);
            if(cursor.moveToFirst()) {
                //get the restaurant id
                String rowId = cursor.getString(cursor.getColumnIndex(COL_mId));
                //delete the restaurant at row 1
                bdd.delete(TABLE_DERNIERS_RESTAURANTS, COL_mId + "=?",  new String[]{rowId});
            }
        }

        //return all restaurants objects from the database
         public ArrayList<Restaurant> getAllRestaurants() {
        Cursor c = bdd.query(TABLE_DERNIERS_RESTAURANTS, null, null, null, null,null, null);
        return cursorToRestaurants(c);
        }


        //Return the number of restaurants
        public int nbRow() {
        Cursor c = bdd.query(TABLE_DERNIERS_RESTAURANTS, null, null, null, null,null, null);
        return c.getCount();
        }



        //Convert the cursor to a restaurant object
        private Restaurant cursorToRestaurant(Cursor c){
            //if not restaurant found, return null
            if (c.getCount() == 0)
                return null;
            c.moveToFirst();
            //Create the restaurant object
            Restaurant restaurant = new Restaurant(c.getInt(0), c.getFloat(1), c.getFloat(2), c.getString(3), c.getString(4), c.getString(5),c.getString(6), c.getInt(7), c.getInt(8));
            c.close();

            //return the restaurant
            return restaurant;
        }

    //Convert the cursor to a list of restaurant object
    private ArrayList<Restaurant> cursorToRestaurants(Cursor c) {
        //if not restaurant found an empty list
        if (c.getCount() == 0)
            return new ArrayList<Restaurant>(0);

        ArrayList<Restaurant> retRestaurants = new ArrayList<Restaurant>(c.getCount());
        c.moveToFirst();
        do {
            //Create the restaurant object and add to the list
            Restaurant restaurant = new Restaurant(c.getInt(0), c.getFloat(1), c.getFloat(2), c.getString(3), c.getString(4), c.getString(5),c.getString(6), c.getInt(7), c.getInt(8));
            retRestaurants.add(restaurant);
        } while (c.moveToNext());
        c.close();

        //return the list of restaurant object
        return retRestaurants;
    }


}
