package ru.lukyanov.repository;

import lombok.SneakyThrows;
import ru.lukyanov.entity.Customer;
//import ru.lukyanov.processor.Benchmark;
import ru.lukyanov.repository.ConnectionPool;
import ru.lukyanov.repository.Repo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Repository
public class CustomerRepo implements Repo<Customer> {

    public static final String GET_CUSTOMER_BY_ID = """
            SELECT id, login, password
            FROM game.users
            WHERE id=?
            """;

    public static final String UPDATE_CUSTOMER = """
            UPDATE game.users
            SET login=?, password=?
            WHERE id=?
            """;

    private final ConnectionPool pool;

    public CustomerRepo(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    @SneakyThrows
    public Customer getById(Long id) {
        try(Connection connection = pool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Customer customer = new Customer();
                customer.setId(resultSet.getLong("id"));
                customer.setLogin(resultSet.getString("login"));
                customer.setPassword(resultSet.getString("password"));
                return customer;

            }
            else {
                throw new RuntimeException("incorrect id"); }
        }

    }

    @Override
    @SneakyThrows
    public void update(Customer entity) {
        try(Connection connection = pool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER);
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.execute();
        }
    }
}