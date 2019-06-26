package com.example.yimingzhai.lab2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;


public class DeleteDialog extends AppCompatDialogFragment {

    DatabaseHelper myDb;

    String name;

    public DeleteDialog() {

    }

    @SuppressLint("ValidFragment")
    public DeleteDialog(String name) {
        this.name = name;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Notice")
                .setMessage("Delete this note?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Context context = getActivity().getApplicationContext();

                        myDb = new DatabaseHelper(context);

                        if (myDb.deleteData(name)) {
                            Intent backtoListActivity = new Intent(context, ListActivity.class);
                            startActivity(backtoListActivity);

                            String successText = "Delete Successful";
                            Toast.makeText(context, successText, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String failText = "Delete failed";
                            Toast.makeText(context, failText, Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
}
