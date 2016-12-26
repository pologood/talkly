package com.message;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.message.chat.listener.Register;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TalklyApplicationTests {

    public static AtomicLong successCount = new AtomicLong(0);
    public static AtomicLong counter = new AtomicLong(5);
    public static AtomicLong connectionCount = new AtomicLong(5);
    public static Random r = new Random();
    public static IO.Options opts;
    private static int aa = 5000;

    public static void createSocket(final int id) {

        try {
            final Socket sck = IO.socket("http://localhost:9092", opts);

            sck.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                    successCount.incrementAndGet();
                    for (int i = 0; i < aa; i++) {
                        sck.emit("send_test", id * 1000 + i);
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (successCount.get() <= counter.get()) {
                        // Adding a little rate limiting helps, but we're not hitting this *that* hard
                        /*
                        try {
                            Thread.sleep(r.nextInt(300 + 50));
                        } catch ( java.lang.InterruptedException iex ) {
                        }
                        */
                        sck.connect();
                    }
                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Error! " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connect error! " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connect timeout! " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Reconnect " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Reconnect ERROR! " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Reconnect FAILED! " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Reconnect Attempt! " + new ArrayList(Arrays.asList(args)));
                }
            }).on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Reconnecting! " + new ArrayList(Arrays.asList(args)));
                }
            }).on("test", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    int s = Integer.parseInt(objects[0].toString());
//                    System.out.println("Ack! " + Math.ceil(s / 1000) + "--" + s % 1000);
                    System.out.println("Ack! " + s);
                    if (objects[0].toString().endsWith(String.valueOf(aa - 1))) {
                        successCount.incrementAndGet();
                        sck.close();
                    }
                }
            });
            sck.connect();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void contextLoads() throws InterruptedException {
        opts = new IO.Options();
        opts.forceNew = true;

        connectionCount.set(1);
        counter.set(1);
        opts.transports = new String[]{WebSocket.NAME};

        System.out.println("Going to create " + counter + " connections to the SocketIO server");

        long start = System.currentTimeMillis();

        for (int ii = 0; ii < connectionCount.get(); ii++)
            createSocket(ii);

        System.out.println("Done creating connections: thread count =" + java.lang.Thread.activeCount());

        while (successCount.get() < counter.get()) {
            System.out.println(new Date() + " " + successCount.get() + " of " + counter + " completed, thread count = " + java.lang.Thread.activeCount());
            Thread.sleep(10000);
        }
        System.out.println(new Date() + " " + successCount.get() + " of " + counter + " completed");
        System.out.println("Done: elapsed=" + (System.currentTimeMillis() - start) + "ms");
    }

}
