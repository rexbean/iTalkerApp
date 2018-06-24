package net.rex.italker.common.tools;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;



public class UiTools {
    private static int STATUS_BAR_HEIGHT = -1;

    public static int getStatusBarHeight(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && STATUS_BAR_HEIGHT == -1){
            try{
                final Resources res = activity.getResources();
                // try to get the value which corresponds to the attr "status_bar_height"
                int resourceId = res.getIdentifier("status_bar_height","dimen","android");
                if(resourceId == 0){
                    Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                    Object object = clazz.newInstance();
                    resourceId = Integer.parseInt(clazz.getField("status_bar_height")
                            .get(object).toString());
                }
                if(resourceId > 0){
                    STATUS_BAR_HEIGHT = activity.getResources().getDimensionPixelSize(resourceId);
                }
                if(STATUS_BAR_HEIGHT <= 0){
                    Rect rectangle = new Rect();
                    Window window = activity.getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    STATUS_BAR_HEIGHT = rectangle.top;
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return STATUS_BAR_HEIGHT;
    }

    /**
     * get the width of the screen
     * @param activity the owner activity
     * @return the screen width
     */
    public static int getScreenWidth(Activity activity){
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * get the screen height
     * @param activity the owner activity
     * @return the screen height
     */
    public static int getScreenHeight(Activity activity){
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
