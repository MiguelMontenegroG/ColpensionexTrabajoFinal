package com.unquindisoft.colpensionex.scheduler;

import com.unquindisoft.colpensionex.service.SolicitudService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerConfig {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final SolicitudService solicitudService;

    public SchedulerConfig() {
        this.solicitudService = new SolicitudService();
    }

    public void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Ejecutando proceso de traslado de solicitudes");
            solicitudService.trasladarSolicitudes();
        }, 0, 1, TimeUnit.HOURS);
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }
}
