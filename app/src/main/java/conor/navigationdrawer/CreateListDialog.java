package conor.navigationdrawer;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by conoroneill on 12/2/16.
 */

public class CreateListDialog extends Dialog implements View.OnClickListener {

    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public static interface DialogListener {
        public void onDialogCreateClick(CreateListDialog dialog, String string);
        public void onDialogCancelClick(Dialog dialog);
    }



    // Use this instance of the interface to deliver action events
    DialogListener mListener;

    Button cancelButton;
    Button createButton;
    EditText editText;

    TextView dialogTitle;

    ListView listView;

    public CreateListDialog(Context context, String titleString, String hint) {
        super(context);

        // Log.v("Length",""+hi.length);
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /** Design the dialog in main.xml file */
        setContentView(R.layout.create_list_dialogue);


        dialogTitle = (TextView)findViewById(R.id.dialog_title);
        dialogTitle.setText(titleString);

        editText = (EditText)findViewById(R.id.editText);
        editText.setHint(hint);

        listView = (ListView)findViewById(R.id.list_view);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,hi);

        //lst.setAdapter(adapter);
        //lst.setOnItemClickListener(this);

        cancelButton = (Button)findViewById(R.id.cancel_button);
        createButton = (Button)findViewById(R.id.create_button);

        cancelButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

    }

    public DialogListener getmListener() {
        return mListener;
    }

    public void setmListener(DialogListener mListener) {
        this.mListener = mListener;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CreateListDialog");
        }
    }*/

    @Override
    public void onClick(View v) {
        /** When OK Button is clicked, dismiss the dialog */
        if (v == cancelButton)
        {
            dismiss();
        }
        else if (v == createButton)
        {
            //send desired list name to activity
            mListener.onDialogCreateClick(this, editText.getText().toString());
            dismiss();
        }
    }

}





