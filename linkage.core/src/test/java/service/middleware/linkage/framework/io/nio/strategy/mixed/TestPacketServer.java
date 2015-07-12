package service.middleware.linkage.framework.io.nio.strategy.mixed;

import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.MessageEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketDataType;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.reader.DataTypeReader;
import service.middleware.linkage.framework.io.nio.strategy.mixed.reader.FileDataReader;
import service.middleware.linkage.framework.io.nio.strategy.mixed.reader.MessageDataReader;
import service.middleware.linkage.framework.io.nio.strategy.mixed.reader.PacketReader;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Smile on 2015/7/4.
 */
public class TestPacketServer {

    public static void testReadByte() {
        // TODO Auto-generated method stub
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8881);
            // bind the address and listen to the port
            serverSocketChannel.bind(address);
            while (true) {
                final SocketChannel objSocket = serverSocketChannel.accept();
                // 读入线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                ByteBuffer bb = ByteBuffer.allocate(1);
                                int read = objSocket.read(bb);
                                byte[] readBytes = bb.array();
                                if (read > 0) {
                                    System.out.println("server side read " + readBytes[0]);
                                }
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                if (objSocket != null)
                                    objSocket.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void testReadPacket() {
        MessageDataReader messageReader = new MessageDataReader(null);
        FileDataReader fileReader = new FileDataReader(null);
        DataTypeReader dataReader = new DataTypeReader(messageReader, fileReader);
        final PacketReader reader = new PacketReader(dataReader);
        // TODO Auto-generated method stub
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8881);
            // bind the address and listen to the port
            serverSocketChannel.bind(address);
            while (true) {
                final SocketChannel objSocket = serverSocketChannel.accept();
                final LinkageSocketChannel linkageSocketChannel = new LinkageSocketChannel(objSocket);
                // 读入线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                if(linkageSocketChannel.isOpen()) {
                                    PacketEntity packetEntity = new PacketEntity();
                                    reader.read(linkageSocketChannel, packetEntity);
                                    if (packetEntity.getPacketDataType() == PacketDataType.MESSAGE) {
                                        System.out.println("receive message type : " + packetEntity.getPacketDataType().name());
                                        MessageEntity messageEntity = (MessageEntity) packetEntity.getContentEntity();
                                        System.out.println("receive message: " + new String(messageEntity.getData(), "UTF-8"));
                                    }
                                }
                                else{
                                    System.out.println("channel is closed, exit thread.");
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                if (objSocket != null)
                                    objSocket.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        testReadPacket();
    }
}
