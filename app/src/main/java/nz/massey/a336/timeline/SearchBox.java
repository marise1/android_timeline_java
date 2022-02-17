package nz.massey.a336.timeline;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class SearchBox extends androidx.appcompat.widget.AppCompatEditText {

    private Drawable mBtnClear;

    public SearchBox(@NonNull Context context) {
        super(context);
        init();
    }

    public SearchBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mBtnClear = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_clear_24_opaque, null);

        addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // at the end of text where drawable is
                if ((getCompoundDrawablesRelative()[2] != null)) {

                    float clearButtonStart; // left to right
                    float clearButtonEnd;  // RTL
                    boolean isClearButtonClicked = false;

                    // button at left side
                    if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {

                        clearButtonEnd = mBtnClear.getIntrinsicWidth() + getPaddingStart();
                        if (event.getX() < clearButtonEnd) {
                            isClearButtonClicked = true;
                        }
                    } else {
                        clearButtonStart = (getWidth() - getPaddingEnd() - mBtnClear.getIntrinsicWidth());
                        if (event.getX() > clearButtonStart) {
                            isClearButtonClicked = true;
                        }
                    }

                    if (isClearButtonClicked) {

                        // if pressed, not opaque
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            mBtnClear = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_clear_24, null);
                            showClearButton();
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            mBtnClear = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_clear_24_opaque, null);

                            getText().clear();
                            hideClearButton();
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

    }

    private void showClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mBtnClear, null);
    }

    private void hideClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
    }
}
