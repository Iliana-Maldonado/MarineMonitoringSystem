/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine.serviceimpl;

import distsys.smartmarine.Empty;
import distsys.smartmarine.PHReading;
import distsys.smartmarine.PollutionReading;
import distsys.smartmarine.PollutionSummary;
import distsys.smartmarine.Temperature;
import distsys.smartmarine.WaterQualityServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author ilian
 */
public class WaterQualityServiceImpl extends WaterQualityServiceGrpc.WaterQualityServiceImplBase {
     @Override
    public void getWaterTemperature(Empty request, StreamObserver<Temperature> responseObserver) {
        Temperature response = Temperature.newBuilder()
                .setValue(14.5)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getWaterPH(Empty request, StreamObserver<PHReading> responseObserver) {
        PHReading response = PHReading.newBuilder()
                .setValue(7.8)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
     @Override
    public StreamObserver<PollutionReading> sendPollutionReadings(StreamObserver<PollutionSummary> responseObserver) {

        return new StreamObserver<PollutionReading>() {

            int count = 0;
            double totalMicroplastic = 0;
            double totalChemical = 0;

            @Override
            public void onNext(PollutionReading reading) {
                count++;
                totalMicroplastic += reading.getMicroplasticIndex();
                totalChemical += reading.getChemicalLevel();
            }
             @Override
            public void onError(Throwable t) {
                System.err.println("Error receiving pollution readings: " + t.getMessage());
            }
             @Override
            public void onCompleted() {
                double avgMicroplastic = (count == 0) ? 0 : totalMicroplastic / count;
                double avgChemical = (count == 0) ? 0 : totalChemical / count;

                String level;
                if (avgMicroplastic > 7 || avgChemical > 7) {
                    level = "HIGH";
                } else if (avgMicroplastic > 4 || avgChemical > 4) {
                    level = "MEDIUM";
                } else {
                    level = "LOW";
                }
                PollutionSummary summary = PollutionSummary.newBuilder()
                        .setTotalReadings(count)
                        .setAverageMicroplasticIndex(avgMicroplastic)
                        .setAverageChemicalLevel(avgChemical)
                        .setLevel(level)
                        .build();

                responseObserver.onNext(summary);
                responseObserver.onCompleted();
            }
        };
    }
}
                
