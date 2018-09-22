package br.pucpr.appdev.recycler.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CityDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "cities.sqlite";
    private static final String DB_TABLE = "cities";
    private static final String COL_ID = "_id";
    private static final String COL_CITY = "city";
    private static final String COL_PEOPLE = "people";
    private static final int DB_VERSION = 1;

    private Context context;

    public CityDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table if not exists " + DB_TABLE + "(" +
                     COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     COL_CITY + " TEXT, " +
                     COL_PEOPLE + " INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public long addCity(City city) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_CITY, city.getName());
        values.put(COL_PEOPLE, city.getPeople());

        long id = db.insert(DB_TABLE, "", values);
        city.setId(id);

        db.close();

        return id;
    }

    public int removeCity(City city) {

        SQLiteDatabase db = getWritableDatabase();

        String _id = String.valueOf(city.getId());
        int count = db.delete(DB_TABLE, COL_ID+"=?", new String[]{_id});

        db.close();

        return count;
    }

    public void updateCity(City city) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_CITY, city.getName());
        values.put(COL_PEOPLE, city.getPeople());

        String _id = String.valueOf(city.getId());
        db.update(DB_TABLE, values, COL_ID+"=?", new String[]{_id});

        db.close();
    }

    public List<City> getAll() {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                DB_TABLE,
                null,
                null,
                null,
                null,
                null,
                COL_CITY);
        List<City> cities = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                City city = new City(
                        cursor.getString(cursor.getColumnIndex(COL_CITY)),
                        cursor.getInt(cursor.getColumnIndex(COL_PEOPLE))
                );
                city.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                cities.add(city);
            } while (cursor.moveToNext());
        }

        db.close();

        return cities;
    }

    public int removeAll(){

        SQLiteDatabase db = getWritableDatabase();

        int count = db.delete(DB_TABLE, null, null);

        db.close();

        return count;

    }

    public City getCity (City ct){

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(DB_TABLE, null, COL_CITY + "=?", new String[]{ ct.getName() }, null,null, COL_CITY);

        City city = null;

        if(cursor.moveToFirst()){
            do{
                city = new City(
                        cursor.getString(cursor.getColumnIndex(COL_CITY)),
                        cursor.getInt(cursor.getColumnIndex(COL_PEOPLE))
                );

                city.setId(cursor.getColumnIndex(COL_ID));
            }while (cursor.moveToNext());
        }

        db.close();

        return city;
    }

    public List<City> firstWordCity(String first){

        SQLiteDatabase db = getReadableDatabase();

//        Cursor cursor = db.query(DB_TABLE, null, COL_CITY + " LIKE '?%'", new String[]{first}, null,null, COL_CITY);
        Cursor cursor = db.query(DB_TABLE, null, COL_CITY + " LIKE '?'", new String[]{ "%" + first + "%" }, null,null, COL_CITY);

        List<City> cities = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                City city = new City(
                        cursor.getString(cursor.getColumnIndex(COL_CITY)),
                        cursor.getInt(cursor.getColumnIndex(COL_PEOPLE))
                );

                city.setId(cursor.getColumnIndex(COL_ID));
                cities.add(city);
            }while (cursor.moveToNext());
        }

        db.close();

        return cities;
    }

    public List<City> findPeople(int people){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(DB_TABLE, null, COL_PEOPLE + ">= ?", new String[]{String.valueOf(people)}, null,null, COL_CITY);

        List<City> cities = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                City city = new City(
                        cursor.getString(cursor.getColumnIndex(COL_CITY)),
                        cursor.getInt(cursor.getColumnIndex(COL_PEOPLE))
                );

                city.setId(cursor.getColumnIndex(COL_ID));
                cities.add(city);
            }while (cursor.moveToNext());
        }

        db.close();

        return cities;
    }
}
