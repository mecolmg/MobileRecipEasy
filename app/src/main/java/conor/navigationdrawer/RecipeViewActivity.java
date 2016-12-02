package conor.navigationdrawer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeViewActivity extends AppCompatActivity {

    private JSONObject recipe;
    private ImageView recipeImage;
    private TextView recipeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recipeTitle = (TextView) findViewById(R.id.recipe_title);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);

        try {
            String json = getIntent().getStringExtra(DiscoverActivity.RECIPE_JSON);
            recipe = new JSONObject(json);

            recipeTitle.setText(recipe.getString("title"));
            Picasso.with(this)
                    .load(recipe.getString("image"))
                    .placeholder(R.drawable.stock_no_image)
                    .into(recipeImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
