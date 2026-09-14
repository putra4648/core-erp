package id.putra.core_erp.config;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vaadin.quarkus.annotation.VaadinServiceEnabled;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class ExecutorConfiguration {

    @Produces
    @Singleton
    @VaadinServiceEnabled
    public Executor vaadinTaskExecutor() {
        ThreadPoolExecutor customExecutor = new ThreadPoolExecutor(
                16, // Core pool size
                32, // Maximum pool size
                120, // Keep-alive time
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r, "CustomVaadinExecutor-" + r.hashCode());
                    thread.setDaemon(true);
                    return thread;
                });
        customExecutor.allowCoreThreadTimeOut(true);
        return customExecutor;
    }
}