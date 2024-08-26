package org.example;

import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.util.ByteArrays;

import javax.swing.*;
import java.net.Inet4Address;

public class PacketParser implements PacketListener {
    private final JTextArea outputArea;

    public PacketParser(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    @Override
    public void gotPacket(Packet packet) {
        IpV4Packet ipPacket = packet.get(IpV4Packet.class);
        if (ipPacket != null) {
            Inet4Address srcAddr = ipPacket.getHeader().getSrcAddr();
            Inet4Address dstAddr = ipPacket.getHeader().getDstAddr();
            outputArea.append("IP Packet: Src Addr: " + srcAddr + " Dst Addr: " + dstAddr + "\n");

            if (ipPacket.getHeader().getProtocol().equals(IpNumber.TCP)) {
                TcpPacket tcpPacket = packet.get(TcpPacket.class);
                if (tcpPacket != null) {
                    int srcPort = tcpPacket.getHeader().getSrcPort().valueAsInt();
                    int dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();
                    outputArea.append("TCP Packet: Src Port: " + srcPort + " Dst Port: " + dstPort + "\n");

                    if (tcpPacket.getPayload() != null) {
                        byte[] payload = tcpPacket.getPayload().getRawData();
                        if (payload != null) {
                            outputArea.append("Payload: " + ByteArrays.toHexString(payload, " ") + "\n");
                        } else {
                            outputArea.append("Payload: null\n");
                        }
                    } else {
                        outputArea.append("TCP Packet has no payload.\n");
                    }
                }
            }
        }
    }
}
