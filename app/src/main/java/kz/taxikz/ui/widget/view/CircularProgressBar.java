package kz.taxikz.ui.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import kz.taxikz.utils.AndroidUtils;


public class CircularProgressBar extends BaseView {
    static final String ANDROID_XML = "http://schemas.android.com/apk/res/android";
    public static boolean animationEnabled = true;
    int arcD = 1;
    int arcO = 0;
    int backgroundColor = Color.parseColor((String) "#1E88E5");
    int cont = 0;
    boolean firstAnimationOver = false;
    int limite = 0;
    float radius1 = 0.0f;
    float radius2 = 0.0f;
    float rotateAngle = 0.0f;

    public CircularProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setAttributes(attributeSet);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void drawFirstAnimation(Canvas canvas) {
        if (this.radius1 < (float) (this.getWidth() / 2)) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(this.makePressColor());
            this.radius1 = this.radius1 >= (float) (this.getWidth() / 2) ? (float) this.getWidth() / 2.0f : this.radius1 + 2.0f;
            canvas.drawCircle((float) (this.getWidth() / 2), (float) (this.getHeight() / 2), this.radius1, paint);
        } else {
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(this.makePressColor());
            canvas2.drawCircle((float) (this.getWidth() / 2), (float) (this.getHeight() / 2), (float) (this.getHeight() / 2), paint);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            if (this.cont >= 50) {
                this.radius2 = this.radius2 >= (float) (this.getWidth() / 2) ? (float) this.getWidth() / 2.0f : this.radius2 + 1.0f;
            } else {
                this.radius2 = this.radius2 >= (float) (this.getWidth() / 2 - AndroidUtils.convertDpToPx(this.getContext(), 4)) ? (float) this.getWidth() / 2.0f - (float) AndroidUtils.convertDpToPx(this.getContext(), 4) : this.radius2 + 3.0f;
            }
            canvas2.drawCircle((float) (this.getWidth() / 2), (float) (this.getHeight() / 2), this.radius2, paint);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint());
            if (this.radius2 >= (float) (this.getWidth() / 2 - AndroidUtils.convertDpToPx(this.getContext(), 4))) {
                ++this.cont;
            }
            if (this.radius2 < (float) (this.getWidth() / 2)) return;
            {
                this.firstAnimationOver = true;
            }
        }
    }

    private void drawSecondAnimation(Canvas canvas) {
        if (this.arcO == this.limite) {
            this.arcD += 6;
        }
        if (this.arcD >= 290 || this.arcO > this.limite) {
            this.arcO += 6;
            this.arcD -= 6;
        }
        if (this.arcO > this.limite + 290) {
            this.arcO = this.limite = this.arcO;
            this.arcD = 1;
        }
        this.rotateAngle += 4.0f;
        canvas.rotate(this.rotateAngle, (float) (this.getWidth() / 2), (float) (this.getHeight() / 2));
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.backgroundColor);
        canvas2.drawArc(new RectF(0.0f, 0.0f, (float) this.getWidth(), (float) this.getHeight()), (float) this.arcO, (float) this.arcD, true, paint);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas2.drawCircle((float) (this.getWidth() / 2), (float) (this.getHeight() / 2), (float) (this.getWidth() / 2 - AndroidUtils.convertDpToPx(this.getContext(), 4)), paint);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint());
    }

    protected int makePressColor() {
        return Color.argb(128, this.backgroundColor >> 16 & 255, this.backgroundColor >> 8 & 255, this.backgroundColor & 255);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.firstAnimationOver) {
            this.drawFirstAnimation(canvas);
            if (animationEnabled) {
                this.invalidate();
            }
        }
        if (this.cont > 0) {
            this.drawSecondAnimation(canvas);
            if (animationEnabled) {
                this.invalidate();
            }
        }
    }

    public void reset() {
        this.radius1 = 0.0f;
        this.radius2 = 0.0f;
        this.cont = 0;
        this.firstAnimationOver = false;
        this.arcD = 1;
        this.arcO = 0;
        this.rotateAngle = 0.0f;
        this.limite = 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void setAttributes(AttributeSet attributeSet) {
        setMinimumHeight(AndroidUtils.convertDpToPx(getContext(), 32));
        setMinimumWidth(AndroidUtils.convertDpToPx(getContext(), 32));
        int n = attributeSet.getAttributeResourceValue(ANDROID_XML, "background", -1);
        if (n != -1) {
            setBackgroundColor(ContextCompat.getColor(getContext(), n));
        } else {
            n = attributeSet.getAttributeIntValue(ANDROID_XML, "background", -1);
            if (n != -1) {
                setBackgroundColor(n);
            } else {
                setBackgroundColor(Color.parseColor("#1E88E5"));
            }
        }
        setMinimumHeight(AndroidUtils.convertDpToPx(getContext(), 3));
    }

    public void setBackgroundColor(int n) {
        super.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
        if (isEnabled()) {
            beforeBackground = backgroundColor;
        }
        backgroundColor = n;
    }
}

