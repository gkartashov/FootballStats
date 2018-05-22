package com.example.jg.footballstats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class ObjectSerializer {
    public static String serialize(Serializable serializable) {
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        String result = null;
        try{
            byteArrayOutputStream= new ByteArrayOutputStream();
            objectOutputStream= new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            byteArrayOutputStream.close();
            result = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.ISO_8859_1);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        return result;
    }

    public static Object deserialize(String serialized) {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        Object object = null;
        if (serialized != null) {
            byte[] serializedBytes = serialized.getBytes(StandardCharsets.ISO_8859_1);
            try {
                byteArrayInputStream = new ByteArrayInputStream(serializedBytes);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                object = objectInputStream.readObject();
                byteArrayInputStream.close();
                objectInputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
