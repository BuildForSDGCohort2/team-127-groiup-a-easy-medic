package org.builgforsdg.team127a.easymedic.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.builgforsdg.team127a.easymedic.entities.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDBHelper {

    // Articles Table Column names
    private static final String KEY_ID = "_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_BODY = "body";
    private static final String KEY_PATH = "path";
    private static final String KEY_DATETIME = "dateTime";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";

    private static final String TAG = "DBHELPER";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    // Database Name
    private static final String DATABASE_NAME = "ArticleDB.db";
    //  table name
    private static final String TABLE_ARTICLES = "ArticlesTbl";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_ARTICLE_TABLE = "CREATE VIRTUAL TABLE " + TABLE_ARTICLES + " USING fts3 ("
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_AUTHOR + " TEXT,"
            + KEY_BODY + " TEXT,"
            + KEY_PATH + " TEXT,"
            + KEY_DATETIME + " TEXT,"
            + KEY_TYPE + " TEXT,"
            + KEY_DAY + " TEXT,"
            + KEY_MONTH + " TEXT,"
            + KEY_YEAR + " TEXT" + ")";

    private final Context mContext;

    /*public static class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }*/

    public static class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                Log.d(TAG, "onCreate: CREATE_ARTICLE_TABLE >> "+ CREATE_ARTICLE_TABLE);
                db.execSQL(CREATE_ARTICLE_TABLE);
                Log.d(TAG, "OnCreate() Database");
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
            Log.d(TAG, "OnUpgrade() Database");
            // Creating tables again
            onCreate(db);
        } // Adding new Article

        public void addArticle(Article article) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DESCRIPTION, article.getDescription()); // Article itself
            values.put(KEY_CATEGORY, article.getCategory()); //
            values.put(KEY_AUTHOR, article.getAuthor()); //
            values.put(KEY_BODY, article.getBody()); //
            values.put(KEY_PATH, article.getPath()); //
            values.put(KEY_DATETIME, article.getDateTime()); //
            values.put(KEY_TYPE, article.getType()); //
            values.put(KEY_DAY, article.getDay()); //
            values.put(KEY_MONTH, article.getMonth()); //
            values.put(KEY_YEAR, article.getYear()); //
// Inserting Row
            db.insert(TABLE_ARTICLES, null, values);
            db.close(); // Closing database connection
        }

//        // Getting one Article
//        public Article getArticle(int id) {
//            SQLiteDatabase db = this.getReadableDatabase();
//
//            Cursor cursor = db.query(TABLE_ARTICLES, new String[]{
//                            KEY_DESCRIPTION, KEY_CATEGORY, KEY_AUTHOR, KEY_BODY, KEY_DATETIME, KEY_DAY, KEY_MONTH, KEY_YEAR}, KEY_ID + "=?",
//                    new String[]{String.valueOf(id)}, null, null, null, null);
//            if (cursor != null)
//                cursor.moveToFirst();
//
//            assert cursor != null;
//            Article myArticle = new Article(cursor.getString(0),
//                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
//                    cursor.getString(4), cursor.getString(5), cursor.getString(6),
//                    cursor.getString(7));
//            // return myArticle
//            return myArticle;
//        }

        //Getting Cursor to read data from table
        public Cursor readData() {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] allColumns = new String[]{ArticleDBHelper.KEY_DESCRIPTION,
                    ArticleDBHelper.KEY_BODY};
            Cursor c = db.query(ArticleDBHelper.TABLE_ARTICLES, allColumns, null,
                    null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }

        /**
         * Gets all Articles in the Database and returns a cursor of them.
         * If there are no items in the database then the cursor returns null
         *
         * @return A Cursor of all Articles or null
         */
        public Cursor getAllArticleCursor() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_ARTICLES, new String[]{KEY_ID, KEY_DESCRIPTION,
                            KEY_BODY, KEY_DATETIME, KEY_DAY, KEY_CATEGORY, KEY_AUTHOR},
                    null, null, null,
                    null, KEY_ID + " DESC");
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            } else {
                return null;
            }
        }

        public void saveArticle(Article article) {
            ContentValues values = new ContentValues();
            SQLiteDatabase db = this.getWritableDatabase();
            values.put(KEY_DESCRIPTION, article.getDescription()); // Article itself
            values.put(KEY_CATEGORY, article.getCategory()); //
            values.put(KEY_AUTHOR, article.getAuthor()); //
            values.put(KEY_BODY, article.getBody()); //
            values.put(KEY_PATH, article.getPath()); //
            values.put(KEY_DATETIME, article.getDateTime()); //
            values.put(KEY_TYPE, article.getType()); //
            values.put(KEY_DAY, article.getDay()); //
            values.put(KEY_MONTH, article.getMonth()); //
            values.put(KEY_YEAR, article.getYear()); //

            // Inserting Row
            db.insert(TABLE_ARTICLES, null, values);
            db.close();
        }


        /**
         * Finds a Article in the database and returns it to the caller. If this function does not find
         * a Article, then the returned Article is null
         *
         * @param phrase Name of the Article to find
         * @return A valid Article or a null Article
         */

        public List<Article> findMatchingArticle(String phrase) {
            List<Article> articleList = new ArrayList<>();
            // Select All Query

            String selectQuery = "Select * FROM " + TABLE_ARTICLES + " WHERE " + TABLE_ARTICLES +
                    " MATCH  '"+ KEY_BODY + ":" + phrase + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to l
            // ist
            if (cursor.moveToFirst()) do {
                Article article = new Article();
                // article.setid(cursor.getInt(0));
                article.setCategory(cursor.getString(1));
                article.setDescription(cursor.getString(0));
                article.setDateTime(cursor.getString(5));
                article.setPath(cursor.getString(4));
                article.setBody(cursor.getString(3));
                article.setDateTime(cursor.getString(6));
                article.setDay(cursor.getString(7));
                article.setMonth(cursor.getString(8));
                article.setYear(cursor.getString(9));
                article.setAuthor(cursor.getString(2));

                Log.d(TAG, "findMatchingArticle: article >>"+ phrase);
                // Adding article to list
                articleList.add(article);
            } while (cursor.moveToNext());

            // return Article list
            return articleList;
        }

        public Article findArticle(String Articlename) {
            Cursor cursor;
            String query = "Select * FROM " + TABLE_ARTICLES + " WHERE " + KEY_DESCRIPTION +
                    " = \"" + Articlename + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(query, null);
            Article p = new Article();

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                // p.setID(Integer.parseInt(cursor.getString(0)));
                p.setDescription(cursor.getString(2));
                p.setBody(cursor.getString(4));
                p.setCategory(cursor.getString(3));
                p.setAuthor(cursor.getString(4));
                p.setDay(cursor.getString(5));
                p.setMonth(cursor.getString(6));
                p.setYear(cursor.getString(7));

                cursor.close();
            } else {
                p = null;
            }
            db.close();
            return p;
        }







        // Getting All Articles as a list
        //might not be acurate as type is not yet added
        public List<Article> getAllArticles() {
            List<Article> articleList = new ArrayList<>();
// Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_ARTICLES;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to l
// ist
            if (cursor.moveToFirst()) do {
                Article dexpens = new Article();
                // dexpens.setid(cursor.getInt(0));
                dexpens.setDescription(cursor.getString(1));
                dexpens.setCategory(cursor.getString(2));
                dexpens.setAuthor(cursor.getString(4));
                dexpens.setBody(cursor.getString(3));
                dexpens.setDateTime(cursor.getString(5));
                dexpens.setDay(cursor.getString(6));
                dexpens.setMonth(cursor.getString(7));
                dexpens.setYear(cursor.getString(8));


// Adding dexpens to list
                articleList.add(dexpens);
            } while (cursor.moveToNext());

// return Article list
            return articleList;
        }

        // Getting Articles Count
        public int getArticlesCount() {
            String countQuery = "SELECT * FROM " + TABLE_ARTICLES;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();

// return count
            return cursor.getCount();
        }



    }

    public ArticleDBHelper(Context context) {
        mContext = context;
    }


}
