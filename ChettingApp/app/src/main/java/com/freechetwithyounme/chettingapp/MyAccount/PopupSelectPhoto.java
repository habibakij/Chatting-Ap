package com.freechetwithyounme.chettingapp.MyAccount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.freechetwithyounme.chettingapp.R;

public class PopupSelectPhoto extends AppCompatDialogFragment {
    TextView takeFromCamera, takeFromGallery;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.popupselectphoto,null);

        builder.setView(view);

        return super.onCreateDialog(savedInstanceState);
    }
}
