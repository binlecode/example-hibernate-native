package example.binle.hbm;

import java.util.HashMap;
import java.util.Map;

import example.binle.hbm.entity.Business;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * this util class is to create hibernate sessionFactory without using hibernate.cfg.xml
 */
public class HibernateUtilNoCfgXml {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                // Create registry builder
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Map<String, String> settings = new HashMap<>();

                settings.put("hibernate.hbm2ddl.auto", "update");
                settings.put("hibernate.connection.driver_class", "org.h2.Driver");
                settings.put("hibernate.connection.url", "jdbc:h2:./testDb2");
                settings.put("hibernate.connection.username", "sa");
                settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

                // Apply settings
                registryBuilder.applySettings(settings);

                // Create registry
                registry = registryBuilder.build();

                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // add mapping xml file as resource
                sources.addResource("employee.hbm.xml");

                // add annotated entity by class or by name
                sources.addAnnotatedClass(Business.class);
                sources.addAnnotatedClassName("example.binle.hbm.entity.Company");

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();



            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
