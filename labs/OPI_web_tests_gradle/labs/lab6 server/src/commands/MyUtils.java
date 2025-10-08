package commands;

import java.io.*;
import java.nio.ByteBuffer;

public class MyUtils {
    public static ByteBuffer serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return ByteBuffer.wrap(baos.toByteArray());
    }

    public static Object deserialize(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}
