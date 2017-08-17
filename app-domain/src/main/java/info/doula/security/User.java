package info.doula.security;

import lombok.Data;

import javax.persistence.Entity;

/**
 * Created by doula_itc on 2017-08-16.
 */

@Data
@Entity
public class User {

    private Integer id;
    private String username;
    private String password;
}
