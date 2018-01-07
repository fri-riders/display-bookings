package com.fri.rso.fririders.displaybookings.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Optional;


@SessionScoped
public class AuthBean implements Serializable{

    private Logger logger = LogManager.getLogger(AuthBean.class.getName());

    private static Client client = ClientBuilder.newClient();

    private String token = null;

    @Inject
    @DiscoverService(value="users", version = "1.0.x", environment = "dev")
    transient private Optional<String> usersUrl;

    public String getAuthToken(){
        if (this.token != null) {
            logger.info("Token already exists.");
            return token;
        }
        else {
            String email = "user1@example.com";
            String passwd = "123456";
            if (this.usersUrl.isPresent()) {
                try {
                    logger.info("Calling users service ...");
                    String authUrl = this.usersUrl.get() + "/v1/users/login?email=" + email + "&password=" + passwd;
                    Object a = client.target(authUrl)
                            .request(MediaType.APPLICATION_JSON)
                            .get((new GenericType<Object>() {
                            }));
                    LinkedHashMap hm = ((LinkedHashMap) a);
                    String token = (String) hm.get("token");
                    logger.info("Token successfully retrieved.");
                    this.token = token;
                    return token;
                } catch (WebApplicationException | ProcessingException e) {
                    logger.error(e);
                    throw new InternalServerErrorException(e);
                }
            }
            return null;
        }
    }

}
