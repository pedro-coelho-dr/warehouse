package com.warehouse.warehouse.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.warehouse.warehouse.entity.Pessoa;

public class test {

    private static final String URL = "jdbc:mysql://localhost:3306/warehouse";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {

        try {
//            conecta com o bd
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select id, nome, email, tipo, cpf from pessoa;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String email = resultSet.getString("email");
                String tipo = resultSet.getString("tipo");
                String cpf = resultSet.getString("cpf");

                Pessoa p1 = new Pessoa(nome, email, tipo, cpf);

                System.out.println(id + " - " + p1.getNome() + " - " + p1.getEmail() + " - " + p1.getTipo() + " - " + p1.getCpf() + "\n");
            }


            // Example: Insert operation
            //        insertData(connection, "John", 30);
            // Example: Select operation
            //        selectData(connection);
            // Example: Update operation
            //        updateData(connection, 1, "David");
            // Example: Delete operation
            //        deleteData(connection, 2);


//            fecha conexao com o bd
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
