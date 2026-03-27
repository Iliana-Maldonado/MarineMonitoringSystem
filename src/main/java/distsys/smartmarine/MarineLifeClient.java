/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Iterator;

/**
 *
 * @author ilian
 */
public class MarineLifeClient {
    
    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        
        try {
            MarineLifeServiceGrpc.MarineLifeServiceBlockingStub stub =
                    MarineLifeServiceGrpc.newBlockingStub(channel);

            LocationRequest request = LocationRequest.newBuilder()
                    .setLocationId(1)
                    .build();

            Iterator<Species> responses = stub.listObservedSpecies(request);

            while (responses.hasNext()) {
                Species species = responses.next();

                System.out.println("Species: " + species.getName());
                System.out.println("Category: " + species.getCategory());
                System.out.println("Risk: " + species.getRiskStatus());
                System.out.println("-------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }
    }   
}