package net.bmacattack.queue.persistence.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.StringUtils;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Set;

@Embeddable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAccessToken {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String accessToken;
    @Column(nullable = false)
    private String accessRights;

    public UserAccessToken() {
    }

    public UserAccessToken(String name, String accessToken, String accessRights) {
        this.name = name;
        this.accessToken = accessToken;
        this.accessRights = accessRights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Set<String> getRights() {
        return StringUtils.commaDelimitedListToSet(accessRights);
    }

    public void addRight(String access) {
        if (access.contains(",")) throw new IllegalArgumentException("Cant add comma in access: " + access);
        Set<String> rights = StringUtils.commaDelimitedListToSet(accessRights);
        rights.add(access);
        accessRights = StringUtils.collectionToCommaDelimitedString(rights);
    }
}
