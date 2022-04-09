package fraud;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public record FraudCheckService(FraudCheckHistoryRepo fraudCheckHistoryRepo) {

    public boolean isFraudulentCustomer(Integer customerId) {
        fraudCheckHistoryRepo.save(
                FraudCheckHistory.builder()
                        .customerId(customerId)
                        .isFraudster(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        return false;
    }
}