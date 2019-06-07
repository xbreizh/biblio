package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.MailService;
import org.troparo.web.service.ConnectServiceImpl;
import org.troparo.web.service.MailServiceImpl;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ContextConfiguration("classpath:org/troparo/web/config/spring-hibernate-jax-ws.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
public class MailServiceIntegrationTest {
    @Inject
    private MailServiceImpl mailService;
   /* @Inject
    private MemberManager memberManager;
*/

    @BeforeEach
    void init() {
        //connectService = new ConnectServiceImpl();
        //memberManager = new MemberManagerImpl();
        //MemberDAO memberDAO = new MemberDAOImpl();
        //memberManager.setMemberDAO(memberDAO);
        //connectService.setMemberManager(memberManager);

    }


}
