package kz.taxikz.helper;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.TextView;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.taxi4.R;
import kz.taxikz.utils.PhoneUtil;
import timber.log.Timber;

public final class UiHelper {
    private UiHelper() {
    }

    public static boolean isValidPassword(EditText editText, TextInputLayout textInputLayout){
        String incorrectPassword = TaxiKzApp.get().getString(R.string.incorrect_password);
        boolean isValid = editText.getText().toString().equals(TaxiKzApp.storage.getPassword());
            if(!isValid){
                setEditTextError(incorrectPassword, editText, textInputLayout);
            }
        return isValid;
    }
    public static boolean isEmptyField(EditText editText, TextInputLayout textInputLayout) {
        String errorText = TaxiKzApp.get().getString(R.string.empty_field);
        boolean isEmpty = TextUtils.isEmpty(editText.getText().toString());
        if (isEmpty) {
            setEditTextError(errorText, editText, textInputLayout);
        }
        return isEmpty;
    }

    public static boolean isValidPhone(EditText editText, TextInputLayout textInputLayout){
        String invalidPhone = TaxiKzApp.get().getString(R.string.invalid_number);
        String mask;
        if(editText.getText().length()>3){
            mask = getPhoneMaskFromEditText(editText);
        }else{
            setEditTextError(invalidPhone, editText, textInputLayout);
            return false;
        }

        Timber.d("mask "+mask);
        boolean isValid = PhoneUtil.isMaskValid(mask);
        if(isValid){
            textInputLayout.setError(null);
        }else{
            setEditTextError(invalidPhone, editText, textInputLayout);
        }
        return isValid;
    }
    public static boolean isEmptyField(EditText editText) {
        return isEmptyField(editText, null);
    }

    public static void setEditTextError(String errorText, EditText editText) {
        setEditTextError(errorText, editText, null);
    }

    public static String getPhoneFromEditText(EditText mPhone) {
        return mPhone.getText().toString().replace("+","").replace("(","").replace(")","").replace("-","").replace(" ","").substring(1);
    }

    public static String getPhoneMaskFromEditText(EditText editText) {
        return getPhoneFromEditText(editText).substring(0, 3);
    }
    public static void setEditTextError(String errorText, EditText editText, TextInputLayout textInputLayout) {
        if (textInputLayout == null) {
            editText.setError(errorText);
            return;
        }
        textInputLayout.setError(errorText);
        textInputLayout.setErrorEnabled(true);
    }

    public static void markSearchTerm(TextView textView, String searchTerm) {
        if (!TextUtils.isEmpty(textView.getText()) && !TextUtils.isEmpty(searchTerm)) {
            String wholeText = textView.getText().toString();
            int indexOf = textView.getText().toString().toLowerCase().indexOf(searchTerm.toLowerCase());
            SpannableStringBuilder sb = new SpannableStringBuilder(wholeText);
            StyleSpan bss = new StyleSpan(1);
            if (indexOf >= 0 && indexOf <= wholeText.length()) {
                sb.setSpan(bss, indexOf, searchTerm.length() + indexOf, 18);
                textView.setText(sb);
            }
        }
    }

    public static void setTitle(FragmentActivity activity, int title) {
        activity.setTitle(title);
    }

    public static void setTitle(FragmentActivity activity, String title) {
        activity.setTitle(title);
    }

}
