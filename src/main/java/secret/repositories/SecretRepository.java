package secret.repositories;

import org.springframework.data.repository.CrudRepository;
import secret.struct.Secret;

public interface SecretRepository extends CrudRepository<Secret, Long> {
     Secret getSecretById(Long id);
     Secret getSecretByHash(String hash);
     void deleteSecretByHash(String hash);
}
