package net.andreaskluth.hibernate;

import java.io.Serializable;
import java.util.List;

import net.andreaskluth.hibernate.entities.Car;
import net.andreaskluth.hibernate.entities.Driver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.google.common.base.Stopwatch;
import com.hazelcast.core.Hazelcast;

public class Program {

  private static Log LOG = LogFactory.getLog(Program.class);

  private static SessionFactory sessionFactory;

  private static ServiceRegistry serviceRegistry;

  public static void main(String[] args) throws Exception {
    createSessionFactory();

    runStatelessHql();

    // Uncached
    obtainEntityInStupidUnitOfWork(Driver.class, 1);
    obtainEntityInStupidUnitOfWork(Driver.class, 2);
    // Cached
    obtainEntityInStupidUnitOfWork(Driver.class, 1);
    obtainEntityInStupidUnitOfWork(Driver.class, 2);

    obtainEntityInStupidUnitOfWork(Car.class, 1);
    obtainEntityInStupidUnitOfWork(Car.class, 2);

    // Uncached
    runStatefulHql();
    // Cached
    runStatefulHql();

    // Uncached
    runStatefulCriteria();
    // Cached
    runStatefulCriteria();

    // Uncached
    runStatelessHql();
    // Uncached
    runStatelessHql();
    
    LOG.info(sessionFactory.getStatistics());

    Hazelcast.shutdownAll();
  }

  private static <T> T obtainEntityInStupidUnitOfWork(Class<T> clazz, Serializable id) throws Exception {
    Session session = sessionFactory.openSession();
    try {
      session.getTransaction().begin();
      LOG.info("Before Contains: " + clazz.getName() + " with id " + id + "? "
          + sessionFactory.getCache().containsEntity(clazz, id));
      @SuppressWarnings("unchecked")
      T entity = (T) session.get(clazz, id);
      LOG.info("After Contains: " + clazz.getName() + " with id " + id + "? "
          + sessionFactory.getCache().containsEntity(clazz, id));
      session.getTransaction().commit();
      return entity;

    } catch (Exception ex) {
      session.getTransaction().rollback();
      throw ex;
    } finally {
      session.close();
    }
  }

  private static void runStatefulHql() throws Exception {
    Stopwatch watch = Stopwatch.createStarted();
    Session session = sessionFactory.openSession();
    try {
      session.getTransaction().begin();
      @SuppressWarnings("unchecked")
      List<Driver> list = 
        session.createQuery("select d from Driver d LEFT JOIN FETCH d.cars c")
        .setCacheable(true)
        .setCacheMode(CacheMode.NORMAL).list();
      for (Driver entry : list) {
        LOG.info("Entry " + entry.getId());
      }
      session.getTransaction().commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
      throw ex;
    } finally {
      session.close();
    }
    LOG.info("StatefulHql:=" + watch.toString());
  }
  
  private static void runStatefulCriteria() throws Exception {
    Stopwatch watch = Stopwatch.createStarted();
    Session session = sessionFactory.openSession();
    try {
      session.getTransaction().begin();
      @SuppressWarnings("unchecked")
      List<Driver> list = 
        session
          .createCriteria(Driver.class)
          .setFetchMode("cars", FetchMode.JOIN)
          .setCacheable(true)
          .setCacheMode(CacheMode.NORMAL)
          .list();
      for (Driver entry : list) {
        LOG.info("Entry " + entry.getId());
      }
      session.getTransaction().commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
      throw ex;
    } finally {
      session.close();
    }
    LOG.info("StatefulCriteria:=" + watch.toString());
  }

  private static void runStatelessHql() throws Exception {
    Stopwatch watch = Stopwatch.createStarted();
    StatelessSession statelessSession = sessionFactory.openStatelessSession();
    try {
      statelessSession.getTransaction().begin();
      Query query = statelessSession
          .createQuery(
              " SELECT d.id, d.firstName, d.lastName, c.id, c.make " + " FROM Driver d "
                  + " LEFT JOIN d.cars c WHERE index(c) LIKE 'Good'").setFetchSize(0).setReadOnly(true);

      ScrollableResults scroll = query.scroll(ScrollMode.FORWARD_ONLY);
      while (scroll.next()) {
        LOG.info("Entry " + scroll.get(0));
      }
      statelessSession.getTransaction().commit();
    } catch (Exception ex) {
      statelessSession.getTransaction().rollback();
      throw ex;
    } finally {
      statelessSession.close();
    }
    LOG.info("StatelessHql:=" + watch.toString());
  }

  public static void createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure();
    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
  }
}
