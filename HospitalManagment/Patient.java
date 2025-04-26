package HospitalManagment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner sc;

    public Patient(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name = sc.next();
        System.out.print("Enter Patient age: ");
        int age = sc.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = sc.next();
        System.out.print("Enter Family Contect no.: ");
        int mobile_no = sc.nextInt();

        try{
            String query = "INSERT INTO patients(name,age,gender,mobile_no) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            preparedStatement.setInt(4,mobile_no);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Patient added successfully!");
            }else{
                System.out.println("Failed to add patient");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+------------+---------------------+---------+-------------+-------------+");
            System.out.println("| Patient Id | name                | age     | Gender      | Mobile no.  |");
            System.out.println("+-----------+---------------------+---------+-------------+--------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                int mobile_no = resultSet.getInt("mobile_no");
                System.out.printf("| %-10s | %-19s | %-7s | %-11s | %-11s |\n",id,name,age,gender,mobile_no);
                System.out.println("+-----------+---------------------+---------+-------------+--------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientId(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
