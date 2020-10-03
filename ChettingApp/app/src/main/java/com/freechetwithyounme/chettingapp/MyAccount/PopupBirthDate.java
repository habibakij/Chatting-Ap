package com.freechetwithyounme.chettingapp.MyAccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.LocalDB.SQLiteDB;
import com.freechetwithyounme.chettingapp.R;

public class PopupBirthDate extends AppCompatDialogFragment {
    private DatePicker datePicker;
    private BirthDate birthDateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.popup_birth_date,null);

        builder.setView(view)
                .setTitle("Select birth date")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int month= datePicker.getMonth()+1;
                        String date= datePicker.getYear()+"/"+month+"/"+datePicker.getDayOfMonth();
                        birthDateListener.getBirthDay(date);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        datePicker= view.findViewById(R.id.bith_date_picker);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            birthDateListener = (BirthDate) context;
        } catch (ClassCastException e){
            new Throwable("must select birth date");
        }
    }

    public interface BirthDate{
        void getBirthDay(String BirthDay);
    }
}