package kz.taxikz.helper;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import timber.log.Timber;

public class SerializerHelper {

    public static <T> T readObject(Class<T> cls, String fileName) throws IOException {
        T result = null;
        try {
            result = (T) new ObjectInputStream(TaxiKzApp.get().openFileInput(fileName)).readObject();
        } catch (Exception e) {
            Timber.e(e.toString());
        }
        return result;
    }

    public static void storeObject(Serializable object, String fileName) {
        try {
            new ObjectOutputStream(TaxiKzApp.get().openFileOutput(fileName, 0)).writeObject(object);
        } catch (IOException e) {
            Timber.d(Log.getStackTraceString(e));
        }
    }

    public static <T> T readObject(String fileName) {
        T result = null;
        try {
            result = (T) new ObjectInputStream(TaxiKzApp.get().openFileInput(fileName)).readObject();
        } catch (Exception e) {
            storeObject(null, fileName);
            Timber.e(e.toString());
        }
        return result;
    }

    public static void storeObjects(List<? extends Serializable> object, String fileName) {
        try {
            new ObjectOutputStream(TaxiKzApp.get().openFileOutput(fileName, 0)).writeObject(object);
        } catch (IOException e) {
            Timber.d(Log.getStackTraceString(e));
        }
    }

    public static <T> List<T> readObjects(String fileName) {
        List<T> result = null;
        try {
            result = (List<T>) new ObjectInputStream(TaxiKzApp.get().openFileInput(fileName)).readObject();
        } catch (Exception e) {
            storeObjects(null, fileName);
            Timber.e(e.toString());
        }
        return result;
    }
}
