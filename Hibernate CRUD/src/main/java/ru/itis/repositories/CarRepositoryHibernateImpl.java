package ru.itis.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.itis.models.Car;
import ru.itis.models.Driver;

import java.util.List;
import java.util.Optional;

public class CarRepositoryHibernateImpl implements CarRepository {

    //language=HQL
    private final String HQL_FIND_ALL = "select c from Car c";

    private final SessionFactory sessionFactory;

    public CarRepositoryHibernateImpl() {
        Configuration hibernateConfiguration = new Configuration().configure("hibernate/hibernate.cfg.xml");
        this.sessionFactory = hibernateConfiguration.buildSessionFactory();
    }

    @Override
    public Optional<Car> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Car.class, id));
        }
    }

    @Override
    public List<Car> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Car> query = session.createQuery(HQL_FIND_ALL);
            return query.getResultList();
        }
    }

    @Override
    public Car save(Car car) {
        try (Session session = sessionFactory.openSession()) {
            if (session.find(Driver.class, car.getId()) == null) {
                session.persist(car);
            } else {
                session.update(car);
            }
            return car;
        }
    }

    @Override
    public void delete(Car car) {
        try (Session session = sessionFactory.openSession()) {
            session.delete(car);
        }
    }
}
