package nz.massey.a336.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import nz.massey.a336.timeline.databinding.ActivityMainBinding;
import nz.massey.a336.timeline.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    private ActivitySettingsBinding mSettingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsLayout = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mSettingsLayout.getRoot());

        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog(SettingsActivity.this, 50, 0, 0);

        mSettingsLayout.menuContemporary.setOnClickListener((view) -> {
            colorPickerDialog.show(getSupportFragmentManager(),null);
            Log.i(TAG, "");
        });

        colorPickerDialog.setCallback(new ColorPickerDialog.ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                mSettingsLayout.colorPreviewMenuContemporary.setBackgroundColor(color);

            }
        });

    }
}
