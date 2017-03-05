package kz.taxikz.utils;

import android.support.annotation.NonNull;

import java.util.List;

import kz.taxikz.helper.DataHelper;

public class PhoneUtil {

    public static boolean isMaskValid(@NonNull String mask){
        int maskInt = Integer.valueOf(mask);
        List<Integer> masks = DataHelper.getMasks();
        return masks.contains(maskInt);
    }
}
