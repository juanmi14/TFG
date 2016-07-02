package jmvdeveloper.eucast.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jmvdeveloper.eucast.Utils.Globals;
import jmvdeveloper.eucast.logic.Antibiotic;
import jmvdeveloper.eucast.logic.AntibioticFamily;
import jmvdeveloper.eucast.logic.Note;
import jmvdeveloper.eucast.logic.S;
import jmvdeveloper.eucast.logic.R;
import jmvdeveloper.eucast.logic.SensitivityQuery;
import jmvdeveloper.eucast.logic.ZoneDiameterBreakpoint;

/**
 * Created by Juanmi on 17/06/2016.
 */
public class BDHandler extends SQLiteOpenHelper {
    private Context ctx;
    //tables names
    private final String FAMILIES_NAMES_TABLE = "FAMILIES_NAMES";
    private final String ANTIBIOTIC_NAMES_TABLE = "ANTIBIOTIC_NAMES";
    private final String SENSIBILITY_QUERY_TABLE = "SENSIBILITY_QUERY";
    private final String NOTES_TABLE = "NOTES";
    private final String ANTIBIOTIC_TABLE = "ANTIBIOTIC";
    private final String ANTIBIOTIC_NOTES_TABLE = "ANTIBIOTIC_NOTES";
    private final String ANTIBIOTIC_FAMILY_TABLE = "ANTIBIOTIC_FAMILY";
    private final String ANTIBIOTIC_FAMILY_NOTES_TABLE = "ANTIBIOTIC_FAMILY_NOTES";
    //param names
    private final String FAMILIES_NAMES_NAME = "Name";
    private final String ANTIBIOTIC_NAMES_NAME = "Name";

    public BDHandler(Context context) {
        super(context, "EUCAST", null, Globals.BD_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertarIniciales(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){ db.execSQL("PRAGMA foreign_keys = ON;"); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        insertarIniciales(db);
    }

    private void insertarIniciales(SQLiteDatabase db) {
        //FAMILY_NAMES
        String families = "CREATE TABLE `" + FAMILIES_NAMES_TABLE + "` (" +
                "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`" + FAMILIES_NAMES_NAME + "` TEXT NOT NULL" +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + FAMILIES_NAMES_TABLE + "';");
        db.execSQL(families);

        //ANTIBIOTIC_NAMES
        String antimicroFamilies = "CREATE TABLE `" + ANTIBIOTIC_NAMES_TABLE + "` (" +
                "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`" + ANTIBIOTIC_NAMES_NAME + "` TEXT NOT NULL" +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + ANTIBIOTIC_NAMES_TABLE + "';");
        db.execSQL(antimicroFamilies);

        //NOTES
        String notes = "CREATE TABLE '" + NOTES_TABLE + "' ( " +
                "`Id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`IdNote`TEXT NOT NULL, " +
                "`Note`TEXT NOT NULL " +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + NOTES_TABLE + "';");
        db.execSQL(notes);

        //ANTIBIOTIC
        String antibiotic = "CREATE TABLE '" + ANTIBIOTIC_TABLE + "' ( " +
                "`Id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`Name`TEXT NOT NULL, " +
                "`S`TEXT, " +
                "`R`TEXT, " +
                "`Link`TEXT " +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + ANTIBIOTIC_TABLE + "';");
        db.execSQL(antibiotic);

        //ANTIBIOTIC_FAMILY
        String antibioticFamily = "CREATE TABLE '" + ANTIBIOTIC_FAMILY_TABLE + "' ( " +
                "`Id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`Name`TEXT NOT NULL " +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + ANTIBIOTIC_FAMILY_TABLE + "';");
        db.execSQL(antibioticFamily);

        //SENSIBILITY_QUERY
        String sensibilityQuery = "CREATE TABLE `" + SENSIBILITY_QUERY_TABLE + "` (" +
                "`Id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`" + SensitivityQuery.SENSIBILITY + "` TEXT NOT NULL, " +
                "`" + SensitivityQuery.FAMILY + "` TEXT NOT NULL, " +
                "`" + SensitivityQuery.DIAMETER + "` TEXT NOT NULL, " +
                "`" + SensitivityQuery.ANTIBIOTIC_NAME + "` TEXT NOT NULL, " +
                "`" + SensitivityQuery.ANTIBIOTIC_FAMILY + "` INTEGER NOT NULL, " +
                "`" + SensitivityQuery.ANTIBIOTIC + "` INTEGER NOT NULL " +
//                "FOREIGN KEY(`" + SensitivityQuery.ANTIBIOTIC_FAMILY + "`) REFERENCES " + ANTIBIOTIC_FAMILY_TABLE + "(Id), " +
//                "FOREIGN KEY(`" + SensitivityQuery.ANTIBIOTIC + "`) REFERENCES " + ANTIBIOTIC_TABLE + "(Id)" +
                ");";
        db.execSQL("DROP TABLE IF EXISTS '" + SENSIBILITY_QUERY_TABLE + "';");
        db.execSQL(sensibilityQuery);
//        String sensibilityQuery = "CREATE TABLE `" + SENSIBILITY_QUERY_TABLE + "` (" +
//                "`" + SensitivityQuery.SENSIBILITY + "` TEXT NOT NULL, " +
//                "`" + SensitivityQuery.FAMILY + "` TEXT NOT NULL, " +
//                "`" + SensitivityQuery.DIAMETER + "` TEXT NOT NULL, " +
//                "`" + SensitivityQuery.ANTIBIOTIC_NAME + "` TEXT NOT NULL, " +
//                "`" + SensitivityQuery.ANTIBIOTIC_FAMILY + "` INTEGER NOT NULL, " +
//                "`" + SensitivityQuery.ANTIBIOTIC + "` INTEGER NOT NULL, " +
//                "PRIMARY KEY(" + SensitivityQuery.FAMILY + "," + SensitivityQuery.DIAMETER + "," + SensitivityQuery.ANTIBIOTIC_NAME + "), " +
//                "FOREIGN KEY(`" + SensitivityQuery.ANTIBIOTIC_FAMILY + "`) REFERENCES " + ANTIBIOTIC_FAMILY_TABLE + "(Id), " +
//                "FOREIGN KEY(`" + SensitivityQuery.ANTIBIOTIC + "`) REFERENCES " + ANTIBIOTIC_TABLE + "(Id)" +
//                ");";
//        db.execSQL("DROP TABLE IF EXISTS '" + SENSIBILITY_QUERY_TABLE + "';");
//        db.execSQL(sensibilityQuery);

        //ANTIBIOTIC_FAMILY_NOTES
        String antibioticFamilyNotes = "CREATE TABLE '" + ANTIBIOTIC_FAMILY_NOTES_TABLE + "' ( " +
                "`SensQuery`INTEGER NOT NULL, " +
                "`AntibioticFamily`INTEGER NOT NULL, " +
                "`Note`INTEGER NOT NULL, " +
                "PRIMARY KEY(SensQuery,AntibioticFamily,Note), " +
                "FOREIGN KEY(`SensQuery`) REFERENCES " + SENSIBILITY_QUERY_TABLE + "(Id), " +
                "FOREIGN KEY(`AntibioticFamily`) REFERENCES " + ANTIBIOTIC_FAMILY_TABLE + "(Id), " +
                "FOREIGN KEY(`Note`) REFERENCES NOTES(Id) " +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + ANTIBIOTIC_FAMILY_NOTES_TABLE + "';");
        db.execSQL(antibioticFamilyNotes);

        //ANTIBIOTIC_NOTES
        String antibioticNotes = "CREATE TABLE '" + ANTIBIOTIC_NOTES_TABLE + "' ( " +
                "`SensQuery`INTEGER NOT NULL, " +
                "`Antibiotic`INTEGER NOT NULL, " +
                "`Note`INTEGER NOT NULL, " +
                "PRIMARY KEY(SensQuery,Antibiotic,Note), " +
                "FOREIGN KEY(`SensQuery`) REFERENCES " + SENSIBILITY_QUERY_TABLE + "(Id), " +
                "FOREIGN KEY(`Antibiotic`) REFERENCES " + ANTIBIOTIC_TABLE + "(Id), " +
                "FOREIGN KEY(`Note`) REFERENCES NOTES(Id) " +
                ")";
        db.execSQL("DROP TABLE IF EXISTS '" + ANTIBIOTIC_NOTES_TABLE + "';");
        db.execSQL(antibioticNotes);


//        //ELEMENTOS INICIALES
//        db.execSQL("INSERT INTO `" + FAMILIES_NAMES_TABLE + "` VALUES(1, 'Enterobacteriaceae');");
//        db.execSQL("INSERT INTO `" + ANTIBIOTIC_NAMES_TABLE + "` VALUES(1, 'Ampicillin');");
    }

    public SQLiteDatabase obtenerManejadorLectura() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase obtenerManejadorEscritura() {
        return this.getWritableDatabase();
    }

    public void abrir() {
        this.getWritableDatabase();
    }

    public void cerrar() {
        this.close();
    }

    //------------------------------FAMILY NAMES
    public List<String> getFamilyNames(){
        ArrayList<String> families = new ArrayList();
        String query = "SELECT * FROM " + FAMILIES_NAMES_TABLE;

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                families.add(cursor.getString(cursor.getColumnIndex(FAMILIES_NAMES_NAME)));
            } while (cursor.moveToNext());
        }

        Log.d("getFamilyNames", "size: " + families.size());

        db.close();
        cursor.close();
        return families;
    }

    public boolean thereIsFamilyNames(){
        String query = "SELECT COUNT(Id) FROM " + FAMILIES_NAMES_TABLE;
        SQLiteDatabase db = obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
            return cursor.getInt(0) > 0;
        else
            return false;
    }

    public boolean insertFamiliNames(List<String> families){
        boolean ok = false;
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        for(String fm: families){
            ok = insertFamilyName(fm, db, false) != -1;
        }
        db.close();
        return ok;
    }

    public Integer insertFamilyName(String family, SQLiteDatabase db, boolean close){
        if(isFamilyName(family))
            return -1;
        else{
//            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

//            values.put("Id", null);
            values.put(FAMILIES_NAMES_NAME, family);

            long ident = db.insert(FAMILIES_NAMES_TABLE, null, values);

//            if (ident != -1) {
//                articuloSupermercado.setId((int)ident);
//            }

            if(close)
                db.close();
            return (int)ident;
        }
    }

    public boolean isFamilyName(String family){
        boolean ok;
        String query = "SELECT " + FAMILIES_NAMES_NAME + " FROM " + FAMILIES_NAMES_TABLE + " where " + FAMILIES_NAMES_NAME + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{family});

        if(cursor.moveToFirst())
            ok = true;
        else
            ok = false;

        //lectura.close();
        cursor.close();
        return ok;
    }

    ////------------------------------ANTIBIOTIC NAMES
    public List<String> getAntibioticNames(){
        ArrayList<String> antibiotics = new ArrayList();
        String query = "SELECT * FROM " + ANTIBIOTIC_NAMES_TABLE;

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                antibiotics.add(cursor.getString(cursor.getColumnIndex(ANTIBIOTIC_NAMES_NAME)));
            } while (cursor.moveToNext());
        }

        Log.d("getFamilyNames", "size: " + antibiotics.size());

        db.close();
        cursor.close();
        return antibiotics;
    }

    public boolean insertAntibioticNames(List<String> antibiotics){
        boolean ok = false;
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        for(String afm: antibiotics){
            ok = insertAntibioticName(afm, db, false) != -1;
        }
        db.close();
        return ok;
    }

    public Integer insertAntibioticName(String antibiotic, SQLiteDatabase db, boolean close){
        if(isAntibioticName(antibiotic))
            return -1;
        else{
//            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

//            values.put("Id", null);
            values.put(ANTIBIOTIC_NAMES_NAME, antibiotic);

            long ident = db.insert(ANTIBIOTIC_NAMES_TABLE, null, values);

//            if (ident != -1) {
//                articuloSupermercado.setId((int)ident);
//            }

            if(close)
                db.close();
            return (int)ident;
        }
    }

    public boolean isAntibioticName(String antibiotic){
        boolean ok;
        String query = "SELECT " + ANTIBIOTIC_NAMES_NAME + " FROM " + ANTIBIOTIC_NAMES_TABLE + " where " + ANTIBIOTIC_NAMES_NAME + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{antibiotic});

        if(cursor.moveToFirst())
            ok = true;
        else
            ok = false;

        //lectura.close();
        cursor.close();
        return ok;
    }

    ////------------------------------SENSIBILITY_QUERY
    //necesario haber comprobado la existencia del elemento si no se quieren repetidos
    public void insertSensibilityQuery(SensitivityQuery sq){
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        //1-antibiótico, familiaAtibiótica
        //2-sensibilityQuery
        //3-notas
        //antibiotic
        int idAntibiotic = insertAntibiotic(sq.getAntibiotic(), db, false);
        //antibioticFamily
        int idAntibioticFamily = insertAntibioticFamily(sq.getAntibioticFamily(), db, false);
        //SensitivityQuery
        ContentValues values = new ContentValues();
        values.put(SensitivityQuery.SENSIBILITY, sq.getSensitivity());
        values.put(SensitivityQuery.FAMILY, sq.getFamily());
        values.put(SensitivityQuery.ANTIBIOTIC_NAME, sq.getAntibioticName());
        values.put(SensitivityQuery.DIAMETER, sq.getDiameter());
        values.put(SensitivityQuery.ANTIBIOTIC, idAntibiotic);
        values.put(SensitivityQuery.ANTIBIOTIC_FAMILY, idAntibioticFamily);
        int idSensQuery = (int) db.insert(SENSIBILITY_QUERY_TABLE, null, values);
        //notas y sus relaciones
        //antibioticNotes
        if (sq.getAntibiotic().getNotes() != null) {
            for (Note n : sq.getAntibiotic().getNotes()) {
                int idN = insertNote(n, db, false);
                insertAntibioticNote(idSensQuery, idAntibiotic, idN, db, false);
            }
        }
        if (sq.getAntibiotic().getZoneDiameterBreakpoint().getS().getNotes() != null) {
            for (Note n : sq.getAntibiotic().getZoneDiameterBreakpoint().getS().getNotes()) {
                int idN = insertNote(n, db, false);
                insertAntibioticNote(idSensQuery, idAntibiotic, idN, db, false);
            }
        }
        if (sq.getAntibiotic().getZoneDiameterBreakpoint().getR().getNotes() != null) {
            for (Note n : sq.getAntibiotic().getZoneDiameterBreakpoint().getR().getNotes()) {
                int idN = insertNote(n, db, false);
                insertAntibioticNote(idSensQuery, idAntibiotic, idN, db, false);
            }
        }
        //antibioticFamilyNotes
        if (sq.getAntibioticFamily().getOwnNotes() != null) {
            for (Note n : sq.getAntibioticFamily().getOwnNotes()) {
                int idN = insertNote(n, db, false);
                insertAntibioticFamilyNote(idSensQuery, idAntibioticFamily, idN, db, false);
            }
        }
        db.close();
    }

    public boolean deleteSensibilityQuery(SensitivityQuery sq, SQLiteDatabase db, boolean close){
        //borrar las notas relacionadas
        //SQLiteDatabase db = obtenerManejadorEscritura();
        int idSq = getSensibilityQueryId(sq.getFamily(), sq.getAntibioticName(), sq.getDiameter());
        Log.v("__SensibilityQueryDel_", String.valueOf(idSq));
        deleteAntibioticNoteBySensQuery(idSq, db, false);
        deleteAntibioticFamilyNoteBySensQuery(idSq, db, false);
        int rows = db.delete(SENSIBILITY_QUERY_TABLE,
                        SensitivityQuery.FAMILY + " = '" + sq.getFamily() + "' AND " +
                        SensitivityQuery.ANTIBIOTIC_NAME + " = '" + sq.getAntibioticName() + "' AND " +
                        SensitivityQuery.DIAMETER + " = '" + sq.getDiameter() + "'",
                null);
        if(close)
            db.close();
        return rows > 0;
    }

    public boolean isSensibilityQuery(SensitivityQuery sq){
        return isSensibilityQuery(sq.getFamily(), sq.getAntibioticName(), sq.getDiameter());
    }

    public boolean isSensibilityQuery(String family, String antibiotic, String diameter){
        SQLiteDatabase db = obtenerManejadorLectura();
        String query = "SELECT COUNT(Id) FROM " + SENSIBILITY_QUERY_TABLE +
                " WHERE " + SensitivityQuery.FAMILY + " = ? AND " +
                SensitivityQuery.ANTIBIOTIC_NAME + " = ? AND " +
                SensitivityQuery.DIAMETER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{family, antibiotic, diameter});
        int count = 0;
        if(cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        //db.close();
        cursor.close();
        return count > 0;
    }

    public int getSensibilityQueryId(String family, String antibiotic, String diameter){
        SQLiteDatabase db = obtenerManejadorLectura();
        String query = "SELECT Id FROM " + SENSIBILITY_QUERY_TABLE +
                " WHERE " + SensitivityQuery.FAMILY + " = ? AND " +
                SensitivityQuery.ANTIBIOTIC_NAME + " = ? AND " +
                SensitivityQuery.DIAMETER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{family, antibiotic, diameter});
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        db.close();
        cursor.close();
        return 0;
    }

    public SensitivityQuery getSensibilityQuery(String family, String antibiotic, String diameter){
        SQLiteDatabase db = obtenerManejadorLectura();
        String query = "SELECT * FROM " + SENSIBILITY_QUERY_TABLE +
                " WHERE " + SensitivityQuery.FAMILY + " = ? AND " +
                SensitivityQuery.ANTIBIOTIC_NAME + " = ? AND " +
                SensitivityQuery.DIAMETER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{family, antibiotic, diameter});
        if(cursor.moveToFirst()) {
            SensitivityQuery sq = new SensitivityQuery();
            sq.setSensitivity(cursor.getString(cursor.getColumnIndex(SensitivityQuery.SENSIBILITY)));
            sq.setFamily(cursor.getString(cursor.getColumnIndex(SensitivityQuery.FAMILY)));
            sq.setAntibioticName(cursor.getString(cursor.getColumnIndex(SensitivityQuery.ANTIBIOTIC_NAME)));
            sq.setDiameter(cursor.getString(cursor.getColumnIndex(SensitivityQuery.DIAMETER)));

            int idAntibiotic = cursor.getInt(cursor.getColumnIndex(SensitivityQuery.ANTIBIOTIC));
            sq.setAntibiotic(getAntibiotic(idAntibiotic, db, false));
            int idAntibioticFamily = cursor.getInt(cursor.getColumnIndex(SensitivityQuery.ANTIBIOTIC));
            sq.setAntibioticFamily(getAntibioticFamily(idAntibioticFamily, db, false));
            return sq;
        }
        db.close();
        cursor.close();
        return null;
    }

    public SensitivityQuery getSensibilityQueryComplex(String family, String antibiotic, String diameter){
        SQLiteDatabase db = obtenerManejadorLectura();
        String query = "select * " +
                "from sensibility_query as sq " +
                "join antibiotic_family antf on antf.id = sq.AntibioticFamily " +
                "join antibiotic as ant on ant.id = sq.Antibiotic " +
                "where sq.family = ? and sq.antibioticname = ? and sq.diameter = ?";
//        String query = "SELECT * FROM " + SENSIBILITY_QUERY_TABLE +
//                " WHERE " + SensitivityQuery.FAMILY + " = ? AND " +
//                SensitivityQuery.ANTIBIOTIC_NAME + " = ? AND " +
//                SensitivityQuery.DIAMETER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{family, antibiotic, diameter});
        if(cursor.moveToFirst()) {
            SensitivityQuery sq = new SensitivityQuery();
            //1-sq.sensibility
            //2-sq.family
            //3-sq.diameter
            //0-
            //0-
            //0-
            sq.setSensitivity(cursor.getString(1));
            sq.setFamily(cursor.getString(2));
            sq.setDiameter(cursor.getString(3));
            sq.setAntibioticName(cursor.getString(4));
            AntibioticFamily antf = new AntibioticFamily();
            antf.setName(cursor.getString(8));
            sq.setAntibioticFamily(antf);
            Antibiotic ant = new Antibiotic();
            ant.setName(cursor.getString(10));
            S s = new S();
            s.setValue(cursor.getString(11));
            R r = new R();
            r.setValue(cursor.getString(12));
            ant.setZoneDiameterBreakpoint(new ZoneDiameterBreakpoint(s, r));
            ant.setLink(cursor.getString(13));
            sq.setAntibiotic(ant);
            return sq;
        }
        db.close();
        cursor.close();
        return null;
    }

    public List<SensitivityQuery> getSensibilityQueries(){
        ArrayList<SensitivityQuery> lista = new ArrayList<SensitivityQuery>();
        String query = "SELECT * FROM " + SENSIBILITY_QUERY_TABLE;

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SensitivityQuery sq = new SensitivityQuery();
                sq.setSensitivity(cursor.getString(cursor.getColumnIndex(SensitivityQuery.SENSIBILITY)));
                sq.setFamily(cursor.getString(cursor.getColumnIndex(SensitivityQuery.FAMILY)));
                sq.setAntibioticName(cursor.getString(cursor.getColumnIndex(SensitivityQuery.ANTIBIOTIC_NAME)));
                sq.setDiameter(cursor.getString(cursor.getColumnIndex(SensitivityQuery.DIAMETER)));

                int idAntibiotic = cursor.getInt(cursor.getColumnIndex(SensitivityQuery.ANTIBIOTIC));
                sq.setAntibiotic(getAntibiotic(idAntibiotic, db, false));
                int idAntibioticFamily = cursor.getInt(cursor.getColumnIndex(SensitivityQuery.ANTIBIOTIC));
                sq.setAntibioticFamily(getAntibioticFamily(idAntibioticFamily, db, false));
                lista.add(sq);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulos()", lista.toString());
        db.close();
        cursor.close();
        return lista;
    }

    //------------------------------NOTES
    public Integer insertNote(Note note, SQLiteDatabase db, boolean close){
        int id = getNoteId(note);
        if(id != -1)
            return id;
        else{
            ContentValues values = new ContentValues();
            values.put("IdNote", note.getId());
            values.put("Note", note.getValue());

            long ident = db.insert(NOTES_TABLE, null, values);

            if(close)
                db.close();
            return (int)ident;
        }
    }

    public Integer getNoteId(Note note){
        int id;
        String query = "SELECT Id FROM " + NOTES_TABLE + " where IdNote = ? AND Note = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{note.getId(), note.getValue()});

        if(cursor.moveToFirst())
            id = cursor.getInt(0);
        else
            id = -1;
        //lectura.close();
        cursor.close();

        return id;
    }

    public Note getNoteById(int id){
        Note note = new Note();
        String query = "SELECT IdNote, Note FROM " + NOTES_TABLE + " where Id = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{String.valueOf(id)});

        if(cursor.moveToFirst()) {
            note.setId(cursor.getString(cursor.getColumnIndex("IdNote")));
            note.setValue(cursor.getString(cursor.getColumnIndex("Note")));
        }else
            note = null;
        //lectura.close();
        cursor.close();

        return note;
    }

    public int deleteAntibioticNoteBySensQuery(int idSensQuery, SQLiteDatabase db, boolean close){
        int rows = db.delete(ANTIBIOTIC_NOTES_TABLE, "SensQuery=" + idSensQuery, null);
        if(close)
            db.close();
        Log.v("__AntibioticRowsDeleted", String.valueOf(rows));
        return rows;
    }

    public int deleteAntibioticFamilyNoteBySensQuery(int idSensQuery, SQLiteDatabase db, boolean close){
        int rows = db.delete(ANTIBIOTIC_FAMILY_NOTES_TABLE, "SensQuery=" + idSensQuery, null);
        if(close)
            db.close();
        Log.v("__AntibioticRowsDeleted", String.valueOf(rows));
        return rows;
    }

    public List<Note> getAntibioticFamilyNotes(SensitivityQuery sq){
        ArrayList<Note> notes = new ArrayList();
        String query = "SELECT Note FROM " + ANTIBIOTIC_FAMILY_NOTES_TABLE + " WHERE SensQuery = ? AND AntibioticFamily = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        int sqId = getSensibilityQueryId(sq.getFamily(), sq.getAntibioticName(), sq.getDiameter());
        int aId = getAntibioticFamilyId(sq.getAntibioticFamily());
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sqId), String.valueOf(aId)});

        if (cursor.moveToFirst()) {
            do {
                Note note = getNoteById(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Note"))));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        Log.d("getAntibFamNotes", "size: " + notes.size());

        db.close();
        cursor.close();
        return notes;
    }

    public List<Note> getAntibioticNotes(SensitivityQuery sq){
        ArrayList<Note> notes = new ArrayList();
        String query = "SELECT Note FROM " + ANTIBIOTIC_NOTES_TABLE + " WHERE SensQuery = ? AND Antibiotic = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        int sqId = getSensibilityQueryId(sq.getFamily(), sq.getAntibioticName(), sq.getDiameter());
        int aId = getAntibioticId(sq.getAntibiotic());
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sqId), String.valueOf(aId)});

        if (cursor.moveToFirst()) {
            do {
                Note note = getNoteById(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Note"))));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        Log.d("getAntibioticNotes", "size: " + notes.size());

        db.close();
        cursor.close();
        return notes;
    }

    //------------------------------ANTIBIOTIC
    public Integer insertAntibiotic(Antibiotic antibiotic, SQLiteDatabase db, boolean close){
        int id = getAntibioticId(antibiotic);
        if(id != -1)
            return id;

        ContentValues values = new ContentValues();
        values.put("Name", antibiotic.getName());
        values.put("S", antibiotic.getZoneDiameterBreakpoint().getS().getValue());
        values.put("R", antibiotic.getZoneDiameterBreakpoint().getR().getValue());
        values.put("Link", antibiotic.getLink());

        long ident = db.insert(ANTIBIOTIC_TABLE, null, values);

        if(close)
            db.close();
        return (int)ident;
    }

    public Integer getAntibioticId(Antibiotic antibiotic){
        int id;
        String query = "SELECT Id FROM " + ANTIBIOTIC_TABLE +
                " where Name = ? AND S = ? AND R = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query,
                new String[]{
                        antibiotic.getName(),
                        antibiotic.getZoneDiameterBreakpoint().getS().getValue(),
                        antibiotic.getZoneDiameterBreakpoint().getR().getValue()
                });

        if(cursor.moveToFirst())
            id = cursor.getInt(0);
        else
            id = -1;

        //lectura.close();
        cursor.close();

        return id;
    }

    public Antibiotic getAntibiotic(int id, SQLiteDatabase db, boolean close){
        Antibiotic a = new Antibiotic();
        String query = "SELECT * FROM " + ANTIBIOTIC_TABLE + " WHERE Id=?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if(c.moveToFirst()){
            a.setName(c.getString(c.getColumnIndex("Name")));
            S s = new S();
            s.setValue(c.getString(c.getColumnIndex("S")));
            R r = new R();
            r.setValue(c.getString(c.getColumnIndex("R")));
            a.setZoneDiameterBreakpoint(new ZoneDiameterBreakpoint(s, r));
            a.setLink(c.getString(c.getColumnIndex("Link")));
        }
        if(close)
            db.close();
        return a;
    }

    //------------------------------ANTIBIOTIC_FAMILY
    private int insertAntibioticFamily(AntibioticFamily antibioticFamily, SQLiteDatabase db, boolean close) {
        int id = getAntibioticFamilyId(antibioticFamily);
        if(id != -1)
            return id;

        ContentValues values = new ContentValues();
        values.put("Name", antibioticFamily.getName());

        long ident = db.insert(ANTIBIOTIC_FAMILY_TABLE, null, values);

        if(close)
            db.close();
        return (int)ident;
    }

    public Integer getAntibioticFamilyId(AntibioticFamily antibioticFamily){
        int id;
        String query = "SELECT Id FROM " + ANTIBIOTIC_FAMILY_TABLE + " where Name = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{antibioticFamily.getName()});

        if(cursor.moveToFirst())
            id = cursor.getInt(0);
        else
            id = -1;

        //lectura.close();
        cursor.close();

        return id;
    }

    public AntibioticFamily getAntibioticFamily(int id, SQLiteDatabase db, boolean close){
        AntibioticFamily af = new AntibioticFamily();
        String query = "SELECT * FROM " + ANTIBIOTIC_FAMILY_TABLE + " WHERE Id=?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(id)});
        if(c.moveToFirst()){
            af.setName(c.getString(c.getColumnIndex("Name")));
        }
        if(close)
            db.close();
        return af;
    }

    //------------------------------ANTIBIOTIC_NOTES
    private void insertAntibioticNote(int idSensQuery, int idAntibiotic, int idN, SQLiteDatabase db, boolean close) {
        ContentValues values = new ContentValues();
        values.put("SensQuery", idSensQuery);
        values.put("Antibiotic", idAntibiotic);
        values.put("Note", idN);

        db.insert(ANTIBIOTIC_NOTES_TABLE, null, values);

        if(close)
            db.close();
    }

    //------------------------------ANTIBIOTIC_FAMILY_NOTES
    private void insertAntibioticFamilyNote(int idSensQuery, int idAntibioticFamily, int idN, SQLiteDatabase db, boolean close) {
        ContentValues values = new ContentValues();
        values.put("SensQuery", idSensQuery);
        values.put("AntibioticFamily", idAntibioticFamily);
        values.put("Note", idN);

        db.insert(ANTIBIOTIC_FAMILY_NOTES_TABLE, null, values);

        if(close)
            db.close();
    }
//    public Integer getAntibiotic(Antibiotic antibiotic){
//        int id;
//        String query = "SELECT Id FROM " + NOTES_TABLE + " where IdNote = ? AND Note = ?";
//        SQLiteDatabase lectura = this.obtenerManejadorLectura();
//
//        Cursor cursor = lectura.rawQuery(query, new String[]{note.getId(), note.getValue()});
//
//        if(cursor.moveToFirst())
//            id = cursor.getInt(0);
//        else
//            id = -1;
//
//        cursor.close();
//
//        return id;
//    }
}
