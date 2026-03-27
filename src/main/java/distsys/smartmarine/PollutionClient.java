package distsys.smartmarine;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PollutionClient {

    public static void main(String[] args) throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            WaterQualityServiceGrpc.WaterQualityServiceStub stub =
                    WaterQualityServiceGrpc.newStub(channel);

            CountDownLatch latch = new CountDownLatch(1);

            StreamObserver<PollutionSummary> responseObserver = new StreamObserver<PollutionSummary>() {
                @Override
                public void onNext(PollutionSummary summary) {
                    System.out.println("Total readings: " + summary.getTotalReadings());
                    System.out.println("Average microplastic index: " + summary.getAverageMicroplasticIndex());
                    System.out.println("Average chemical level: " + summary.getAverageChemicalLevel());
                    System.out.println("Pollution level: " + summary.getLevel());
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Client streaming completed.");
                    latch.countDown();
                }
            };

            StreamObserver<PollutionReading> requestObserver =
                    stub.sendPollutionReadings(responseObserver);

            requestObserver.onNext(PollutionReading.newBuilder()
                    .setLocationId(1)
                    .setMicroplasticIndex(3.5)
                    .setChemicalLevel(2.0)
                    .build());

            requestObserver.onNext(PollutionReading.newBuilder()
                    .setLocationId(1)
                    .setMicroplasticIndex(5.5)
                    .setChemicalLevel(4.5)
                    .build());

            requestObserver.onNext(PollutionReading.newBuilder()
                    .setLocationId(1)
                    .setMicroplasticIndex(8.0)
                    .setChemicalLevel(7.5)
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