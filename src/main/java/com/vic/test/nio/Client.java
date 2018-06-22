package com.vic.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by vic
 * Create time : 2018/6/22 14:24
 */
public class Client {
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        new Client().start();

    }

    public void start() throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress("localhost", 8001));
        Selector selector = Selector.open();
        sc.register(selector, SelectionKey.OP_CONNECT);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            System.out.println("keys=" + keys.size());
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isConnectable()) {
                    sc.finishConnect();
                    sc.register(selector, SelectionKey.OP_WRITE);
                    System.out.println("server connected...");
                    break;
                } else if (key.isWritable()) {
                    System.out.println("please input message:");
                    String message = scanner.nextLine();

                    writeBuffer.clear();
                    writeBuffer.put(message.getBytes());

                    writeBuffer.flip();
                    sc.write(writeBuffer);

                    sc.register(selector, SelectionKey.OP_READ);
                    sc.register(selector, SelectionKey.OP_WRITE);
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    System.out.println("receive message:");
                    SocketChannel client = (SocketChannel) key.channel();
                    readBuffer.clear();
                    int num = client.read(readBuffer);
                    System.out.println(new String(readBuffer.array(), 0, num));
                    sc.register(selector, SelectionKey.OP_WRITE);
                }
            }
        }
    }
}
