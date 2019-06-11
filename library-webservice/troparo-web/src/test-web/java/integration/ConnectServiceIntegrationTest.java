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
import org.troparo.web.service.ConnectServiceImpl;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
@ContextConfiguration("classpath:org/troparo/web/config/spring-hibernate-jax-ws.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
public class ConnectServiceIntegrationTest {
    @Inject
    private ConnectServiceImpl connectService;
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

    @Test
    @Disabled("should return a token")
    void getToken() throws BusinessExceptionConnect {
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin("LOKII");
        parameters.setPassword("123");
        String token = connectService.getToken(parameters).getReturn();
        System.out.println("token returned: "+token);
        assertNotEquals("wrong login or pwd",token);
    }
}