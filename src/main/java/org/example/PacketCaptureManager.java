package org.example;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PacketCaptureManager {
    private static final int SNAPLEN = 65536; // [bytes]
    private static final int TIMEOUT = 10; // [ms]

    private final PcapNetworkInterface nif;
    private final String targetIp;
    private final JTextArea outputArea;
    private final ExecutorService executorService;
    private PcapHandle handle;
    private Future<?> future;

    public PacketCaptureManager(PcapNetworkInterface nif, String targetIp, JTextArea outputArea) {
        this.nif = nif;
        this.targetIp = targetIp;
        this.outputArea = outputArea;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void startCapture() {
        future = executorService.submit(() -> {
            try {
                handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, TIMEOUT);

                if (!targetIp.isEmpty()) {
                    String filter = String.format("ip and tcp and dst host %s", targetIp);
                    outputArea.append("Setting filter: " + filter + "\n");
                    handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
                } else {
                    outputArea.append("No filter set, capturing all traffic\n");
                }

                PacketListener listener = new PacketParser(outputArea);
                outputArea.append("Start capturing on interface: " + nif.getName() + "\n");
                handle.loop(-1, listener);
            } catch (PcapNativeException | NotOpenException | InterruptedException e) {
                if (!(e instanceof InterruptedException)) {
                    e.printStackTrace();
                }
            } finally {
                if (handle != null && handle.isOpen()) {
                    handle.close();
                }
            }
        });
    }

    public void stopCapture() {
        if (handle != null && handle.isOpen()) {
            try {
                handle.breakLoop();
            } catch (NotOpenException e) {
                throw new RuntimeException(e);
            }
        }
        if (future != null) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
