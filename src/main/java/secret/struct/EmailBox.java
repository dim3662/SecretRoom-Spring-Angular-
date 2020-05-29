package secret.struct;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class EmailBox {
    private String url; // содержимое письма
    private String email;// кому пиьсмо отправится
}
