package net.rex.italker.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Util for collection

*/
public class CollectionUtil {

    /**
     *  Convert List collection to array
     * @param items list
     * @param tClass the type of the data
     * @param <T> Class
     * @return conveted array
     */
    // question : using /unchecked/ here??
    public static <T> T[] toArray(List<T> items, Class<T> tClass){
        if(items == null || items.size() == 0){
            return null;
        }
        int size = items.size();
        try{
            T[] array = (T[]) Array.newInstance(tClass, size);
            return items.toArray(array);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /***
     *  Convert set collection to array
     * @param items set to be converted
     * @param tClass the data type
     * @param <T> class
     * @return converted array
     */
    // question : using /unchecked/ here??
    public static <T> T[] toArray(Set<T> items, Class<T> tClass){
        if(items == null || items.size() == 0){
            return null;
        }
        int size = items.size();
        try{
            T[] array = (T[]) Array.newInstance(tClass, size);
            return items.toArray(array);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert array to a hashset
     * @param items array
     * @param <T>  Class
     * @return converted hash set
     */
    public static <T> HashSet<T> toHashSet(T[] items){
        if(items == null || items.length == 0){
            return null;
        }
        HashSet<T> set = new HashSet<>();
        Collections.addAll(set,items);
        return set;
    }


    /**
     * Convert Array to ArrayList
     * @param items array
     * @param <T> class
     * @return converted arrayList
     */
    public static <T> ArrayList<T> toArrayList(T[] items){
        if(items == null || items.length == 0){
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list,items);
        return list;
    }



}
