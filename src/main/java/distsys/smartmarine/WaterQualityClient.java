/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 *
 * @author ilian
 */
public class WaterQualityClient {
    public static void main(String[] args) {
        
        //Create the channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        //Stub
        WaterQualityServiceGrpc.WaterQualityServiceBlockingStub stub =
                WaterQualityServiceGrpc.newBlockingStub(channel);
        
        //Create request
        Empty request = Empty.newBuilder().build();
        
        //Method
        Temperature response = stub.getWaterTemperature(request);

        System.out.println("Water Temperature: " + response.getValue() + " °C");

        //Close channel
        channel.shutdown();
    }
}
