package net.bmacattack.queue.persistence;

import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

public class JdbcClientDetailSeviceExtension extends JdbcClientDetailsService {
    public JdbcClientDetailSeviceExtension(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails details = super.loadClientByClientId(clientId);
       // if (details.getAuthorities())
        return details;
    }
}
