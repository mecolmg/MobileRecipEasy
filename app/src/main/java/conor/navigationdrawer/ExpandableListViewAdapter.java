package conor.navigationdrawer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferObserverSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by conoroneill on 12/4/16.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter implements CreateListDialog.DialogListener
{

    Database database;
    private Context context;
    ArrayList<String> expandableListTitle;
    HashMap<String, ArrayList<String>> expandableListDetail;

    public ExpandableListViewAdapter(Context context, ArrayList<String> expandableListTitle,
                                       HashMap<String, ArrayList<String>> expandableListDetail, Activity activity) {

        this.database = new Database(activity);
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        Button deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //do something
                String listToDeleteItemFrom = expandableListTitle.get(listPosition);

                ArrayList<String> itemList = expandableListDetail.get(listToDeleteItemFrom);

                String itemToRemove = itemList.get(expandedListPosition);

                itemList.remove(expandedListPosition);
                Log.e("Deleting item ", itemToRemove);

                database.removeIngredientFromMyList(listToDeleteItemFrom, itemToRemove);

                ExpandableListViewAdapter.this.notifyDataSetChanged();
            }
        });


        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);


        /*View row = inflater.inflate(R.layout.fg, arg2, false);

        Button b2 = (Button) row.findViewById(R.id.button1);
        b2.setTag(arg0);
        b2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int pos = (int)arg0.getTag();
                lista.remove(pos);
                SunetePreferateAdaptor.this.notifyDataSetChanged();            }
        });*/


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        Button addButton = (Button) convertView.findViewById(R.id.addbutton);

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String listToAddIngrediate = expandableListTitle.get(listPosition);
                CreateListDialog dialog = new CreateListDialog(context, "Add Item To " + listToAddIngrediate , "Item Name:");
                dialog.setmListener(ExpandableListViewAdapter.this);
                dialog.show();
            }
        });

        //View row = convertView(R.layout.list_group, parent, false);
        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                String listToDelete = expandableListTitle.get(listPosition);
                expandableListDetail.remove(listToDelete);
                expandableListTitle.remove(listToDelete);
                Log.e("Deleting List", listToDelete);
                database.removeListFromMyLists(listToDelete);
                database.printDatabase();

                ExpandableListViewAdapter.this.notifyDataSetChanged();
            }
        });

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void onDialogCreateClick(CreateListDialog dialog, String listName) {
        //add ingrediant lsitname to a
        String title = dialog.dialogTitle.getText().toString();

        String[] commandInfo = title.split("Add Item To ");
        //ommandInfo = commandInfo[1].split("<SEP>");

        String listNameFromDialog = commandInfo[1];
        Log.e("ADDING ITEM to List  ", "LIST NAME = " + listNameFromDialog + "\t" + "ITEM NAME = " + listName );

        ArrayList<String> itemsForListName = expandableListDetail.get(listNameFromDialog);

        itemsForListName.add(listName);
        ExpandableListViewAdapter.this.notifyDataSetChanged();

        database.addIngredientToMyList(listNameFromDialog, listName);
    }

    @Override
    public void onDialogCancelClick(Dialog dialog) {

        dialog.cancel();
        dialog.dismiss();
    }
}