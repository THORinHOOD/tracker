package com.lada.tracker.services;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        long pid = ProcessHandle.current().pid();
        try {
            Runtime.getRuntime().exec("echo " + pid + " > pid");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}