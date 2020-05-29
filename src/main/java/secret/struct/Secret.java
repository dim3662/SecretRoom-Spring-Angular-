package secret.struct;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    @OneToOne(targetEntity = Message.class)
    private Message message;
    private String lifetime;
    private Date createdAt;
    private boolean show = false;
    private boolean forPrivateComp = false;
    private String hash;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
