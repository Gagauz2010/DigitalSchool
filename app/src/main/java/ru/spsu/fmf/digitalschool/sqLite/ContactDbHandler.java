package ru.spsu.fmf.digitalschool.sqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import ru.spsu.fmf.digitalschool.structures.Contact;

public class ContactDbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String
            DATABASE_NAME   = "cantactManager",
            TABLE_NAME      = "cantacts",
            KEY_ID          = "id",
            KEY_NAME        = "name",
            KEY_PHONE       = "phone",
            KEY_EMAIL       = "email",
            KEY_ADDRESS     = "address",
            KEY_IMAGE       = "imageUri";

    public ContactDbHandler(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_NAME + " TEXT," + KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT, " + KEY_ADDRESS +
                " TEXT," + KEY_IMAGE + " TEXT)");
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void createContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGE, contact.getImageURI().toString());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_IMAGE}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));

        cursor.close();
        db.close();

        return contact;
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    public int getContactsCount(){
        //SELECT * FROM CONTACTS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int updateContact (Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGE, contact.getImageURI().toString());

        int rowsAffected = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(contact.getID())});
        db.close();

        return rowsAffected;
    }

    public List<Contact> getAllContacts(){
        List<Contact> contacts = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5))));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return contacts;
    }

    public boolean contactExist(String name){
        List<Contact> contacts = new ArrayList<>(this.getAllContacts());

        for (ru.spsu.fmf.digitalschool.structures.Contact contact : contacts) {
            if (name.compareToIgnoreCase(contact.getName()) == 0)
                return true;
        }

        return false;
    }
}
