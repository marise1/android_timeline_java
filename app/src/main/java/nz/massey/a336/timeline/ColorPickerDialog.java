package nz.massey.a336.timeline;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

    private int red = 0;
    private int green = 0;
    private int blue = 0;
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

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mColorPickerDialogLayout = ColorPickerDialogBinding.inflate(inflater, container, false);
        View view = mColorPickerDialogLayout.getRoot();

        mColorPickerDialogLayout.barRed.setOnSeekBarChangeListener(this);
        mColorPickerDialogLayout.barGreen.setOnSeekBarChangeListener(this);
        mColorPickerDialogLayout.barBlue.setOnSeekBarChangeListener(this);

        mColorPickerDialogLayout.txtRedVal.addTextChangedListener(new EditTextVal(mColorPickerDialogLayout.txtRedVal));
        mColorPickerDialogLayout.txtGreenVal.addTextChangedListener(new EditTextVal(mColorPickerDialogLayout.txtGreenVal));
        mColorPickerDialogLayout.txtBlueVal.addTextChangedListener(new EditTextVal(mColorPickerDialogLayout.txtBlueVal));


        mColorPickerDialogLayout.txtRedVal.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        mColorPickerDialogLayout.txtGreenVal.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        mColorPickerDialogLayout.txtBlueVal.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});

        // set rgb with init value
        mColorPickerDialogLayout.txtRedVal.setText("" + red);
        mColorPickerDialogLayout.txtGreenVal.setText("" + green);
        mColorPickerDialogLayout.txtBlueVal.setText("" + blue);

        mColorPickerDialogLayout.barRed.setProgress(red);
        mColorPickerDialogLayout.barGreen.setProgress(green);
        mColorPickerDialogLayout.barBlue.setProgress(blue);

        mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));


        mColorPickerDialogLayout.btnSaveDialog.setOnClickListener((v) -> {

/*            try {
                red = Integer.parseInt(mColorPickerDialogLayout.txtRedVal.getText().toString());
                green = Integer.parseInt(mColorPickerDialogLayout.txtGreenVal.getText().toString());
                blue = Integer.parseInt(mColorPickerDialogLayout.txtBlueVal.getText().toString());

            } catch(NumberFormatException e){
                Log.i(TAG, "not typed");

            }*/
            int color = Color.rgb(red, green, blue);

            if (callback != null)
                callback.onColorChosen(color);

            dismiss();
        });

        mColorPickerDialogLayout.btnClear.setOnClickListener((v) -> {
            dismiss();
        });


        return view;
    }

    // seekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.barRed) {
            red = progress;
            mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));

            mColorPickerDialogLayout.txtRedVal.setText("" + progress);
            //Log.i(TAG, "red " + progress);

        } else if (seekBar.getId() == R.id.barGreen) {
            green = progress;
            mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));

            mColorPickerDialogLayout.txtGreenVal.setText("" + progress);
        } else {
            blue = progress;
            mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));

            mColorPickerDialogLayout.txtBlueVal.setText("" + progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private class EditTextVal implements TextWatcher {

        private View view;

        private EditTextVal(View v) {
            view = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {

            if(view.getId() == R.id.txtRedVal){
                red = Integer.parseInt(mColorPickerDialogLayout.txtRedVal.getText().toString());

                mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));
                mColorPickerDialogLayout.barRed.setProgress(red);
                //Log.i(TAG, "text changed ");

            } else if(view.getId() == R.id.txtGreenVal){
                green = Integer.parseInt(mColorPickerDialogLayout.txtGreenVal.getText().toString());

                mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));
                mColorPickerDialogLayout.barGreen.setProgress(green);
            } else{
                blue = Integer.parseInt(mColorPickerDialogLayout.txtBlueVal.getText().toString());

                mColorPickerDialogLayout.colorPreviewDialog.setBackgroundColor(Color.rgb(red, green, blue));
                mColorPickerDialogLayout.barBlue.setProgress(blue);
            }
        }

    }
}