package nz.massey.a336.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Bar extends View {

    private Paint mPaintPeriod;
    private Paint mPaintText;

    private void init(){
        mPaintPeriod = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPeriod.setColor(Color.parseColor("#7D7D7D"));
        mPaintPeriod.setStrokeWidth(30);

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(35f);
    }

    public Bar(Context context) {
        this(context, null);
    }
    public Bar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public Bar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // specify size of view,
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // size imposed by parent
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // actual size on the screen
/*        int width = getWidth();
        int height = getHeight();

        int widthFinal = 10;
        int heightFinal = 10;

        if (widthMode == MeasureSpec.AT_MOST) {
            widthFinal = Math.min(width, widthSize);
        } else {
            widthFinal = width;
        }*/

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = getWidth()/2;
        int textPosX = center - 10;
        int offset = 5;
        int barPosX = center + 70;
        int barPosY = 150;
        double scale = 3;

        // Map
/*
        canvas.drawText("<Map>", center+20, 100, mPaintText);
        canvas.save();
*/

        //mPaintText.setTextSize(30);

        // contemporary
        mPaintPeriod.setColor(Color.parseColor("#7D7D7D"));
        canvas.drawLine(barPosX, barPosY, barPosX, (int)(barPosY + 20*scale), mPaintPeriod);
        canvas.drawText("2100", textPosX, barPosY, mPaintText);
        barPosY += 20*scale;

        // modern1
        mPaintPeriod.setColor(Color.parseColor("#6EC6AF"));
        canvas.drawLine(barPosX, barPosY, barPosX, (int)(barPosY + 20*scale), mPaintPeriod);
        canvas.drawText("1900", textPosX, barPosY + offset, mPaintText);
        barPosY += 20*scale;

        // modern2
        mPaintPeriod.setColor(Color.parseColor("#B69F31"));
        canvas.drawLine(barPosX, barPosY, barPosX, (int)(barPosY + 20*scale), mPaintPeriod);
        canvas.drawText("1700", textPosX, barPosY + offset, mPaintText);
        barPosY += 20*scale;

        // medieval
        mPaintPeriod.setColor(Color.parseColor("#365A2B"));
        canvas.drawLine(barPosX, barPosY, barPosX, (int)(barPosY + 100*scale), mPaintPeriod);
        canvas.drawText("1500", textPosX, barPosY + offset, mPaintText);
        barPosY += 100*scale;

        // classical
        mPaintPeriod.setColor(Color.parseColor("#C56E29"));
        canvas.drawLine(barPosX, barPosY, barPosX, (int)(barPosY + 110*scale), mPaintPeriod);
        canvas.drawText("500", textPosX, barPosY + offset, mPaintText);
        barPosY += 110*scale;

        // prehistory
        mPaintPeriod.setColor(Color.parseColor("#995023"));
        canvas.drawLine(barPosX, barPosY, barPosX, (int)(barPosY + 240*scale), mPaintPeriod);
        canvas.drawText("-600", textPosX, barPosY + offset, mPaintText);
        barPosY += 240*scale;

        canvas.drawText("-3000", textPosX, barPosY + offset, mPaintText);

    }
}
