package customer;


import amqp.RabbitMQMessageProducer;
import clients.fraud.FraudCheckResponse;
import clients.fraud.FraudClient;
import clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepo customerRepo, FraudClient fraudClient, RabbitMQMessageProducer rabbitMQMessageProducer) {
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        var customer = Customer.builder()
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

        var notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to DH...",
                        customer.getFirstName())
        );

        //This was de direct way to notify . Now will be sent to Queue
        /*notificationClient.sendNotification(
                notificationRequest
        );*/
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "rabbitmq.routing-keys.internal"
        );

    }
}
