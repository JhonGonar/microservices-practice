package notification;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Notification {
    @Id
    @SequenceGenerator(
            name = "notification_id_sequence",
            sequenceName = "notification_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_sequence"
    )
    private Integer notificationId;
    private Integer toCustomerId;
    private String toCustomerEmail;
    private String sender;
    private String message;
    private LocalDateTime sentAt;

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", toCustomerId=" + toCustomerId +
                ", toCustomerEmail='" + toCustomerEmail + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(notificationId, that.notificationId) && Objects.equals(toCustomerId, that.toCustomerId) && Objects.equals(toCustomerEmail, that.toCustomerEmail) && Objects.equals(sender, that.sender) && Objects.equals(message, that.message) && Objects.equals(sentAt, that.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId, toCustomerId, toCustomerEmail, sender, message, sentAt);
    }
}
