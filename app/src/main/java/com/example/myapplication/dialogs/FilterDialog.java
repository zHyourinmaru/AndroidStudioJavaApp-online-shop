package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Informations;
import com.example.myapplication.R;
import com.example.myapplication.searchActivities.DrinkSearchActivity;

import java.util.ArrayList;


public class FilterDialog extends DialogFragment {


    DrinkSearchActivity activity = null;

    public ArrayList<String> activeFilters;
    private final ArrayList<String> backupActiveFilters;
    boolean[] checkedItems;
    boolean[] backupCheckedItems;

    public FilterDialog(DrinkSearchActivity activity, ArrayList<String> activeFilters, boolean[] checkedItems) {
        this.activity = activity;
        this.activeFilters = activeFilters;
        this.backupActiveFilters = (ArrayList<String>) this.activeFilters.clone();
        this.checkedItems = checkedItems;
        this.backupCheckedItems = this.checkedItems.clone();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        String[] filtri = Informations.allIngredients.toArray(new String[0]);

        TextView textView = new TextView(requireActivity());
        textView.setText(R.string.filters+":");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.fucsiapink));
        textView.setTextColor(Color.WHITE);

        builder.setCustomTitle(textView).setMultiChoiceItems(filtri, this.checkedItems, (DialogInterface dialog, int which, boolean isChecked) -> {
                String filtro = filtri[which];
                if (isChecked) {
                    activeFilters.add(filtro);
                    checkedItems[which] = true;
                    System.out.println(filtro);
                } else if (activeFilters.contains(filtro)) {
                    // Else, if the item is already in the array, remove it
                    activeFilters.remove(filtro);
                    checkedItems[which] = false;
                }

            }
        ).setPositiveButton(R.string.confirm_button, (DialogInterface dialog, int id) -> {
            this.activity.activeFilters = activeFilters;
            this.activity.checkedItems = checkedItems;
            this.activity.showDrinks();
            System.out.println(activeFilters);
        }).setNegativeButton(R.string.cancel_button, ((DialogInterface dialog, int id) -> {
            activeFilters = (ArrayList<String>) backupActiveFilters.clone();
            checkedItems = backupCheckedItems.clone();
            this.activity.activeFilters = activeFilters;
            this.activity.checkedItems = checkedItems;
            this.activity.showDrinks();
            System.out.println(activeFilters);}));

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
    }

}


