package secret.repositories;

import org.springframework.data.repository.CrudRepository;
import secret.struct.Message;

public interface MessageRepository extends CrudRepository<Message,Long> {
}
