/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine.serviceimpl;

import distsys.smartmarine.LocationRequest;
import distsys.smartmarine.MarineLifeServiceGrpc;
import distsys.smartmarine.Species;
import distsys.smartmarine.SpeciesRequest;
import distsys.smartmarine.SpeciesStatus;
import io.grpc.stub.StreamObserver;


/**
 *
 * @author ilian
 */
public class MarineLifeServiceImpl extends MarineLifeServiceGrpc.MarineLifeServiceImplBase {
    
    @Override
    public void getSpeciesStatus(SpeciesRequest request, StreamObserver<SpeciesStatus> responseObserver) {

        String speciesName = request.getName();
        SpeciesStatus response;
        if (speciesName.equalsIgnoreCase("Grey Seal")) {
            response = SpeciesStatus.newBuilder()
                    .setName("Grey Seal")
                    .setPopulationTrend("STABLE")
                    .setLastSeenDaysAgo(2)
                    .build();
         } else if (speciesName.equalsIgnoreCase("Common Dolphin")) {
            response = SpeciesStatus.newBuilder()
                    .setName("Common Dolphin")
                    .setPopulationTrend("INCREASING")
                    .setLastSeenDaysAgo(5)
                    .build();
        } else if (speciesName.equalsIgnoreCase("Leatherback Turtle")) {
            response = SpeciesStatus.newBuilder()
                    .setName("Leatherback Turtle")
                    .setPopulationTrend("DECLINING")
                    .setLastSeenDaysAgo(12)
                    .build();
         } else {
            response = SpeciesStatus.newBuilder()
                    .setName(speciesName)
                    .setPopulationTrend("UNKNOWN")
                    .setLastSeenDaysAgo(-1)
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
public void listObservedSpecies(LocationRequest request, StreamObserver<Species> responseObserver) {

    int locationId = request.getLocationId();

    if (locationId == 1) {
        responseObserver.onNext(Species.newBuilder()
                .setName("Grey Seal")
                .setCategory("RESIDENT")
                .setRiskStatus("LOW")
                .build());

        responseObserver.onNext(Species.newBuilder()
                .setName("Common Dolphin")
                .setCategory("MIGRATORY")
                .setRiskStatus("MEDIUM")
                .build());
    } else if (locationId == 2) {
        responseObserver.onNext(Species.newBuilder()
                .setName("Leatherback Turtle")
                .setCategory("MIGRATORY")
                .setRiskStatus("HIGH")
                .build());
    } else {
        responseObserver.onNext(Species.newBuilder()
                .setName("Unknown Species")
                .setCategory("UNKNOWN")
                .setRiskStatus("UNKNOWN")
                .build());
    }

    responseObserver.onCompleted();
  }
}
