package com.message.service;

import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by lex on 2016/12/27.
 */
@Service
public class SerializerService {

    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object deserialize(byte[] bytes) {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            ObjectInputStream o = new ObjectInputStream(b);
            return o.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
