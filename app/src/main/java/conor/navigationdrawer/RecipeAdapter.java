package conor.navigationdrawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Colm Gallagher on 12/1/2016.
 */

public class RecipeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<JSONObject> recipes;
    private HashMap<Integer, Bitmap> cachedImages;
    private HashMap<Integer, DownloadImageTask> downloadTasks;

    public RecipeAdapter(Context context) {
        mContext = context;
        recipes = new ArrayList<>();
        cachedImages = new HashMap<>();
        downloadTasks = new HashMap<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItems(ArrayList<JSONObject> newItems){
        recipes.addAll(newItems);
        for(JSONObject recipe : newItems){
            try {
                int id = recipe.getInt("id");
                downloadTasks.put(id,new DownloadImageTask(null, id));
                downloadTasks.get(id).execute(recipe.getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            if(cachedImages.containsKey(id)){
                recipeImg.setImageBitmap(cachedImages.get(id));
            } else {
                downloadTasks.get(id).setImageView(recipeImg);
            }
            recipeName.setText(json.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rowView.getWidth();
        return rowView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int id;
        Bitmap result;

        public DownloadImageTask(ImageView bmImage, int id) {
            this.bmImage = bmImage;
            this.id = id;
            this.result = null;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        public void setImageView(ImageView view){
            bmImage = view;
            if(result != null){
                onPostExecute(result);
            }
        }

        protected void onPostExecute(Bitmap result) {
            this.result = result;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 50, out);
            Bitmap thumbnail = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            if(!cachedImages.containsKey(id)) {
                cachedImages.put(id, thumbnail);
            }
            if(bmImage != null){
                bmImage.setImageBitmap(thumbnail);
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    Log.d("DOWNLOAD", "FINALIZE FAILED");
                    throwable.printStackTrace();
                }
            }
        }
    }
}