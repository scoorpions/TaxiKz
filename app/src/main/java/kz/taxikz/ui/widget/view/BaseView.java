/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.util.AttributeSet
 *  android.widget.RelativeLayout
 */
package kz.taxikz.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BaseView extends RelativeLayout {

    static final String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    static final String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    boolean animation = false;
    int beforeBackground;
    final int disabledBackgroundColor = Color.parseColor((String)"#E2E2E2");
    public boolean isLastTouch = false;

    public BaseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onAnimationEnd() {
        super.onAnimationEnd();
        this.animation = false;
    }

    protected void onAnimationStart() {
        super.onAnimationStart();
        this.animation = true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.animation) {
            this.invalidate();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setEnabled(boolean bl) {
        super.setEnabled(bl);
        if (bl) {
            this.setBackgroundColor(this.beforeBackground);
        } else {
            this.setBackgroundColor(this.disabledBackgroundColor);
        }
        this.invalidate();
    }
}

