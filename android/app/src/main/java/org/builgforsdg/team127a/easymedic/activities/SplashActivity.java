package org.builgforsdg.team127a.easymedic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.builgforsdg.team127a.easymedic.R;
import org.builgforsdg.team127a.easymedic.entities.Article;
import org.builgforsdg.team127a.easymedic.utilities.ArticleDBHelper;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SplashActivity extends AppCompatActivity {
    ArticleDBHelper.DatabaseHelper dbHandler;

    private static final String TAG = "SPLASH";
    Handler handler;
    TextView tryagain, maintext, txt_pls_wait;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        tryagain = findViewById(R.id.retry);
        maintext = findViewById(R.id.maintext);
        txt_pls_wait = findViewById(R.id.txt_pls_wait);
        sharedPreferences = getSharedPreferences(getString(R.string.first_time_user), MODE_PRIVATE);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dbHandler = new ArticleDBHelper.DatabaseHelper(SplashActivity.this, null, null, 1);
                int userStatus = sharedPreferences.getInt(getString(R.string.first_time_user), 0);
                if (userStatus == 1) {
                    //has already added database data
                    Intent it = new Intent(SplashActivity.this, SearchActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    if (checkDb()) //returns true if data is available
                    {
                        Intent it = new Intent(SplashActivity.this, SearchActivity.class);
                        startActivity(it);
                        finish();
                    } else {
                        maintext.setVisibility(View.VISIBLE);
                        tryagain.setVisibility(View.VISIBLE);
                        txt_pls_wait.setVisibility(View.INVISIBLE);
                        Toast.makeText(SplashActivity.this, "An data found", Toast.LENGTH_LONG).show();
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(getString(R.string.first_time_user), 1);
                    editor.apply();
                    Intent it = new Intent(SplashActivity.this, SearchActivity.class);
                }
            }

        }, 2500);


        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SplashActivity.this, SplashActivity.class);
                startActivity(it);
            }
        });
    }

    private boolean checkDb() {
        AssetManager assetManager = getAssets();
        boolean output = false;
        ArrayList<String> categories = new ArrayList<String>();
        String newPath = "", body = "", completePath = "", extension = "";
        String[] tempArray;
        // To get names of all files inside the "Files" folder
        try {
            String[] folders = assetManager.list("articles");

            assert folders != null;
            //Log.d(TAG, "checkDb: About to loop through folders");
            for (int i = 0; i < folders.length; i++) {
                //Log.d(TAG, "checkDb: articles Dir >> " + folders[i]);
                categories.add(folders[i]);
                newPath = "articles/" + folders[i];
                //Log.d(TAG, "checkDb: newPath >> " + newPath);
                String[] files = assetManager.list(newPath);
                //Log.d(TAG, "checkDb: about to loop through files in folder");
                assert files != null;
                for (int j = 0; j < files.length; j++) {
                    //Log.d(TAG, "checkDb: articles files >> " + files[j]);
                    if (files[j].length()>2){
                        extension = files[j].substring(files[j].length() - 3);
                    }
                    //Log.d(TAG, "checkDb: extension >> " + extension);
                    completePath = newPath + "/" + files[j];
                    //get body of article
                    body = LoadFileBodyData(completePath, extension);
//                    //Log.d(TAG, "checkDb: body >> " + body);
                    if ((extension.equalsIgnoreCase("tml"))
                            || (extension.equalsIgnoreCase("htm"))
                            || (extension.equalsIgnoreCase("txt"))
                            || (extension.equalsIgnoreCase("php"))) {
                        saveArticleToDb(folders[i], files[j], body, extension, completePath);

                    }

                }

            }
            //Log.d(TAG, "checkDb: ALL CATEGORIES >>>>>>>> " + categories);
            output = true;

        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return output;
    }

    private void saveArticleToDb(String category, String title, String body, String type, String path) {
        Date date = new Date();
        String theDate = date.toString();
        String month = "", year = "", day = "";

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        Calendar calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String strDateInFormat = dateFormat.format(cal.getTime());
        Article myArticle = new Article(title, category, "default", body,
                type, path, strDateInFormat, day, month, year);
        dbHandler.saveArticle(myArticle);
        //Log.d(TAG, "saveArticleToDb: >> Record Added >>>>" + myArticle.toString());
    }

    private String LoadFileBodyData(String inFile, String ext) {
        String tContents = "";
        try {
            InputStream stream = getAssets().open(inFile);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
            tContents = tContents.replaceAll("<br />", "\n");
            tContents = tContents.replaceAll("<br/>", "\n");
            tContents = tContents.replaceAll("<br>", "\n");
            if ((ext.equalsIgnoreCase("tml"))
                    || (ext.equalsIgnoreCase("htm"))
                    || (ext.equalsIgnoreCase("php"))
            ) {
                //Log.d(TAG, "LoadFileBodyData: before stripping html tags > tContents >>" + tContents);
                tContents = html2text(tContents);
                tContents = tContents.replaceAll("<?", "");
                tContents = tContents.replaceAll("<?php", "");
                //Log.d(TAG, "LoadFileBodyData: AFTER PHP tag removal");
            } else if (ext.equalsIgnoreCase("pdf")) {
                String parsedText = null;
                PDDocument document = null;
                InputStream pdfStream = getAssets().open(inFile);
                try {
                    document = PDDocument.load(pdfStream);
                } catch (IOException e) {
                    Log.e("Pdf Document issue", "Exception thrown while loading document to strip", e);
                }

                try {
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    pdfStripper.setStartPage(0);
                    pdfStripper.setEndPage(1);
                    parsedText = "Parsed text: " + pdfStripper.getText(document);
                } catch (IOException e) {
                    Log.e("Pdf Document issue", "Exception thrown while stripping text", e);
                } finally {
                    try {
                        if (document != null) document.close();
                    } catch (IOException e) {
                        Log.e("Pdf Document issue", "Exception thrown while closing document", e);
                    }
                }
                //Log.d(TAG, "LoadFileBodyData: Extracted PDF >>" + parsedText);
                tContents = parsedText;
                //Log.d(TAG, "LoadFileBodyData: After complete Cleanup of document");

            }// end of pdf condition

            ///end of try
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tContents;

    }// end of LoadFileBodyData


    private static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

}