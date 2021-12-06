package layer;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class CommonCode {

    public static void doSomethingOnLayer(final LambdaLogger logger, final String s) {
        logger.log("Doing something on layer" + s);
        S3Client s3Client = S3Client.builder().build();

        logger.log("list buckets: ");
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
        listBucketsResponse.buckets().stream().forEach(x -> logger.log(x.name()));
    }
}
