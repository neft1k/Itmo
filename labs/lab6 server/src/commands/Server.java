package commands;

import models.Response;
import utility.CollectionManager;
import utility.Console;
import utility.DumpManager;
import utility.StandartConsole;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

//public class Server extends Thread implements Serializable {
//    public static void setProcessor(CommandProcessor processor) {
//        Server.processor = processor;
//    }
//
//    private static CommandProcessor processor;
//
//    public static void setCollectionManager(CollectionManager collectionManager) {
//        Server.collectionManager = collectionManager;
//    }
//
//    private static CollectionManager collectionManager;
//    public static CommandProcessor getProcessor(){
//        return Server.processor;
//    }
//    public void run() {
//        try {
//            ServerSocketChannel serverSocket = ServerSocketChannel.open();
//            serverSocket.bind(new InetSocketAddress(2324));
//            serverSocket.configureBlocking(false);
//
//            Selector selector = Selector.open();
//            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
//
//            while (true) {
//                selector.select();
//                Set<SelectionKey> selectedKeys = selector.selectedKeys();
//                Iterator<SelectionKey> iterator = selectedKeys.iterator();
//
//                while (iterator.hasNext()) {
//                    SelectionKey key = iterator.next();
//
//                    if (key.isAcceptable()) {
//                        // Client connected
//                        SocketChannel client = serverSocket.accept();
//                        client.configureBlocking(false);
//                        client.register(selector, SelectionKey.OP_READ);
//                    } else if (key.isReadable()) {
//                        SocketChannel client = (SocketChannel) key.channel();
//                        ByteBuffer sizeBuffer = ByteBuffer.allocate(4); // Integer.BYTES == 4
//                        client.read(sizeBuffer);
//                        sizeBuffer.flip();
//                        int size = sizeBuffer.getInt();
//
//                        ByteBuffer dataBuffer = ByteBuffer.allocate(size);
//
//                        client.read(dataBuffer);
//                        dataBuffer.flip();
//                        Command receivedObject = (Command) fromByteBuffer(dataBuffer);
////                        System.out.println(receivedObject.getUser());
////
////                        System.out.println(receivedObject.getClass());
////                        System.out.println(receivedObject.getName());
//                        System.out.println(receivedObject.getClient());
////                        System.out.println(receivedObject.getId());
//                        System.out.println(receivedObject.getStudyGroup());
//
//                        Useless responce = new Useless("23");
//                        String[] userCommand = (receivedObject.getName() + " 1 1 s ").split(" ", 4);
//                        if (Objects.equals(receivedObject.getName(), "add") ){
//                            responce = Server.processor.processCommand(userCommand, receivedObject.getClient(), receivedObject.getStudyGroup());
//                        } else if (receivedObject.getName().equals("update id")) {
//                            responce = Server.processor.processCommand(userCommand, receivedObject.getId(), receivedObject.getStudyGroup());
//                        }   else if (receivedObject.getName().equals("remove_by_id")) {
//                            responce = Server.processor.processCommand(userCommand, receivedObject.getId());
//                        } else if (receivedObject.getName().equals("registration")) {
//                            responce = Server.processor.processCommand(userCommand, receivedObject.getUser());
//                        }else if (receivedObject.getName().equals("login")) {
//                            responce = Server.processor.processCommand(userCommand, receivedObject.getUser());
//                        }else {
//                            responce = Server.processor.processCommand(userCommand, receivedObject.getClient());
//                        }
//
//
//                        Response response2 = new Response(responce.getAnswer());
//                        // Serialize result back into buffer
//                        ByteBuffer bufferToWrite = toBuffer(response2);
//                        // Attach buffer to key
//                        key.attach(bufferToWrite);
//                        // Change interestops to write
//                        key.interestOps(SelectionKey.OP_WRITE);
//
//                    } else if (key.isWritable()) {
//                        // You are ready to send result back to the client
//                        SocketChannel client = (SocketChannel) key.channel();
//                        ByteBuffer buffer = (ByteBuffer) key.attachment();
//                        buffer.flip();
//                        client.write(buffer);
//                        client.close();
//                    }
//                    iterator.remove();
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    collectionManager.saveCollection();
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("There was an error: " + e);
//        }
//    }
//
//    /*
//    Converting object into ByteBuffer
//    @param object - serializable object
//    @return return object converted in ByteBuffer
//    @throws IOException input output exception
//     */
//    public static ByteBuffer toBuffer(Serializable object) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(object);
//        oos.flush();
//        byte[] data = baos.toByteArray();
//        int length = data.length + 4;
//        ByteBuffer writeBuffer = ByteBuffer.allocate(length);
//        writeBuffer.putInt(data.length);
//        writeBuffer.put(data);
//        return writeBuffer;
//    }
//
//    /*
//    Converting object into ByteBuffer
//    @param byteBuffer
//    @return return deserialized object
//    @throws IOException, ClassNotFoundException
//     */
//    public static Serializable fromByteBuffer(ByteBuffer buffer) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
//        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
//
//        Serializable response = (Serializable) objectInputStream.readObject();
//
//        objectInputStream.close();
//        bais.close();
//
//        return response;
//    }
//}

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;


public class Server extends Thread implements Serializable {
    private static CommandProcessor processor;
    private static CollectionManager collectionManager;

    private final ForkJoinPool readPool = new ForkJoinPool();
    private final ExecutorService processPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Lock readLock = new ReentrantLock();
    private final Lock writeLock = new ReentrantLock();

    public static void setProcessor(CommandProcessor processor) {
        Server.processor = processor;
    }

    public static void setCollectionManager(CollectionManager collectionManager) {
        Server.collectionManager = collectionManager;
    }

    public static CommandProcessor getProcessor() {
        return Server.processor;
    }

    public void run() {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(2324));
            serverSocket.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        // Client connected
                        SocketChannel client = serverSocket.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        readPool.submit(() -> handleRead(key));
                    } else if (key.isWritable()) {
                        handleWrite(key);
                    }
                    iterator.remove();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    collectionManager.saveCollection();
                }
            }
        } catch (IOException e) {
            System.out.println("There was an error: " + e);
        }
    }


    private void handleRead(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            readLock.lock();
            try {
                client.read(sizeBuffer);
            } finally {
                readLock.unlock();
            }
            sizeBuffer.flip();
            int size = sizeBuffer.getInt();

            ByteBuffer dataBuffer = ByteBuffer.allocate(size);
            readLock.lock();
            try {
                client.read(dataBuffer);
            } finally {
                readLock.unlock();
            }
            dataBuffer.flip();
            Command receivedObject = (Command) fromByteBuffer(dataBuffer);

            processPool.submit(() -> processCommand(key, receivedObject));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during read: " + e);
        }
    }

    private void processCommand(SelectionKey key, Command receivedObject) {
        try {
            Useless responce = new Useless("23");
            String[] userCommand = (receivedObject.getName() + " 1 1 s ").split(" ", 4);


            if (Objects.equals(receivedObject.getName(), "add")) {
                synchronized (collectionManager) {
                    responce = Server.processor.processCommand(userCommand, receivedObject.getStudyGroup(), receivedObject.getClient());
                }
            } else if (receivedObject.getName().equals("update id")) {
                synchronized (collectionManager) {
                    responce = Server.processor.processCommand(userCommand, receivedObject.getId(), receivedObject.getStudyGroup(), receivedObject.getClient());
                }
            } else if (receivedObject.getName().equals("remove_by_id")) {
                synchronized (collectionManager) {
                    responce = Server.processor.processCommand(userCommand, receivedObject.getId(), receivedObject.getClient());
                }
            } else if (receivedObject.getName().equals("registration")) {
                responce = Server.processor.processCommand(userCommand, receivedObject.getUser());
            } else if (receivedObject.getName().equals("login")) {
                responce = Server.processor.processCommand(userCommand, receivedObject.getUser());
            } else {
                responce = Server.processor.processCommand(userCommand, receivedObject.getClient());
            }

            Response response = new Response(responce.getAnswer());
            ByteBuffer bufferToWrite = toBuffer(response);
            key.attach(bufferToWrite);
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            System.out.println("Error during processing: " + e);
        }
    }

    private void handleWrite(SelectionKey key) {
        new Thread(() -> {
            try {
                SocketChannel client = (SocketChannel) key.channel();
                ByteBuffer buffer = (ByteBuffer) key.attachment();
                buffer.flip();
                writeLock.lock();
                try {
                    client.write(buffer);
                } finally {
                    writeLock.unlock();
                }
                client.close();
            } catch (IOException e) {
                System.out.println("Error during write: " + e);
            }
        }).start();
    }

    /*
    Converting object into ByteBuffer
    @param object - serializable object
    @return return object converted in ByteBuffer
    @throws IOException input output exception
     */
    public static ByteBuffer toBuffer(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.flush();
        byte[] data = baos.toByteArray();
        int length = data.length + 4;
        ByteBuffer writeBuffer = ByteBuffer.allocate(length);
        writeBuffer.putInt(data.length);
        writeBuffer.put(data);
        return writeBuffer;
    }

    /*
    Converting object into ByteBuffer
    @param byteBuffer
    @return return deserialized object
    @throws IOException, ClassNotFoundException
     */
    public static Serializable fromByteBuffer(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
        Serializable response = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        bais.close();
        return response;
    }
}