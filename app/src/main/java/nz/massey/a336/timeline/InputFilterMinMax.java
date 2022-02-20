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
            // Removes string that is to be replaced from destination and adds the new string in
            String newVal = dest.subSequence(0, dstart) + source.subSequence(start, end).toString() + dest.subSequence(dend, dest.length());
            int input = Integer.parseInt(newVal);
            if(isInRange(min, max, input))
                return null;
        } catch(NumberFormatException e){ }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b>a ? (c>=a && c<=b) : (c>=b && c<=a);
    }

}
