package conor.navigationdrawer;

import android.app.Activity;
import android.content.SharedPreferences;

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

    public Set<String> myList;
    public HashMap<String, Set<String>> myListIngredientsMap;

    public ArrayList<String> favoritesList; //keys for all of the jsonobjects
    public HashMap<String, JSONObject> jSONMap; //contains all json objects with their cooresponding keys from favorites list
    //public ArrayList<String>

    public Database(Activity activity) {
        favoritesList = new ArrayList<>();
        jSONMap = new HashMap<>();
        myList = new HashSet<>();
        myListIngredientsMap = new HashMap<>();

        this.activity = activity;

        sharedPref = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefEditor = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        retrieveDataFromStorage();
    }

    public void addListToMyLists(String listName){
        myList.add(listName);
        if(sharedPref.contains(MY_LIST_KEY)){
            prefEditor.remove(MY_LIST_KEY);
        }
        if(myListIngredientsMap.containsKey(listName+MY_LIST_INGREDIENTS)){
            myListIngredientsMap.remove(listName+MY_LIST_INGREDIENTS);
        }

        myListIngredientsMap.put(listName+MY_LIST_INGREDIENTS, new HashSet<String>());

        prefEditor.putStringSet(MY_LIST_KEY, myList);
        prefEditor.putStringSet(listName + MY_LIST_INGREDIENTS, new HashSet<String>());

        prefEditor.commit();
    }

    public void removeListFromMyLists(String listName){
        myList.remove(listName);
        myListIngredientsMap.remove(listName+MY_LIST_INGREDIENTS);
        
        if(sharedPref.contains(listName)){
            prefEditor.remove(listName);
        }
        if(sharedPref.contains(listName+MY_LIST_INGREDIENTS)){
            prefEditor.remove(listName+MY_LIST_INGREDIENTS);
        }

        //prefEditor.putStringSet(MY_LIST_KEY, myList);
        prefEditor.commit();
    }

    public void addIngredientToMyList(String listName, String ingredient){
        myListIngredientsMap.get(listName).add(ingredient);



    }

    public void storeJSONObject(JSONObject jObj) throws org.json.JSONException {
        String keyId = jObj.getString("id");
        if (!favoritesList.contains(keyId)) {
            favoritesList.add(keyId);
        }
        jSONMap.put(keyId, jObj);
        String value = jObj.toString();
        if (!sharedPref.contains(keyId)) {
            prefEditor.putString(keyId, value);
        }
        if (sharedPref.contains(FAV_LIST_KEY)) {
            prefEditor.remove(FAV_LIST_KEY);
            prefEditor.putStringSet(FAV_LIST_KEY, new HashSet<>(favoritesList));
        }
        prefEditor.commit();
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

    public ArrayList<String> getFavoritesList() {
        return favoritesList;
    }

    public void retrieveDataFromStorage() {
        Set<String> favoritesSet = sharedPref.getStringSet(FAV_LIST_KEY, null);
        Set<String> myListSet = sharedPref.getStringSet(MY_LIST_KEY, null);

        if (favoritesSet != null) {
            for (String fav : favoritesSet) {
                favoritesList.add(fav);
                try {
                    jSONMap.put(fav, new JSONObject(sharedPref.getString(fav, null)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        if(myListSet != null){
            for(String listItem : myListSet){
                myList.add(listItem);
                Set<String> myListIngredientSet = sharedPref.getStringSet(listItem, null);
                myListIngredientsMap.put(listItem, myListIngredientSet);
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
            for(String myListIngredient : myListIngredientsMap.get(myListName)){
                System.out.println("ingredient: " + myListIngredient);
            }
        }
    }

}
