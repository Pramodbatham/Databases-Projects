package HospitalManagment;

import java.sql.*;
import java.util.Scanner;

public class Hospital {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "prem8450";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(URL,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");

                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        // Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctor
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        // Book appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection, Scanner scanner){
        System.out.print("Enter Patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor id: ");
        int doctorId = scanner.nextInt();

        System.out.print("Enter appointment date (YYYY-MM-DD");
        String appointmentDate = scanner.next();
        if(patient.getPatientId(patientId) && doctor.getDoctorId(doctorId)){
            if(checkDocterAvaileble(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointment(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowAffected = preparedStatement.executeUpdate();
                    if(rowAffected >0){
                        System.out.println("Appointment Booked!!");
                    }else{
                        System.out.println("Appointment doesn't Booked!!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not available on this date!!");
            }
        }
        else{
            System.out.println("Either doctor or patient doesn't exist!!");
        }
    }

    public static boolean checkDocterAvaileble(int doctorId, String appointmentDate,Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE docter_id=? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }
}
