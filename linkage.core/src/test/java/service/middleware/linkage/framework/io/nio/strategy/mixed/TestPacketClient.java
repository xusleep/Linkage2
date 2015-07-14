package service.middleware.linkage.framework.io.nio.strategy.mixed;

import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.MessageEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketDataType;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.writer.*;
import test.framework.concurrence.condition.MainConcurrentThread;
import test.framework.concurrence.condition.job.AbstractJob;
import test.framework.concurrence.condition.job.JobInterface;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Smile on 2015/7/3.
 */
public class TestPacketClient
{
    public static AtomicInteger count = new AtomicInteger(0);
    static class ConcurrentJob extends AbstractJob{
        private final WriterInterface writer;
        private final LinkageSocketChannel linkageSocketChannel;
        public ConcurrentJob(LinkageSocketChannel linkageSocketChannel){
            MessageDataWriter messageWriter = new MessageDataWriter(null);
            FileDataWriter fileWriter = new FileDataWriter(null);
            DataTypeWriter dataWriter = new DataTypeWriter(messageWriter, fileWriter);
            writer = new PacketWriter(dataWriter);
            this.linkageSocketChannel = linkageSocketChannel;
        }

        @Override
        public void doBeforeJob() {

        }

        @Override
        public void doConcurrentJob() {
            try {
                for(int i = 0; i < 100; i++) {
                    byte[] data = ("testsdfsdfdsfsdfsdfdsfdsfsdfsqweqw" + count.incrementAndGet()).getBytes("UTF-8");
                    MessageEntity contentEntity = new MessageEntity();
                    contentEntity.setData(data);
                    contentEntity.setLength(data.length);
                    PacketEntity packetEntity = new PacketEntity();
                    packetEntity.setContentEntity(contentEntity);
                    packetEntity.setPacketDataType(PacketDataType.MESSAGE);
                    packetEntity.setLength(contentEntity.getLength() + 1 + 8);
                    writer.write(linkageSocketChannel, packetEntity);
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void doAfterJob() {

        }
    }

    public static void testConcurrent(){
        MessageDataWriter messageWriter = new MessageDataWriter(null);
        FileDataWriter fileWriter = new FileDataWriter(null);
        DataTypeWriter dataWriter = new DataTypeWriter(messageWriter, fileWriter);
        WriterInterface writer = new PacketWriter(dataWriter);
        try {
            SocketChannel channel = SocketChannel.open();
            // connect
            channel.connect(new InetSocketAddress("127.0.0.1", 8881));
            if(channel.isConnectionPending()){
                channel.finishConnect();
            }
            LinkageSocketChannel linkageSocketChannel = new LinkageSocketChannel(channel);
            ConcurrentJob job1 = new ConcurrentJob(linkageSocketChannel);
            job1.setThreadCount(2000);
            List<JobInterface> jobList = new LinkedList<JobInterface>();
            jobList.add(job1);
            MainConcurrentThread mct1 = new MainConcurrentThread(jobList);
            mct1.start();
            mct1.join();
            System.out.println("count = " + count.get());
            channel.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
//            if(channel!= null) {
//                try {
//                    channel.close();
//                }
//                catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
        }
    }

    private static void testNormanal(){
        SocketChannel channel = null;
        try {
            String s = "$#####$1341*#####*<request><requestid>1</requestid><serviceName>serviceCenter</serviceName><methodName>register</methodName><version>null</version><group>test</group><list><arg>&lt;serviceInformationList&gt;&lt;serviceInformation&gt;&lt;address&gt;localhost&lt;/address&gt;&lt;port&gt;5003&lt;/port&gt;&lt;serviceName&gt;HEART_BEAT_SERVICE&lt;/serviceName&gt;&lt;serviceMethod&gt;receive&lt;/serviceMethod&gt;&lt;serviceVersion&gt;version&lt;/serviceVersion&gt;&lt;/serviceInformation&gt;&lt;serviceInformation&gt;&lt;address&gt;localhost&lt;/address&gt;&lt;port&gt;5003&lt;/port&gt;&lt;serviceName&gt;calculator&lt;/serviceName&gt;&lt;serviceMethod&gt;add&lt;/serviceMethod&gt;&lt;serviceVersion&gt;version&lt;/serviceVersion&gt;&lt;/serviceInformation&gt;&lt;serviceInformation&gt;&lt;address&gt;localhost&lt;/address&gt;&lt;port&gt;5003&lt;/port&gt;&lt;serviceName&gt;calculator&lt;/serviceName&gt;&lt;serviceMethod&gt;add1&lt;/serviceMethod&gt;&lt;serviceVersion&gt;version&lt;/serviceVersion&gt;&lt;/serviceInformation&gt;&lt;serviceInformation&gt;&lt;address&gt;localhost&lt;/address&gt;&lt;port&gt;5003&lt;/port&gt;&lt;serviceName&gt;storage&lt;/serviceName&gt;&lt;serviceMethod&gt;getUploadFileID&lt;/serviceMethod&gt;&lt;serviceVersion&gt;version&lt;/serviceVersion&gt;&lt;/serviceInformation&gt;&lt;/serviceInformationList&gt;</arg></list></request>";
            channel = SocketChannel.open();
            // connect
            channel.connect(new InetSocketAddress("127.0.0.1", 5002));
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            byte[] data = s.getBytes("utf-8");
            ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
            byteBuffer.put(data, 0, data.length);
            byteBuffer.flip();
            System.out.println("length:" + data.length);
            while (byteBuffer.hasRemaining())
                channel.write(byteBuffer);
            channel.shutdownOutput();
            channel.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            try {
                channel.close();
            }
            catch (Exception ex){

            }
        }
    }

    public static void main(String[] args){
        testNormanal();
    }
}
