package net.rex.italker.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import net.rex.italker.factory.Factory;
import net.rex.italker.utils.HashUtil;

import java.io.File;

import java.util.Date;

/**
 * used to upload anything to Ali oss
 */
public class UploadHelper {
    private static final String TAG = UploadHelper.class.getSimpleName();
    private static final String ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";
    // the bucket name in the OSS
    private static final String BUCKET_NAME = "italker";
    private static OSS getClient(){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "LTAIYQD07p05pHQW","2txxzT8JXiHKEdEjylumFy6sXcDQ0G");
        return new OSSClient(Factory.app(), ENDPOINT,credentialProvider);
    }

    /**
     * the final method of uploading, return a path, if successful
     * @param objKey unique hash key, use '/' as directory
     * @param path path of the uploading file
     * @return path
     */
    private static String upload(String objKey, String path){
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,
                objKey, path);

        try{
            OSS client  = getClient();
            // begin synchronous upload
            PutObjectResult result = client.putObject(request);
            // an url from the network outside/ public visit method
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            Log.d(TAG, String.format("publicObjectURL:%s", url));
            return url;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * upload normal image
     * @param path the path of the image
     * @return the resulot of upload
     */
    public static String uploadImage(String path){
        String key = getImageObjKey(path);
        return upload(key,path);
    }

    public static String uploadPortrait(String path){
        String key = getPortraitObjKey(path);
        return upload(key,path);
    }

    public static String uploadAudio(String path){
        String key = getAudioObjKey(path);
        return upload(key,path);
    }

    /**
     * store the file by date
     * @return the timestamp
     */
    public static String getDateString(){
        return DateFormat.format("yyyyMM", new Date()).toString();
    }
    // Image/201704/xxxx.jpg
    private static String getImageObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDateString();
        return String.format("Image/%s/%s.jpg",dateString,fileMd5);

    }
    // portrait/201704/xxxx.jpg
    private static String getPortraitObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDateString();
        return String.format("Portrait/%s/%s.jpg",dateString,fileMd5);
    }
    private static String getAudioObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDateString();
        return String.format("Audio/%s/%s.jpg",dateString,fileMd5);
    }
}
