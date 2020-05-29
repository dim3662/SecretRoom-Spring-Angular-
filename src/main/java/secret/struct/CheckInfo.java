package secret.struct;

import lombok.Data;

@Data
public class CheckInfo {
    private String password;
    private Boolean check=false;
    private String message;
}
