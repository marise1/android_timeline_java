package nz.massey.a336.timeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import nz.massey.a336.timeline.databinding.ActivityMainBinding;
import nz.massey.a336.timeline.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    private ActivitySettingsBinding mSettingsLayout;
    private int colorSettings;// = Color.parseColor("#7D7D7D");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();*/

        mSettingsLayout = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mSettingsLayout.getRoot());

        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        colorSettings = pref.getInt("color", getResources().getColor(R.color.contemporary));

        mSettingsLayout.colorPreviewMenuContemporary.setBackgroundColor(colorSettings);

        int red = Color.red(colorSettings);
        int green = Color.green(colorSettings);
        int blue = Color.blue(colorSettings);
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog(SettingsActivity.this, red, green, blue);

        colorPickerDialog.setCallback(new ColorPickerDialog.ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {

                colorSettings = color;
                mSettingsLayout.colorPreviewMenuContemporary.setBackgroundColor(colorSettings);

                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("color", colorSettings);
                editor.apply();
            }
        });

        mSettingsLayout.menuContemporary.setOnClickListener((view) -> {
            colorPickerDialog.show(getSupportFragmentManager(),null);
            Log.i(TAG, "");
        });
    }

/*
    public static class SettingsFragment extends PreferenceFragmentCompat{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

        }

    }
*/

}
