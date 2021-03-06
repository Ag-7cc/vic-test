package com.vic.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by vic
 * Create time : 2018/6/22 14:22
 */
public class Server {
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            // 打开服务器套接字通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 服务器配置为非阻塞
            ssc.configureBlocking(false);
            // 进行服务器绑定
            ssc.bind(new InetSocketAddress("localhost", 8001));

            // 通过open()方法找到Selector
            Selector selector = Selector.open();
            // 注册到selector，等待链接
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(selector, key);
                    }
                    if (key.isReadable()) {
                        read(selector, key);
                    }
                    if (key.isWritable()) {
                        System.out.print("please input message:");
                        String message = scanner.nextLine();
                        write(selector, key, message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Selector selector, SelectionKey key, String message) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.register(selector, SelectionKey.OP_READ);
        if (Objects.nonNull(message)) {
            System.out.println("write:" + message);
            sendBuffer.clear();
            sendBuffer.put(message.getBytes());
            sendBuffer.flip();
            channel.write(sendBuffer);
        }
    }

    private void read(Selector selector, SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        this.readBuffer.clear();
        int numRead;
        try {
            numRead = channel.read(this.readBuffer);
        } catch (IOException e) {
            key.cancel();
            channel.close();
            return;
        }
        String message = new String(readBuffer.array(), 0, numRead);
        System.out.println("receive message:" + message);
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = ssc.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        System.out.println("a new client connected " + clientChannel.getRemoteAddress());
    }
}
