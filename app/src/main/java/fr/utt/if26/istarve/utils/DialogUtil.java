package fr.utt.if26.istarve.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogUtil {

    private Activity mactivity;

    public DialogUtil (Activity activity) {
        mactivity = activity;
    }
    public void showAlertDialog(String title, String message){
        new AlertDialog.Builder(mactivity)
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
