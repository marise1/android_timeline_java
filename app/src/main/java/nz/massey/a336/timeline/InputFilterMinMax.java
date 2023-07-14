package nz.massey.a336.timeline;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

public class InputFilterMinMax implements InputFilter {

    public static final String TAG = "inputFilter";

    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        // 0-255
        try{
            int kk = Integer.parseInt(dest.toString() + source.toString());
            Log.i(TAG, "dstart1: "+dest.toString() + ", source: " + source.toString());

            // dstart: existing text when try to type on end
            // source: what just typed
            // dend: existing text when try to type on front
            String input = dest.subSequence(0, dstart) + source.subSequence(start, end).toString()
                    + dest.subSequence(dend, dest.length());

            Log.i(TAG, "dstart2: "+dest.subSequence(0, dstart)
                    + ", source: " + source.subSequence(start, end).toString() +
                    ", dend: " + dest.subSequence(dend, dest.length()));

            int value = Integer.parseInt(input);

            // check what typed is in the range
            if(isInRange(min, max, value)){
                return null;
            }
        } catch(NumberFormatException e){ }

        // don't type if not in range
        return "";
    }

    // 0 255 150
    // if b>a is true, do left expression
    private boolean isInRange(int a, int b, int c) {
        return b>a ? (c>=a && c<=b) : (c>=b && c<=a);
    }
}
