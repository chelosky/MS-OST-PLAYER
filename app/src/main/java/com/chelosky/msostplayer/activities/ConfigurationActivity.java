package com.chelosky.msostplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.helpers.UserPreferencesHelper;

public class ConfigurationActivity extends AppCompatActivity {

    EditText txtEditNameFolder;
    TextView txtSwitchSound;
    Switch switchSound;
    Button btnSavePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        txtEditNameFolder = (EditText) findViewById(R.id.txtEditNameFolder);
        txtSwitchSound = (TextView) findViewById(R.id.txtSwitchSound);
        switchSound = (Switch) findViewById(R.id.switchSound);
        btnSavePreferences = (Button) findViewById(R.id.btnSavePreferences);
        setUpInformation();
        btnSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateTextSwitch();
            }
        });
    }

    private void saveInformation(){
        UserPreferencesHelper.updateUserPreferences(this, txtEditNameFolder.getText().toString(), switchSound.isChecked());
    }
    private void setUpInformation(){
        txtEditNameFolder.setText(UserPreferencesHelper.getNameFolderDownloads(this));
        switchSound.setChecked(UserPreferencesHelper.getSoundEffectApp(this));
        updateTextSwitch();

    }

    private void updateTextSwitch(){
        if(switchSound.isChecked()){
            txtSwitchSound.setText("ON");
            txtSwitchSound.setTextColor(getResources().getColor(R.color.green));
        }else{
            txtSwitchSound.setText("OFF");
            txtSwitchSound.setTextColor(getResources().getColor(R.color.red));
        }

    }
}
