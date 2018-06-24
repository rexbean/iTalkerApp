package net.rex.italker.common.app;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

public class Application extends android.app.Application {
    //single instance;
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * get the single instance from outside
     * @return instance
     */
    public static Application getInstance(){
        return instance;
    }

    /**
     * get the dir of the cache
     * @return return the app cache dir address
     */
    public static File getCacheDirFile(){
        return instance.getCacheDir(); // here can be null when the app haven't setup
    }

    /**
     * get the audio file's address
     * @param isTmp whether a cache file or not. If true, every time the address will be same
     * @return the tmp address
     */
    public static File getAudioTmpFile(boolean isTmp){
        File dir = new File(getCacheDirFile(),"audio");

        dir.mkdirs();
        File[] files = dir.listFiles();
        if(files != null && files.length > 0){
            for(File file : files){
                file.delete();
            }
        }

        File path = new File(getCacheDirFile(),isTmp? "tmp.mp3" : SystemClock.uptimeMillis()+".mp3");
        return path.getAbsoluteFile();
    }


    /**
     * get the tmp address of the avatar
     * @return the tmp address
     */
    public static File getPortaitTmpFile(){
        //get the path of the portrait dir's cache address
        File dir = new File(getCacheDirFile(),"portrait");
        // create all responding dirs
        dir.mkdirs();

        //remove old files
        File[] files = dir.listFiles();
        if(files != null && files.length >0){
            for (File file : files) {
                file.delete();
            }

        }
        //return a file path with timestamp
        File path = new File(dir, SystemClock.uptimeMillis()+".jpg");
        return path.getAbsoluteFile();
    }

    /**
     * show a toast
     * @param msg the message want to show
     */
    public static void showToast(final String msg){
        //toast can be only used in the main thread
        //Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
        // here are for the thread switch
        Run.onUiAsync(new Action(){
            @Override
            public void call(){
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * show a toast from a resource
     * @param msgId the id of the string resource
     */
    public static void showToast(@StringRes int msgId){
        showToast(instance.getString(msgId));
    }
}
