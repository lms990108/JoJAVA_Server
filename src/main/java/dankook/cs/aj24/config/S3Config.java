package dankook.cs.aj24.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 스프링에서 설정을 정의하는 어노테이션
public class S3Config {

    // Value 어노테이션 : 설정파일에서 해당 값 주입
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    // Amazon S3 클라이언트 인스턴스를 생성하기 위한 Bean 정의
    @Bean
    public AmazonS3 amazonS3() {
        // AWS 인증을 위해 액세스 키와 시크릿 키를 사용하여 AWSCredentials 객체를 생성
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        // AmazonS3ClientBuilder를 사용하여 Amazon S3 클라이언트를 생성
        // 이때, 위에서 생성한 credentials와 설정된 리전 정보를 사용
        return AmazonS3ClientBuilder
                .standard() // 기본 클라이언트 구성 사용
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) // 생성된 인증 정보 제공
                .withRegion(region) // 지정된 리전 설정
                .build(); // AmazonS3 클라이언트 인스턴스 생성
    }
}
