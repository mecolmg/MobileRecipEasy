package conor.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Colm Gallagher on 12/1/2016.
 */

public class RecipeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<JSONObject> recipes;

    public RecipeAdapter(Context context) {
        mContext = context;
        recipes = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItems(ArrayList<JSONObject> newItems){
        recipes.addAll(newItems);
        for(JSONObject recipe : newItems){
            try {
                String imageUrl = recipe.getString("image");
                Picasso.with(mContext)
                        .load(imageUrl)
                        .placeholder(R.drawable.stock_no_image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.notifyDataSetChanged();
    }

    public void clearItems(){
        recipes.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = (JSONObject) getItem(position);

        View rowView = mInflater.inflate(R.layout.grid_recipe_item, parent, false);
        ImageView recipeImg = (ImageView) rowView.findViewById(R.id.recipe_image);
        TextView recipeName = (TextView) rowView.findViewById(R.id.recipe_text);

        try {
            int id = json.getInt("id");
            String imageUrl = json.getString("image");
            Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.stock_no_image)
                .into(recipeImg);
            recipeName.setText(json.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}