package customer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepo customerRepo, RestTemplate restTemplate) {
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        customerRepo.saveAndFlush(customer);//We need to >flush (persist) so we can get an Id for the client on the go
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://fraud/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("It's a fruadster");//Not considering null values at the moment
        }
    }
}
