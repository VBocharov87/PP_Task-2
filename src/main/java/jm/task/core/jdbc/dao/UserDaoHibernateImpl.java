package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String CREATE_SPREADSHEET_QUERY = "CREATE TABLE IF NOT EXISTS spreadsheet (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), lastname VARCHAR(255), age TINYINT)";
    private static final String DROP_SPREADSHEET_QUERY = "DROP TABLE IF EXISTS spreadsheet";

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.createSQLQuery(CREATE_SPREADSHEET_QUERY).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.createSQLQuery(DROP_SPREADSHEET_QUERY).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);

        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try (Session session = Util.getSession()) {
            session.beginTransaction();
            users = session.createQuery("from User", User.class).getResultList();
            session.getTransaction().commit();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
