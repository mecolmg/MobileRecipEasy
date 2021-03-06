package conor.navigationdrawer;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Database database;
    private GridView recipeGrid;
    private RecipeAdapter recipeGridAdapter;

    public final static String RECIPE_JSON = "Recipe json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

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

        database = DiscoverActivity.database;
        //database = new Database(this);
        ArrayList<JSONObject> favorites = database.getFavoritesList();
        Log.d("FAVORITES", "Size: "+favorites.size());
        recipeGrid = (GridView) findViewById(R.id.recipe_grid);
        recipeGridAdapter = new RecipeAdapter(this);
        recipeGridAdapter.addItems(favorites);
        recipeGrid.setAdapter(recipeGridAdapter);
        recipeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                JSONObject recipe = (JSONObject) recipeGridAdapter.getItem(position);
                Intent intent = new Intent(MyFavoritesActivity.this, RecipeViewActivity.class);
                intent.putExtra(RECIPE_JSON, recipe.toString());
                startActivity(intent);
                try {
                    Toast.makeText(MyFavoritesActivity.this, "" + recipe.getInt("id"),
                            Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        getMenuInflater().inflate(R.menu.favorites_toolbar_options, menu);
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

        return super.onOptionsItemSelected(item);
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
            Intent intent = new Intent(this, MyListsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.my_favorites_activity) {

            //start activity for my favroites
            //Intent intent = new Intent(this, MyFavoritesActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
