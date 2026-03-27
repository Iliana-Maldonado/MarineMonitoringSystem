/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine.serviceimpl;

import distsys.smartmarine.Alert;
import distsys.smartmarine.AlertMessage;
import distsys.smartmarine.AlertRequest;
import distsys.smartmarine.AlertResponse;
import distsys.smartmarine.AlertServiceGrpc;
import distsys.smartmarine.AlertSubscription;
import distsys.smartmarine.Empty;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author ilian
 */
public class AlertServiceImpl extends AlertServiceGrpc.AlertServiceImplBase{
    @Override
    public void createAlert(AlertRequest request, StreamObserver<AlertResponse> responseObserver) {

        String generatedId = "A" + System.currentTimeMillis();

        AlertResponse response = AlertResponse.newBuilder()
                .setAlertId(generatedId)
                .setCreated(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }   
    @Override
    public void getActiveAlerts(Empty request, StreamObserver<Alert> responseObserver) {

        responseObserver.onNext(Alert.newBuilder()
                .setAlertId("A001")
                .setAlertType("POLLUTION")
                .setMessage("Microplastic level is above safe threshold.")
                .setSeverity(3)
                .build());

        responseObserver.onNext(Alert.newBuilder()
                .setAlertId("A002")
                .setAlertType("SPECIES_RISK")
                .setMessage("Leatherback Turtle population is declining.")
                .setSeverity(4)
                .build());
        
        responseObserver.onCompleted();
    }
    
    @Override
public StreamObserver<AlertSubscription> liveAlertChannel(StreamObserver<AlertMessage> responseObserver) {

    return new StreamObserver<AlertSubscription>() {

        @Override
        public void onNext(AlertSubscription request) {

            String msg = "Subscribed to alerts";

            AlertMessage response = AlertMessage.newBuilder()
                    .setText(msg)
                    .setSource("AlertService")
                    .build();

            responseObserver.onNext(response);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("Error in liveAlertChannel: " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            responseObserver.onCompleted();
        }
    };
  }
}

