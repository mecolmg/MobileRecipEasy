package conor.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Colm Gallagher on 12/4/2016.
 */

public class RecipeViewIngredientAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private JSONArray ingredients;

    public RecipeViewIngredientAdapter(Context context){
        this.mContext = context;
        ingredients = new JSONArray();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ingredients.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return ingredients.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setIngredients(JSONArray ingredients){
        this.ingredients = ingredients;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = (JSONObject) getItem(position);
        View itemView = mInflater.inflate(R.layout.list_ingredient_item, parent, false);
        TextView iNumberView = (TextView) itemView.findViewById(R.id.ingredient_number);
        TextView iNameView = (TextView) itemView.findViewById(R.id.ingredient_name);
        TextView iAmountView = (TextView) itemView.findViewById(R.id.ingredient_amount);

        try {
            iNumberView.setText(""+(position+1)+".");
            iNameView.setText(capitalize(json.getString("name")));
            if(json.getInt("amount") != 0){
                iAmountView.setText(""+json.getInt("amount")+" "+json.getString("unitShort"));
            } else {
                iAmountView.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemView;
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
