package conor.navigationdrawer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MyListsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CreateListDialog.DialogListener  {

    Database database;
    boolean mIsLargeLayout;


    ExpandableListView expandableListView;
    ExpandableListViewAdapter expandableListAdapter;


    ArrayList<String> listNames;
    HashMap<String, ArrayList<String>> expandableListDetail; //where the key is the name of the lsit


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);

        database = DiscoverActivity.database;
        expandableListView = (ExpandableListView) findViewById(R.id.list_view);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);


        listNames = database.getMyList();
        expandableListDetail = new HashMap<>();
        this.populateExpandableListViewFromDatabase();    //populate listView with active lists




        /*for (int i = 0; i < 20; i++)
        {
            listNames.add("List " + i);

            ArrayList<String> detail = new ArrayList<>();
            for (int j = 0; j < 6; j++)
            {
                detail.add("item " + j);
            }
            expandableListDetail.put("List " + i, detail);
        }*/


        expandableListAdapter = new ExpandableListViewAdapter(this, listNames, expandableListDetail, this);
        expandableListView.setAdapter(expandableListAdapter);


        this.setListOnClicks();

        Intent intent = getIntent();
        //do shit with intent if needed

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public void populateExpandableListViewFromDatabase()
    {
        for (String listName : listNames)
        {
            ArrayList<String> ingredientsForList = database.getIngredientSet(listName);
            expandableListDetail.put(listName, ingredientsForList);
        }
    }

    private void setListOnClicks()
    {
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listNames.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listNames.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listNames.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                listNames.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lists_toolbar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_list) {
            Log.e("Action_add_list", "creating add lsit dialogue");
            this.showCreateListDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showCreateListDialog()
    {
        CreateListDialog dialog = new CreateListDialog(this, "Create List", "List Name:");
        dialog.setmListener(this);
        dialog.show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.discover_activity) {
            //no intent needed becuase already here

//            Intent intent = new Intent(this, DiscoverActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//            startActivity(intent);
            this.finish();

        } else if (id == R.id.my_recipes_activity) {

            //start activity for my recipes screen
            Intent intent = new Intent(this, MyRecipiesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.my_lists_activity) {
            //start activity for my lists
            //Intent intent = new Intent(this, MyListsActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);

        } else if (id == R.id.my_favorites_activity) {

            //start activity for my favroites
            Intent intent = new Intent(this, MyFavoritesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onDialogCreateClick(CreateListDialog dialog, String listName) {
        Log.e("create clicked", "create list with name " + listName);


        //listNames.add(0, listName);
        expandableListDetail.put(listName, new ArrayList<String>());
        expandableListAdapter.notifyDataSetChanged();

        database.addListToMyLists(listName);

        //listItems.add(listName);
        //adapter.notifyDataSetChanged();
    }



    @Override
    public void onDialogCancelClick(Dialog dialog) {
        Log.e("cancel clicked", "dismissing dialog");
        dialog.dismiss();
    }
}
