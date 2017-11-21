package net.bmacattack.queue.access;

import net.bmacattack.queue.security.authentication.UserTokenAuthentication;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.Serializable;
import java.util.Set;

public class ScopeBasedPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

        return hasPermission(auth, "TODO", targetType, permission);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        Set<String> scopes = null;
        if (auth instanceof OAuth2Authentication) {
            scopes = ((OAuth2Authentication) auth).getOAuth2Request().getScope();
        } else if (auth instanceof UserTokenAuthentication) {
            scopes = ((UserTokenAuthentication)auth).getScope();
        }

        if (scopes != null) {
            return scopes.contains(permission);
        }
        return false;
    }
}
