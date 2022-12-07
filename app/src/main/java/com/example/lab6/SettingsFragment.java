package com.example.lab6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private int NightMode;
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private Switch theme_switch;
    private CheckBox statistics;
    private RadioGroup radioGroup;
    private Spinner spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        theme_switch = (Switch) view.findViewById(R.id.theme);
        theme_switch.setChecked(NightMode==1);
        AppCompatDelegate.setDefaultNightMode(NightMode);

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.difficulty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        statistics = (CheckBox) view.findViewById(R.id.statistics_checkbox);
        statistics.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(statistics.isChecked()){
                            editor.putBoolean("sendStatistics", true);
                            editor.apply();
                        }else{
                            editor.putBoolean("sendStatistics", false);
                            editor.apply();
                        }
                    }
                }
        );

        radioGroup = (RadioGroup) view.findViewById(R.id.colors_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.all_colors_radio:{
                            editor.putString("colors", "Всі");
                        break;
                    }
                    case R.id.warm_colors_radio:{
                            editor.putString("colors", "Теплі");
                        break;
                    }
                    case R.id.cold_colors_radio:{
                            editor.putString("colors", "Холодні");

                        break;
                    }

                }
                editor.apply();
            }
        });


        theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    NightMode = AppCompatDelegate.MODE_NIGHT_NO;
                    sharedPreferences = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                   // editor = sharedPreferences.edit();
                    editor.putInt("NightModeInt", NightMode);
                    editor.apply();
                }else{
                    ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    NightMode = AppCompatDelegate.MODE_NIGHT_YES;
                    sharedPreferences = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                   // editor = sharedPreferences.edit();
                    editor.putInt("NightModeInt", NightMode);
                    editor.apply();
                }
            }
        });

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        String difficulty = mySharedPreferences.getString("difficulty", "Легкая");
        String colors = mySharedPreferences.getString("colors", "Всі");
        boolean sendStatistics = mySharedPreferences.getBoolean("sendStatistics", false);
        int NightModeInt = mySharedPreferences.getInt("NightModeInt", 1);
        theme_switch.setChecked(NightModeInt==1);
        statistics.setChecked(sendStatistics);

        switch(colors){
            case "Всі":{
                radioGroup.check(R.id.all_colors_radio);
                break;
            }
            case "Теплі":{
                radioGroup.check(R.id.warm_colors_radio);
                break;
            }
            case "Холодні":{
                radioGroup.check(R.id.cold_colors_radio);
                break;
            }
        }
        switch(difficulty){
            case "Легко":{
                spinner.setSelection(0);
                break;
            }
            case "Нормально":{
                spinner.setSelection(1);
                break;
            }
            case "Складно":{
                spinner.setSelection(2);
                break;
            }
        }




    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("difficulty",adapterView.getItemAtPosition(i).toString());
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}