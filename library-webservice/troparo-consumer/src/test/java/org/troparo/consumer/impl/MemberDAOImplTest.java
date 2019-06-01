package org.troparo.consumer.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.MemberDAO;

import javax.inject.Inject;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Configuration
@ComponentScan("org.troparo.consumer")
@Transactional
class MemberDAOImplTest {

   /* private static ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            "application-context-test.xml");*/
   /* @Inject
    private MemberDAO memberDAO;*/
    //@Inject
    // private SessionFactory sessionFactory;

    @Inject
    SessionFactory sessionFactory;
    @Inject
    MemberDAO memberDAO;

    @BeforeEach
    void init() {
        /*applicationContext.getBean("transactionManager", HibernateTransactionManager.class);
        applicationContext.getBean("memberDAO", MemberDAO.class);*/
        //memberDAO = new MemberDAOImpl();
    }


    @Test
    void addMember() {

    }

    @Test
    void getAllMembers() {
        System.out.println("test");
        System.out.println(memberDAO.getAllMembers());
    }

    @Test
    void getMemberById() {
    }

    @Test
    void existingLogin() {
    }

    @Test
    void getMembersByCriterias() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void remove() {
    }

    @Test
    void checkToken() {
    }

    @Test
    void invalidToken() {
    }

    @Test
    void getMemberByLogin() {
    }

    @Test
    void getMemberByToken() {
    }
}