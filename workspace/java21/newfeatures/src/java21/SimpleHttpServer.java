package java21;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

// like JDBC
class SimpleWork {

    AtomicLong id = new AtomicLong();
    ReentrantLock lock = new ReentrantLock();

    public String doJob() {
        String response = null;
        lock.lock();
        try {
            Thread.sleep(100);
            response = "Ping_" + id.incrementAndGet();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
        return response;
    }
}

 class SimpleDelayedHandler implements HttpHandler {

    private final List<SimpleWork> workers = new ArrayList<>();
    private final int workersCount = 50;
    private final boolean withLock;
    AtomicLong id = new AtomicLong();

    public SimpleDelayedHandler(boolean withLock) {
        this.withLock = withLock;
        if (withLock) {
            for (int i = 0; i < workersCount; i++) {
                workers.add(new SimpleWork());
            }
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = null;
        if (withLock) {
            response = workers.get((int) (id.incrementAndGet() % workersCount)).doJob();
        } else {
            new BigInteger(500, 10, new Random());
            response = "Ping_" + id.incrementAndGet();
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer
                .create(new InetSocketAddress(8081), 0);
        boolean withLock = true;
        boolean virtual = false;
        httpServer.createContext("/example", new SimpleDelayedHandler(withLock));
        if (virtual) {
            httpServer.setExecutor(
                    Executors.newVirtualThreadPerTaskExecutor());
        } else {
            httpServer.setExecutor(
                    Executors.newFixedThreadPool(200));
        }
        httpServer.start();
    }
}
