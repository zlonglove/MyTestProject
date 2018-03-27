package com.ISHello.Serialization;

import com.ISHello.utils.SdcardUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by zhanglong on 2017/9/28.
 */

public class SerializationManeger {

    private static SerializationManeger serializationManeger;

    private SerializationManeger() {

    }

    public static SerializationManeger getInstance() {
        if (serializationManeger == null) {
            synchronized (SerializationManeger.class) {
                if (serializationManeger == null) {
                    serializationManeger = new SerializationManeger();
                }
            }
        }
        return serializationManeger;
    }

    private void writeCache(Object object) {
        if (SdcardUtils.checkSDCard()) {
            File file = null;
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                file = new File(SdcardUtils.getSDCardPathWithFileSeparators() + "banner");
                if (file != null) {
                    fileOutputStream = new FileOutputStream(file);
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    if (objectOutputStream != null) {
                        objectOutputStream.writeObject(object);
                        objectOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private Object readCache() {
        Object object = null;
        if (SdcardUtils.checkSDCard()) {
            File file = null;
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                file = new File(SdcardUtils.getSDCardPathWithFileSeparators() + "banner");
                if (file != null) {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    if (ois != null) {
                        object = ois.readObject();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return object;
    }
}
