package fr.utt.if26.istarve.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Utility to simplify the use of the Dialog Window
 */
public class DialogUtil {

    private Activity mActivity;

    /**
     * Contructor
     * @param activity
     *  Activity to which the dialog window will be bound
     */
    public DialogUtil (Activity activity) {
        mActivity = activity;
    }

    /**
     * Create and display a Dialog Window
     * @param title
     *  Title of the window
     * @param message
     *  Content of the window
     */
    public void showAlertDialog(String title, String message){
        if(!mActivity.isFinishing()){
            new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // dismiss the alert
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }
}
