package model;

/**
 * Created by Hari Prahlad on 24-04-2016.
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.indianservers.onlinegrocery.R;

public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context

     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(final Context context,String title,String message, final Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle).create();
        // Setting Dialog Title
        // Setting Dialog Message
        alertDialog.setMessage("If you Want connect click below");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Mobile Data", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                return;

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Wifi", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                return;
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                System.exit(0);
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void showAlertDialogComfirmation(final Context context, String message, final Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle).create();
        // Setting Dialog Title
        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
                return;

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}