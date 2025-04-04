package com.unicesumar;

import com.unicesumar.entities.User;
import com.unicesumar.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static UserRepository userRepository;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:your-database.db")) {
            createTablesIfNotExist(connection);
            userRepository = new UserRepository(connection);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Cadastrar usuário");
                System.out.println("2. Listar usuários");
                System.out.println("0. Sair");
                int option = scanner.nextInt();
                scanner.nextLine();
                if (option == 1) {
                    cadastrarUsuario(scanner);
                } else if (option == 2) {
                    listarUsuarios();
                } else if (option == 0) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTablesIfNotExist(Connection connection) throws SQLException {
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "uuid UUID PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTableSQL);
        }
    }

    private static void cadastrarUsuario(Scanner scanner) {
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        User user = new User(UUID.randomUUID(), name, email, password);
        userRepository.save(user);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void listarUsuarios() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            System.out.println("UUID: " + user.getUuid());
            System.out.println("Nome: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Senha: " + user.getPassword());
            System.out.println("-----");
        }
    }
}