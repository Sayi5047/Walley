package io.hustler.wallzy.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class DimenUtils {
    public static float convertDptoPixels(float dp, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
