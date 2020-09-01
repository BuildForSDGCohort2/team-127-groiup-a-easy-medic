package org.builgforsdg.team127a.easymedic.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import org.builgforsdg.team127a.easymedic.R;
import org.builgforsdg.team127a.easymedic.entities.Article;
import org.builgforsdg.team127a.easymedic.entities.list;
import org.builgforsdg.team127a.easymedic.utilities.ArticleDBHelper;
import org.builgforsdg.team127a.easymedic.utilities.CustomAdapter;
import org.builgforsdg.team127a.easymedic.utilities.adapter;

import java.util.ArrayList;
import java.util.List;

import static org.builgforsdg.team127a.easymedic.utilities.GeneralMethods.checkEmpty;
import static org.builgforsdg.team127a.easymedic.utilities.GeneralMethods.checkTextLength;

public class SearchActivity extends AppCompatActivity {
    ArticleDBHelper.DatabaseHelper dbHandler;
    EditText SearchPhrase;
    Button btnSearch, btnClear;
    String strSearchPhrase;
    private List<Article> myArticleList = new ArrayList<Article>();
    private ArrayList<String> category = new ArrayList<>();
    private ArrayList<String> body = new ArrayList<>();
    private ArrayList<String> bodyShortened = new ArrayList<>();
    private ArrayList<String> extention = new ArrayList<>();
    private ArrayList<String> fileName = new ArrayList<>();
    private ArrayList<String> path = new ArrayList<>();

    private String TAG = "SEARCH";
    private RecyclerView listView;
    private ArrayList<list> dataArrayList;
    private adapter listAdapter;
    private list data;
    public ViewPager viewPager;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHandler = new ArticleDBHelper.DatabaseHelper(SearchActivity.this, null, null, 1);
        SearchPhrase = findViewById(R.id.etSearchPhrase);
        btnSearch = findViewById(R.id.btnSearch);
        btnClear = findViewById(R.id.btnClear);
        listView = findViewById(R.id.listview);
        dataArrayList = new ArrayList<>();

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPhrase.setText("");
                clearAllArrayLists();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                listView.setLayoutManager(linearLayoutManager);
                CustomAdapter customAdapter = new CustomAdapter(SearchActivity.this, category,
                        bodyShortened, body, fileName, extention, path);
                listView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                btnClear.setVisibility(View.GONE);
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllArrayLists();
                if (checkEmpty(SearchPhrase, SearchActivity.this)) return;

                if (checkTextLength(SearchPhrase, 3, SearchActivity.this)) return;

                strSearchPhrase = SearchPhrase.getText().toString();
                myArticleList = dbHandler.findMatchingArticle(strSearchPhrase);
                Log.d(TAG, "onClick: myArticleList >> " + myArticleList);
                Log.d(TAG, "onClick: Article count >> " + myArticleList.size());
                int articleCount = myArticleList.size();
                if (articleCount > 0) {
                    btnClear.setVisibility(View.VISIBLE);
                    //loop through a list
                    for (Article article : myArticleList) {
                        //extract list items from list
                        // dataArrayList.add(data = new list(article.getCategory(),  article.getBody().substring(0,50),R.drawable.girl));
                        String srCategory = article.getCategory();
                        String strBody = article.getBody();
                        String strrExtention = article.getType();
                        String strFileName = article.getDescription();
                        String strPath = article.getPath();
                        String strBodyShortened = article.getBody().substring(0, 50);
                        strBodyShortened = strBodyShortened + " (..)";

                        // fetch link and name and store it in arraylist
                        Log.d(TAG, "srCategory is: " + srCategory);
                        Log.d(TAG, "strBody is: " + strBody);
                        Log.d(TAG, "strFileName is: " + strFileName);
                        Log.d(TAG, "strrExtention is: " + strrExtention);
                        Log.d(TAG, "strBodyShortened is: " + strBodyShortened);
                        Log.d(TAG, "strPath is: " + strPath);
                        category.add(srCategory);
                        body.add(strBody);
                        fileName.add(strFileName);
                        extention.add(strrExtention);
                        bodyShortened.add(strBodyShortened);
                        path.add(strPath);
                    }
                } else {
                    showNoResultPopUp();
                }
                // set a LinearLayoutManager with default vertical orientation
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                listView.setLayoutManager(linearLayoutManager);
                CustomAdapter customAdapter = new CustomAdapter(SearchActivity.this, category,
                        bodyShortened, body, fileName, extention, path);
                listView.setAdapter(customAdapter); // set the Adapter to RecyclerView
            }
        });

    }

    private void clearAllArrayLists() {
        category.clear();
        body.clear();
        fileName.clear();
        extention.clear();
        bodyShortened.clear();
    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#F45557")));
            // set item width
            deleteItem.setWidth(150);
            deleteItem.setTitle("x");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(15);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem((0), true);
        } else {
            exitApp();
        }
    }

    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, getString(R.string.msg_yes_exit), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exiting App")
                    .setMessage("Are you sure you want to Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            moveTaskToBack(true);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            finish();
        }
    }

    private void showNoResultPopUp() {
        //Creating an alert dialog
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.no_result));
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setMessage(getApplicationContext().getResources().getString(R.string.no_result_lon_message));
        alertDialogBuilder.setPositiveButton(getApplicationContext().getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        clearAllArrayLists();
                        SearchPhrase.setText("");
                    }
                });


        //Showing the alert dialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}
