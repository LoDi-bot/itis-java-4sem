package ru.itis.repositories;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.itis.models.Driver;

import java.util.List;
import java.util.Optional;

public class DriverRepositoryHibernateImpl implements DriverRepository {

    //language=HQL
    private final String HQL_FIND_ALL = "select d from Driver d";

    private final SessionFactory sessionFactory;

    public DriverRepositoryHibernateImpl() {
        Configuration hibernateConfiguration = new Configuration().configure("hibernate/hibernate.cfg.xml");
        this.sessionFactory = hibernateConfiguration.buildSessionFactory();
    }

    @Override
    public Optional<Driver> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Driver.class, id));
        }
    }

    @Override
    public List<Driver> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Driver> query = session.createQuery(HQL_FIND_ALL);
            return query.getResultList();
        }
    }

    @Override
    public Driver save(Driver driver) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Driver.class, driver.getId()) == null) {
                session.persist(driver);
            } else {
                session.update(driver);
            }
            return driver;
        }
    }

    @Override
    public void delete(Driver driver) {
        try (Session session = sessionFactory.openSession()) {
            session.delete(driver);
        }
    }
}
