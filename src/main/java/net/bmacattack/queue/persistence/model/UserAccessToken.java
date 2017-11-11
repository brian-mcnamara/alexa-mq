package net.bmacattack.queue.persistence.model;

import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import java.util.Set;

@Embeddable
public class UserAccessToken {

    private String name;
    private String accessToken;
    private String accessRights;

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
