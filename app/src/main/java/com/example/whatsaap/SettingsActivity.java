package com.example.whatsaap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

public class SettingsActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);

        RadioGroup radioGroup = findViewById(R.id.rg);

        SharedPreferences pref = getSharedPreferences("settings",MODE_PRIVATE);
        String code = pref.getString("code","en");
        switch (code){
            case "en":
                radioGroup.check(R.id.rb_eng);
                break;
            case "ar":
                radioGroup.check(R.id.rb_ar);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_ar:
                        saveLanguage("ar");

                        break;

                    case R.id.rb_eng:
                       saveLanguage("en");
                        break;


                }
            }
        });


    }
    private void saveLanguage(String languageCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(R.string.close_app)
                .setPositiveButton(R.string.language_dialog_positive, (dialog, which) -> {
                    SharedPreferences.Editor editor =
                            getSharedPreferences("settings",MODE_PRIVATE).edit();
                    editor.putString("code",languageCode);
                    editor.apply();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);




                })
                .setCancelable(false)
                .show();


    }
}