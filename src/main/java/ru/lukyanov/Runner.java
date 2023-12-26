package ru.lukyanov;


import ru.lukyanov.config.AppConfig;
import ru.lukyanov.entity.Customer;
import ru.lukyanov.repository.ConnectionPool;
import ru.lukyanov.repository.CustomerRepo;
import ru.lukyanov.service.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class Runner {
    public static void main(String[] args) {


        CustomerService customerService = new CustomerService();
        ConnectionPool connectionPool = new ConnectionPool();
        CustomerRepo customerRepo = new CustomerRepo(connectionPool);
        customerService.setCustomerRepo(customerRepo);

        Customer customer = customerService.get(1L);
        System.out.println(customer);


    }
}