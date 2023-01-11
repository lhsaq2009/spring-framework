package org.springframework.orm.jpa.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.AbstractContainerEntityManagerFactoryIntegrationTests;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.domain.Person;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hibernate-specific JPA tests with native SessionFactory setup and getCurrentSession interaction.
 *
 * @author Juergen Hoeller
 * @since 5.1
 */
public class HibernateNativeEntityManagerFactoryIntegrationTests extends AbstractContainerEntityManagerFactoryIntegrationTests {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "/org/springframework/orm/jpa/hibernate/hibernate-manager-native.xml",
                "/org/springframework/orm/jpa/memdb.xml",
                "/org/springframework/orm/jpa/inject.xml"};
    }


    @Override
    @Test
    public void testEntityManagerFactoryImplementsEntityManagerFactoryInfo() {
        boolean condition = entityManagerFactory instanceof EntityManagerFactoryInfo;
        assertThat(condition).as("Must not have introduced config interface").isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEntityListener() {
        String firstName = "Tony";
        insertPerson(firstName);

        List<Person> people = sharedEntityManager.createQuery("select p from Person as p").getResultList();
        assertThat(people.size()).isEqualTo(1);
        assertThat(people.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(people.get(0).postLoaded).isSameAs(applicationContext);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testCurrentSession() {
        String firstName = "Tony";
        insertPerson(firstName);

        Query q = sessionFactory.getCurrentSession().createQuery("select p from Person as p");
        List<Person> people = q.getResultList();
        System.out.println(people);

        assertThat(people.size()).isEqualTo(1);
        assertThat(people.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(people.get(0).postLoaded).isSameAs(applicationContext);
    }

    @Test  // SPR-16956
    public void testReadOnly() {
        assertThat(sessionFactory.getCurrentSession().getHibernateFlushMode()).isSameAs(FlushMode.AUTO);
        assertThat(sessionFactory.getCurrentSession().isDefaultReadOnly()).isFalse();
        endTransaction();

        this.transactionDefinition.setReadOnly(true);
        startNewTransaction();
        assertThat(sessionFactory.getCurrentSession().getHibernateFlushMode()).isSameAs(FlushMode.MANUAL);
        assertThat(sessionFactory.getCurrentSession().isDefaultReadOnly()).isTrue();
    }

}
