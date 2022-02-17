package nz.massey.a336.timeline;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import nz.massey.a336.timeline.databinding.ColorPickerDialogBinding;

public class ColorPickerDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener{

    public static final String TAG = "ColorPickerDialog";

    private ColorPickerDialogBinding mColorPickerDialogLayout;
    private ColorPickerCallback callback;
    private ColorPickerDialogBinding binding;

    private int red;
    private int green;
    private int blue;
    private SeekBar seekBarR, seekBarG, seekBarB;

    public interface ColorPickerCallback {
        void onColorChosen(int color);
    }

    public void setCallback(ColorPickerCallback listener) {
        callback = listener;
    }

    public ColorPickerDialog(Activity activity,
                             @IntRange(from = 0, to = 255) int r,
                             @IntRange(from = 0, to = 255) int g,
                             @IntRange(from = 0, to = 255) int b) {
        red = r;
        green = g;
        blue = b;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        binding = ColorPickerDialogBinding.inflate(LayoutInflater.from(getActivity()));

        binding.barRed.setOnSeekBarChangeListener(this);
        binding.barGreen.setOnSeekBarChangeListener(this);
        binding.barBlue.setOnSeekBarChangeListener(this);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mColorPickerDialogLayout = ColorPickerDialogBinding.inflate(inflater, container, false);
        View view = mColorPickerDialogLayout.getRoot();

        mColorPickerDialogLayout.btnSaveDialog.setOnClickListener((v) -> {

            try {
                red = Integer.parseInt(mColorPickerDialogLayout.txtRedVal.getText().toString());
                green = Integer.parseInt(mColorPickerDialogLayout.txtGreenVal.getText().toString());
                blue = Integer.parseInt(mColorPickerDialogLayout.txtBlueVal.getText().toString());

            } catch(NumberFormatException e){
                e.printStackTrace();
                Log.i(TAG, "not number");
                return;
            }
            int color = Color.rgb(red, green, blue);

            if (callback != null)
                callback.onColorChosen(color);

            dismiss();
        });

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar.getId() == R.id.barRed) {
            red = progress;

        } else if(seekBar.getId() == R.id.barGreen) {
            green = progress;
        } else{
            blue = progress;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
