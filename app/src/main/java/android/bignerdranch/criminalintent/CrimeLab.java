package android.bignerdranch.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeCursorWrapper;
import database.CrimeDbSchema;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;  // will use this in chapter 16
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        // stores the context for later use
        mContext = context.getApplicationContext();

        // causes CrimeBaseHelper to do some important work...
        // gets us a reference to an actual database!
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab get(Context context) {
        if(sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

    public void updateCrime(Crime c) {
        String uuidString = c.getID().toString();
        ContentValues values = getContentValues(c);

        mDatabase.update(
                CrimeDbSchema.CrimeTable.NAME,  // name of table with row to update
                values,                         // the values to update the row with
                // building the where clause for the SQL command
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[] { uuidString }
        );
    }

    public void deleteCrime(Crime c){
        String uuidString = c.getID().toString();
        ContentValues values = getContentValues(c);
        mDatabase.delete(
                CrimeDbSchema.CrimeTable.NAME,  // name of table with row to update
                // building the where clause for the SQL command
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[] { uuidString }
        );
    }




    public List<Crime> getCrimes() {
        // return new ArrayList<>();  this is replaced with:
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        // return null;   replace this with:
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if(cursor.getCount() == 0)  // crime wasn't found
                return null;

            cursor.moveToFirst();       // go to top row
            return cursor.getCrime();   // get it and return it
        }
        finally {
            cursor.close();             // close the cursor
        }
    }




    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();

        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getID().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,   // selects all columns
                whereClause,
                whereArgs,
                null,   // groupBy
                null,   // having
                null    // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
}

