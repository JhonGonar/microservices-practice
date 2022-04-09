package customer;


import clients.fraud.FraudCheckResponse;
import clients.fraud.FraudClient;
import clients.notification.NotificationClient;
import clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepo customerRepo, FraudClient fraudClient, NotificationClient notificationClient) {
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        customerRepo.saveAndFlush(customer);//We need to >flush (persist) so we can get an Id for the client on the go
        /*FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://fraud/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );*/

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("It's a fruadster");//Not considering null values at the moment
        }

        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to DH...",
                                customer.getFirstName())
                )
        );

    }
}
