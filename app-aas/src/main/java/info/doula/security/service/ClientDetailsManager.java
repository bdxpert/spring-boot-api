package info.doula.security.service;

import org.springframework.security.oauth2.provider.ClientDetails;

/**
 * Created by hossaindoula on 6/4/2017.
 */
public interface ClientDetailsManager {

    ClientDetails get(String id);
}
