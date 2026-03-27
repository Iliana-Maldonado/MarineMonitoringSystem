/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distsys.smartmarine;

/**
 *
 * @author ilian
 */

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AlertClient {

    public static void main(String[] args) throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            AlertServiceGrpc.AlertServiceStub stub =
                    AlertServiceGrpc.newStub(channel);

            CountDownLatch latch = new CountDownLatch(1);

            StreamObserver<AlertMessage> responseObserver = new StreamObserver<AlertMessage>() {
                @Override
                public void onNext(AlertMessage message) {
                    System.out.println("Message: " + message.getText());
                    System.out.println("Source: " + message.getSource());
                    System.out.println("-------------------------");
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Bidirectional streaming completed.");
                    latch.countDown();
                }
            };

            StreamObserver<AlertSubscription> requestObserver =
                    stub.liveAlertChannel(responseObserver);

            requestObserver.onNext(AlertSubscription.newBuilder()
                    .setClientName("Iliana")
                    .build());

            requestObserver.onNext(AlertSubscription.newBuilder()
                    .setClientName("Marine Dashboard")
                    .build());

            requestObserver.onNext(AlertSubscription.newBuilder()
                    .setClientName("Research Monitor")
                    .build());

            requestObserver.onCompleted();

            latch.await(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }
    }
}
