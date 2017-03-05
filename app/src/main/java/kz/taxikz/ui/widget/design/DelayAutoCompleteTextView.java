package kz.taxikz.ui.widget.design;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.wang.avi.AVLoadingIndicatorView;

public class DelayAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private AVLoadingIndicatorView mLoadingIndicator;

    private ImageView mClearButton;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!this.isInEditMode()) {
            RobotoTextViewUtils.initTypeface(this, context, attrs);
        }
    }

//    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        if(!this.isInEditMode()) {
//            RobotoTextViewUtils.initTypeface(this, context, attrs);
//        }
//
//    }

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!this.isInEditMode()) {
            RobotoTextViewUtils.initTypeface(this, context, attrs);
        }

    }

    public void setLoadingIndicator(AVLoadingIndicatorView progressBar) {
        mLoadingIndicator = progressBar;
    }

    public void setClearButton(ImageView clearButton) {
        this.mClearButton = clearButton;
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        if (mClearButton != null) {
            mClearButton.setVisibility(View.GONE);
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        if (mClearButton != null) {
            mClearButton.setVisibility(View.VISIBLE);
        }
        super.onFilterComplete(count);
    }


//    @Override
//    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//        //Passing FALSE as the SECOND ARGUMENT (fullEditor) to the constructor
//        // will result in the key events continuing to be passed in to this
//        // view.  Use our special BaseInputConnection-derived view
//        InputConnectionAccomodatingLatinIMETypeNullIssues baseInputConnection =
//                new InputConnectionAccomodatingLatinIMETypeNullIssues(this, false);
//
//        //In some cases an IME may be able to display an arbitrary label for a
//        // command the user can perform, which you can specify here.  A null value
//        // here asks for the default for this key, which is usually something
//        // like Done.
//        outAttrs.actionLabel = null;
//
//        //Special content type for when no explicit type has been specified.
//        // This should be interpreted (by the IME that invoked
//        // onCreateInputConnection())to mean that the target InputConnection
//        // is not rich, it can not process and show things like candidate text
//        // nor retrieve the current text, so the input method will need to run
//        // in a limited "generate key events" mode.  This disables the more
//        // sophisticated kinds of editing that use a text buffer.
//        outAttrs.inputType = InputType.TYPE_NULL;
//
//        //This creates a Done key on the IME keyboard if you need one
//        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
//
//        return baseInputConnection;
//    }
}