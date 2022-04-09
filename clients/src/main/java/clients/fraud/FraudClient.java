package clients.fraud;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        value = "fraud",
        path = "api/v1/fraud-check"
)
public interface FraudClient {
}
