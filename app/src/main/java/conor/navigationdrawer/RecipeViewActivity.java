package conor.navigationdrawer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeViewActivity extends AppCompatActivity {

    private JSONObject recipe;
    private ImageView recipeImage;
    private TextView recipeTitle;
    private LinearLayout ingredientList;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new Database(this);

        recipeTitle = (TextView) findViewById(R.id.recipe_title);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        ingredientList = (LinearLayout) findViewById(R.id.ingredient_list);

        try {
            String json = getIntent().getStringExtra(DiscoverActivity.RECIPE_JSON);
            recipe = new JSONObject(json);

            recipeTitle.setText(recipe.getString("title"));
            Picasso.with(this)
                    .load(recipe.getString("image"))
                    .placeholder(R.drawable.stock_no_image)
                    .into(recipeImage);

            RecipeViewIngredientAdapter ingredientAdapter = new RecipeViewIngredientAdapter(this);
            ingredientAdapter.setIngredients(recipe.getJSONArray("extendedIngredients"));
            for(int i=0; i < ingredientAdapter.getCount(); i++){
                ingredientList.addView(ingredientAdapter.getView(i, null, null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder listSelectDialog = new AlertDialog.Builder(RecipeViewActivity.this);
                listSelectDialog.setTitle("Add Ingredients to List:")
                        .setItems(database.getMyList().toArray(new CharSequence[database.getMyList().size()]),
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                try {
                                    JSONArray ingredients = recipe.getJSONArray("extendedIngredients");
                                    for(int i=0; i < ingredients.length(); i++){
                                        database.addIngredientToMyList(database.getMyList().get(which),
                                                ingredients.getJSONObject(i).getString("originalString"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                listSelectDialog.create().show();

//                Snackbar.make(view, "This will add the ingredients to a list", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private String capitalize(String str){
        String splitStr[] = str.split(" ");
        String output = "";
        for(String word : splitStr){
            output += word.substring(0,1).toUpperCase() + word.substring(1,word.length()).toLowerCase() + " ";
        }
        return output.substring(0,output.length()-1);
    }

}
