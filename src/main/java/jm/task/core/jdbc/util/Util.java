package jm.task.core.jdbc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util implements AutoCloseable {

    // JDBC
    private static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final Connection connection;
    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }


    // Hibernate
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Session session = getSession();

    private static SessionFactory buildSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/users?useSSL=false&serverTimezone=UTC");
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "root");
            configuration.setProperty("hibernate.current_session_context_class", "thread");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
        }
        return sessionFactory;
    }

    public static Session getSession() {
        if (session == null || !session.isOpen()) {
            try {
                return sessionFactory.openSession();
            } catch (Exception e) {
                System.err.println("Initial Session creation failed." + e);
            }
        }
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
