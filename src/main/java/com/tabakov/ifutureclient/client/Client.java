package com.tabakov.ifutureclient.client;

import com.squareup.okhttp.*;
import com.tabakov.ifutureclient.config.ClientConfig;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;

@Component
public class Client {

    private final ClientConfig clientConfig;
    private final OkHttpClient client;
    public static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");
    ExecutorService rExecutorService;
    ExecutorService wExecutorService;


    public Client(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.client = new OkHttpClient();
        rExecutorService =
                new ThreadPoolExecutor(clientConfig.getRCount(), clientConfig.getRCount(), 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        wExecutorService =
                new ThreadPoolExecutor(clientConfig.getWCount(), clientConfig.getWCount(), 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());
    }

    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void startSendingRequests() {
        while (true) {
            try {
                wExecutorService.submit(writeTask());
                rExecutorService.submit(readTask());
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(10000);
            }
        }

    }


    private String callGetAmount(int id) throws IOException, IllegalStateException {
        Request request = new Request.Builder()
                .url(clientConfig.getUrl() + "?id=" + id)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String callAddAmount(int id, long amount) throws IOException, IllegalStateException {
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, addAmountJson(id, amount));
        Request request = new Request.Builder()
                .url(clientConfig.getUrl() + "/add")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private Callable<String> readTask() {
        return () -> {
            try {
                var id = ThreadLocalRandom.current().nextInt(clientConfig.getIdLowBound(),
                        clientConfig.getIdHighBound() + 1);

                return callGetAmount(id);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                return "";
            }
        };
    }

    private Callable<String> writeTask() {
        return () -> {
            try {
                var id = ThreadLocalRandom.current().nextInt(clientConfig.getIdLowBound(),
                        clientConfig.getIdHighBound() + 1);
                var amount = ThreadLocalRandom.current().nextLong(clientConfig.getAmountLowBound(),
                        clientConfig.getAmountHighBound() + 1);

                return callAddAmount(id, amount);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                return "";
            }
        };
    }

    private String addAmountJson(int id, long amount) {
        return "{\"id\":"+ id + ", \"amount\":" + amount + "}";
    }
}
