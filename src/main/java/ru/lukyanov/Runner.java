package ru.lukyanov;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lukyanov.config.AppConfig;
import ru.lukyanov.entity.Customer;
import ru.lukyanov.service.CustomerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {

        logger.info("Starting main method");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        try (context) {
            Customer customer = context.getBean(CustomerService.class).get(1L);
            System.out.println();
            System.out.println("1 " + customer);

            customer.setPassword("SomeNewPassword");
            System.out.println("2 " + customer);

            context.getBean(CustomerService.class).update(customer);

            Customer updatedCustomer = context.getBean(CustomerService.class).get(1L);
            System.out.println("3 " + updatedCustomer);
        }
        logger.info("Finishing main method");

    }
}