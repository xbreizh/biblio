package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.web.service.LoanServiceImpl;

import javax.inject.Inject;

@ContextConfiguration("classpath:org/troparo/web/config/spring-hibernate-jax-ws.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
public class LoanServiceIntegrationTest {
    @Inject
    private LoanServiceImpl loanService;
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

