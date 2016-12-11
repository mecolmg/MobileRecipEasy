package conor.navigationdrawer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by jacobcontreras on 12/2/16.
 */

public class Database {


    public static final String PREFS_NAME = "recipez";
    public static final String FAV_LIST_KEY = "favorites";
    public static final String MY_LIST_KEY = "myList";
    public static final String MY_LIST_INGREDIENTS = "ingredients";

    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEditor;

    public Activity activity;

    public ArrayList<String> myList;
    public HashMap<String, Set<String>> myListIngredientsMap;

    public ArrayList<String> favoritesList; //keys for all of the jsonobjects
    public HashMap<String, JSONObject> jSONMap; //contains all json objects with their cooresponding keys from favorites list
    //public ArrayList<String>

    public Database(Activity activity) {
        favoritesList = new ArrayList<>();
        jSONMap = new HashMap<>();
        myList = new ArrayList<>();
        myListIngredientsMap = new HashMap<>();

        this.activity = activity;

        sharedPref = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefEditor = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        retrieveDataFromStorage();
    }
    //-----------------------------------for MyList use--------------------------------------------
    public void addListToMyLists(String listName){
        myList.add(listName);
        if(sharedPref.contains(MY_LIST_KEY)){
            prefEditor.remove(MY_LIST_KEY);
        }
        if(myListIngredientsMap.containsKey(listName+MY_LIST_INGREDIENTS)){
            myListIngredientsMap.remove(listName+MY_LIST_INGREDIENTS);
        }

        myListIngredientsMap.put(listName+MY_LIST_INGREDIENTS, new HashSet<String>());


        prefEditor.putStringSet(MY_LIST_KEY, new HashSet<>(myList));
        prefEditor.putStringSet(listName + MY_LIST_INGREDIENTS, new HashSet<String>());

        prefEditor.commit();
    }

    public void removeListFromMyLists(String listName){
        //myListSet = sharedPref.getStringSet(MY_LIST_KEY, null);
        myList.remove(listName);
        //System.out.println("list size: " + myList.size());
        myListIngredientsMap.remove(listName+MY_LIST_INGREDIENTS);

        if(sharedPref.contains(MY_LIST_KEY)) {
            prefEditor.remove(MY_LIST_KEY);
            prefEditor.putStringSet(MY_LIST_KEY, new HashSet<>(myList));
            //System.out.println("removing list from database: " + listName);
        }
        if(sharedPref.contains(listName+MY_LIST_INGREDIENTS)){
            prefEditor.remove(listName+MY_LIST_INGREDIENTS);
        }

        prefEditor.commit();
    }

    public void addIngredientToMyList(String listName, String ingredient){
        myListIngredientsMap.get(listName+MY_LIST_INGREDIENTS).add(ingredient);
        if(sharedPref.contains(listName+MY_LIST_INGREDIENTS)){
            prefEditor.remove(listName+MY_LIST_INGREDIENTS);
        }
        prefEditor.putStringSet(listName+MY_LIST_INGREDIENTS, myListIngredientsMap.get(listName + MY_LIST_INGREDIENTS));
        prefEditor.commit();
    }

    public void removeIngredientFromMyList(String listName, String ingredient){
        myListIngredientsMap.get(listName+MY_LIST_INGREDIENTS).remove(ingredient);
        if(sharedPref.contains(listName+MY_LIST_INGREDIENTS)){
            prefEditor.remove(listName+MY_LIST_INGREDIENTS);
        }
        prefEditor.putStringSet(listName+MY_LIST_INGREDIENTS, myListIngredientsMap.get(listName + MY_LIST_INGREDIENTS));
        prefEditor.commit();
    }

    public ArrayList<String> getMyList(){
        return myList;
    }

    public ArrayList<String> getIngredientSet(String listName){
        ArrayList<String> ingredientList = new ArrayList<>();
        if(myListIngredientsMap.get(listName + MY_LIST_INGREDIENTS) != null) {
            ingredientList.addAll(myListIngredientsMap.get(listName + MY_LIST_INGREDIENTS));
        }
        return ingredientList;
    }

    //-----------------------------------for MyList use--------------------------------------------

    //-----------------------------------for JSON obj use------------------------------------------

    public void storeJSONObject(JSONObject jObj){

        String keyId = null;
        try {
            keyId = ""+jObj.getInt("id");
            if (!favoritesList.contains(keyId)) {
                System.out.println("hey im storing a json object");
                favoritesList.add(keyId);

            }
            jSONMap.put(keyId, jObj);
            String value = jObj.toString();
            if (!sharedPref.contains(keyId)) {
                prefEditor.putString(keyId, value);
            }
            if (sharedPref.contains(FAV_LIST_KEY)) {
                prefEditor.remove(FAV_LIST_KEY);
            }
            prefEditor.putStringSet(FAV_LIST_KEY, new HashSet<>(favoritesList));
            Log.d("DATABASE", "keyId: " + keyId);
            prefEditor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJSONObject(String keyId) throws org.json.JSONException {
        return jSONMap.get(keyId);
    }

    public void removeJSONObject(String keyId) {
        if (jSONMap.containsKey(keyId)) {
            jSONMap.remove(keyId);
        }
        if (sharedPref.contains(keyId)) {
            prefEditor.remove(keyId);
        }
        if (favoritesList.contains(keyId)) {
            favoritesList.remove(keyId);
        }
        prefEditor.commit();
    }

    public ArrayList<JSONObject> getFavoritesList() {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for(String str : favoritesList){
            try {
                jsonObjects.add(getJSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObjects;
    }

    //-----------------------------------for JSON obj use------------------------------------------

    public void retrieveDataFromStorage() {
        Log.d("DATABASE","retrieving data from storage");

        Set<String> favoritesSet = sharedPref.getStringSet(FAV_LIST_KEY, null);
        if(favoritesSet != null) {
            Log.d("DATABASE", "favset size: " + favoritesSet.size());
        }
        Set<String> myListSet = sharedPref.getStringSet(MY_LIST_KEY, null);

        if (favoritesSet != null) {
            for (String fav : favoritesSet) {
                favoritesList.add(fav);
                Log.d("DATABASE", "fav: " + fav);

                try {
                    jSONMap.put(fav, new JSONObject(sharedPref.getString(fav, null)));
                    //System.out.println("json object: " + sharedPref.getString(fav, null));
                    Log.d("DATABASE", "json object: " + sharedPref.getString(fav, null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        if(myListSet != null){
            for(String listItem : myListSet){
                myList.add(listItem);
                //System.out.println("listItem: " + listItem);
                Log.d("DATABASE", "list item: " + listItem);
                Set<String> myListIngredientSet = sharedPref.getStringSet(listItem+MY_LIST_INGREDIENTS, null);
                myListIngredientsMap.put(listItem+MY_LIST_INGREDIENTS, myListIngredientSet);
            }
        }


    }

    public void printDatabase() { //method for testing the functionality of the database
        System.out.println("printing json info. from the database...");
        for (String fav : favoritesList) {
            System.out.println(fav);

            System.out.println((jSONMap.get(fav)).toString());
        }

        for(String myListName : myList){
            System.out.println("list name: " + myListName);
            if(myListIngredientsMap.containsKey(myListName)){
                for(String myListIngredient : myListIngredientsMap.get(myListName)){
                    System.out.println("ingredient: " + myListIngredient);
                }
            }
        }
    }

}