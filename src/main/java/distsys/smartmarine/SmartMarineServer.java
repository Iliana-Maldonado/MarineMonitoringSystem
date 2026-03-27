/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

import distsys.smartmarine.serviceimpl.WaterQualityServiceImpl;
import distsys.smartmarine.serviceimpl.MarineLifeServiceImpl;
import distsys.smartmarine.serviceimpl.AlertServiceImpl;

/**
 *
 * @author ilian
 */
public class SmartMarineServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(50051)
                .addService(new WaterQualityServiceImpl())
                .addService(new MarineLifeServiceImpl())
                .addService(new AlertServiceImpl())
                .build();

        server.start();
        System.out.println("Server started on port 50051");

        server.awaitTermination();
    }
}
