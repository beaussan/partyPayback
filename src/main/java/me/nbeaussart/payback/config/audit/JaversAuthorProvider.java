package me.nbeaussart.payback.config.audit;

import me.nbeaussart.payback.config.Constants;
import me.nbeaussart.payback.security.SecurityUtils;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.stereotype.Component;

@Component
public class JaversAuthorProvider implements AuthorProvider {

   @Override
   public String provide() {
       String userName = SecurityUtils.getCurrentUserLogin();
       return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
   }
}
