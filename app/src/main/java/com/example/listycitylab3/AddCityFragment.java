package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    public interface AddCityDialogListener {
        void addCity(City city);
        void notifyAdapter();
    }
    private static final String ARG_CITY = "city";
    private AddCityDialogListener listener;
    private City cityToEdit;

    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException("Host must implement AddCityDialogListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog == null) return;

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            EditText editCityName = dialog.findViewById(R.id.edit_text_city_text);
            EditText editProvinceName = dialog.findViewById(R.id.edit_text_province_text);

            String cityName = editCityName.getText().toString().trim();
            String provinceName = editProvinceName.getText().toString().trim();

            if (cityName.isEmpty()) {
                editCityName.setError("City name required");
                return;
            }

            if (provinceName.isEmpty()) {
                editProvinceName.setError("Province required");
                return;
            }

            if (cityToEdit == null) {
                listener.addCity(new City(cityName, provinceName));
            } else {
                cityToEdit.setName(cityName);
                cityToEdit.setProvince(provinceName);
                listener.notifyAdapter();
            }

            cityToEdit = null;
            if (getArguments() != null) {
                getArguments().clear();
            }
            dismiss();
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            cityToEdit = (City) getArguments().getSerializable(ARG_CITY);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        if (cityToEdit != null) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(cityToEdit == null ? "Add City" : "Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();
    }
}
