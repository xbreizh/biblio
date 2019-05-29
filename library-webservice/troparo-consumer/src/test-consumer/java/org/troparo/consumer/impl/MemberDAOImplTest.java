package org.troparo.consumer.impl;

import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.MemberDAO;

import javax.inject.Inject;

@ExtendWith(MockitoExtension.class)
@Transactional()
class MemberDAOImplTest {

    private ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            "application-context-test.xml");

    ///@Inject
    private static SessionFactory sessionFactory;

    @Inject
    private MemberDAO memberDAO;


    @Test
    void addMember() {

    }

    @Test
    void getAllMembers(){
        System.out.println(memberDAO);
        memberDAO = new MemberDAOImpl();

        System.out.println("member dao: " + memberDAO);
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