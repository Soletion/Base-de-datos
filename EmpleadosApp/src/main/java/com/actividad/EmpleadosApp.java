package com.actividad;
// Importar las librerías necesarias

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EmpleadosApp {

    // Definir la URL de conexión, usuario y contraseña
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/EMPLEADOS?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";
    // Crear la conexión
    private Connection conn;

    // Constructor de la clase
    public EmpleadosApp() throws SQLException {
        conn = DriverManager.getConnection(URL, USER, PASS);
        conn.setAutoCommit(false); // Para transacciones manuales
    }

    // Agregar departamento
    public void agregarDepartamento(String nombre) throws SQLException {
        String sql = "INSERT INTO DEPARTAMENTO (NOMBRE_DEPARTAMENTO) VALUES (?)";
        // Controlar excepciones
        if (nombre == null) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        if (nombre.isEmpty()) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 50) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
        // Preparar la sentencia SQL
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            // Ejecutar la sentencia
            ps.executeUpdate();
            // Confirmar la transacción
            conn.commit();
            System.out.println("==========================");
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Departamento agregado: " + nombre + " con ID: " + rs.getInt(1) + ".");
                } else {
                    System.out.println("Departamento agregado: " + nombre + " (no se pudo obtener ID generado).");
                }
            }
        } catch (SQLException e) {
            // Si ocurre un error, revertir la transacción
            conn.rollback();
            throw e;
        }
    }

    // Modificar departamento
    public void modificarDepartamento(int departamentoId, String nuevoNombre) throws SQLException {
        String sql = "UPDATE DEPARTAMENTO SET NOMBRE_DEPARTAMENTO = ? WHERE DEPARTAMENTO_ID = ?";
        // Controlar excepciones
        if (nuevoNombre == null) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        if (departamentoId <= 0) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El ID del departamento no puede ser menor o igual a cero");
        }
        if (nuevoNombre.isEmpty()) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nuevoNombre.length() > 50) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
        // Preparar la sentencia SQL
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, departamentoId);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                conn.commit();
                System.out.println("==========================");
                System.out.println("Departamento modificado con el nuevo nombre: " + nuevoNombre + " con ID: " + departamentoId + ".");
            } else {
                System.out.println("==========================");
                System.out.println("No existe el departamento con ese ID.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Eliminar departamento
    public void eliminarDepartamento(int departamentoId) throws SQLException {
        String sql = "DELETE FROM DEPARTAMENTO WHERE DEPARTAMENTO_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departamentoId);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                conn.commit();
                System.out.println("==========================");
                System.out.println("Departamento eliminado.");
            } else {
                System.out.println("==========================");
                System.out.println("No existe el departamento con ese ID o tiene empleados asignados.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public void agregarEmpleado(String nombre, String apellido, String puesto, double salario, int departamentoId) throws SQLException {
        String sql = "INSERT INTO EMPLEADO (NOMBRE_EMPLEADO, APELLIDO_EMPLEADO, PUESTO, SALARIO, DEPARTAMENTO_ID) VALUES (?,?,?,?,?)";
        // Controlar excepciones
        if (nombre == null || apellido == null || puesto == null) {
            System.out.println("==========================");
            throw new IllegalArgumentException("Los campos no pueden ser nulos");
        }
        if (salario < 0) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El salario no puede ser negativo");
        }
        if (departamentoId <= 0) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El ID del departamento no puede ser menor o igual a cero");
        }
        if (nombre.isEmpty() || apellido.isEmpty() || puesto.isEmpty()) {
            System.out.println("==========================");
            throw new IllegalArgumentException("Los campos no pueden estar vacíos");
        }
        if (nombre.length() > 50 || apellido.length() > 50 || puesto.length() > 50) {
            System.out.println("==========================");
            throw new IllegalArgumentException("Los campos no pueden exceder los 50 caracteres");
        }
        // Preparar la sentencia SQL
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, puesto);
            ps.setDouble(4, salario);
            ps.setInt(5, departamentoId);
            ps.executeUpdate();
            conn.commit();
            System.out.println("==========================");
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Empleado agregado: " + nombre + " con ID: " + rs.getInt(1) + ".");
                } else {
                    System.out.println("Empleado agregado: " + nombre + " (no se pudo obtener ID generado).");
                }
            }

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Modificar empleado
    public void modificarEmpleado(int empleadoId, String nuevoNombre, String nuevoApellido, String nuevoPuesto, double nuevoSalario) throws SQLException {
        String sql = "UPDATE EMPLEADO SET NOMBRE_EMPLEADO = ?, APELLIDO_EMPLEADO = ?, PUESTO = ?, SALARIO = ? WHERE EMPLEADO_ID = ?";
        // Controlar excepciones
        if (nuevoNombre == null || nuevoApellido == null || nuevoPuesto == null) {
            System.out.println("==========================");
            throw new IllegalArgumentException("Los campos no pueden ser nulos");
        }
        if (nuevoSalario < 0) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El salario no puede ser negativo");
        }
        if (empleadoId <= 0) {
            System.out.println("==========================");
            throw new IllegalArgumentException("El ID del empleado no puede ser menor o igual a cero");
        }
        if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoPuesto.isEmpty()) {
            System.out.println("==========================");
            throw new IllegalArgumentException("Los campos no pueden estar vacíos");
        }
        if (nuevoNombre.length() > 50 || nuevoApellido.length() > 50 || nuevoPuesto.length() > 50) {
            System.out.println("==========================");
            throw new IllegalArgumentException("Los campos no pueden exceder los 50 caracteres");
        }
        // Preparar la sentencia SQL
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevoApellido);
            ps.setString(3, nuevoPuesto);
            ps.setDouble(4, nuevoSalario);
            ps.setInt(5, empleadoId);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                conn.commit();
                System.out.println("==========================");
                System.out.println("Empleado modificado con el nuevo nombre: " + nuevoNombre + " " + nuevoApellido + " con ID: " + empleadoId + ".");
            } else {
                System.out.println("==========================");
                System.out.println("No existe el empleado con ese ID.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Eliminar empleado
    public void eliminarEmpleado(int empleadoId) throws SQLException {
        String sql = "DELETE FROM EMPLEADO WHERE EMPLEADO_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empleadoId);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                conn.commit();
                System.out.println("==========================");
                System.out.println("Empleado eliminado con Nombre: " + empleadoId + " con ID: " + empleadoId + ".");
            } else {
                System.out.println("==========================");
                System.out.println("No existe el empleado con ese ID.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // Mostrar empleados por departamento
    public void printEmpleadosPorDepartamento(int departamentoId) throws SQLException {
        String sql = "SELECT * FROM EMPLEADO WHERE DEPARTAMENTO_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departamentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("======= DEPARTAMENTO ID: " + departamentoId + " =======");
                    System.out.printf("ID: %d, Nombre: %s %s, Puesto: %s, Salario: %.2f\n",
                            rs.getInt("EMPLEADO_ID"), rs.getString("NOMBRE_EMPLEADO"),
                            rs.getString("APELLIDO_EMPLEADO"), rs.getString("PUESTO"),
                            rs.getDouble("SALARIO"));
                }
            }
        }
    }

    // Mostrar empleado específico
    public void printEmpleadoEspecifico(int empleadoId) throws SQLException {
        String sql = "SELECT * FROM EMPLEADO WHERE EMPLEADO_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empleadoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("==========================");
                    System.out.printf("ID: %d, Nombre: %s %s, Puesto: %s, Salario: %.2f\n",
                            rs.getInt("EMPLEADO_ID"), rs.getString("NOMBRE_EMPLEADO"),
                            rs.getString("APELLIDO_EMPLEADO"), rs.getString("PUESTO"),
                            rs.getDouble("SALARIO"));
                } else {
                    System.out.println("==========================");
                    System.out.println("No existe el empleado con ese ID.");
                }
            }
        }
    }

    // Mostrar todos los empleados
    public void printTodosEmpleados() throws SQLException {
        // Aqui hacemos un JOIN :)
        String sql = "SELECT E.*, D.NOMBRE_DEPARTAMENTO FROM EMPLEADO E JOIN DEPARTAMENTO D ON E.DEPARTAMENTO_ID = D.DEPARTAMENTO_ID";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("==========================");
                System.out.printf("ID: %d, Nombre: %s %s, Puesto: %s, Salario: %.2f, Departamento: %s\n",
                        rs.getInt("EMPLEADO_ID"), rs.getString("NOMBRE_EMPLEADO"),
                        rs.getString("APELLIDO_EMPLEADO"), rs.getString("PUESTO"),
                        rs.getDouble("SALARIO"), rs.getString("NOMBRE_DEPARTAMENTO"));
            }
        }
    }

    // Mostrar todos los departamentos
    public void printTodosDepartamentos() throws SQLException {
        String sql = "SELECT * FROM DEPARTAMENTO";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("==========================");
                System.out.printf("ID: %d, Nombre: %s\n", rs.getInt("DEPARTAMENTO_ID"), rs.getString("NOMBRE_DEPARTAMENTO"));
            }
        }
    }

    // Cerrar conexión
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            EmpleadosApp app = new EmpleadosApp();
            while (true) {
                System.out.println("==========================");
                System.out.println("Menú de opciones:");
                System.out.println("==========================");
                System.out.println("1. Agregar departamento\n2. Modificar departamento\n3. Eliminar departamento\n4. Agregar empleado\n5. Modificar empleado\n6. Eliminar empleado\n7. Mostrar empleado por ID\n8. Mostrar departamento por ID \n9. Mostrar departametos \n10. Mostrar TODO\n0. Salir.");
                System.out.print("Selecciona una opción: ");
                int op = Integer.parseInt(sc.nextLine());
                if (op == 0) {
                    break;
                }
                switch (op) {
                    case 1 -> {
                        System.out.print("Nombre del departamento: ");
                        String nombreDepartamento = sc.nextLine();
                        app.agregarDepartamento(nombreDepartamento);
                    }
                    case 2 -> {
                        System.out.print("ID del departamento a modificar: ");
                        int idDepartamento = Integer.parseInt(sc.nextLine());
                        System.out.print("Nuevo nombre del departamento: ");
                        String nuevoNombreDepartamento = sc.nextLine();
                        app.modificarDepartamento(idDepartamento, nuevoNombreDepartamento);
                    }
                    case 3 -> {
                        System.out.print("ID del departamento a eliminar: ");
                        int idEliminarDepartamento = Integer.parseInt(sc.nextLine());
                        app.eliminarDepartamento(idEliminarDepartamento);
                    }
                    case 4 -> {
                        System.out.print("Nombre del empleado: ");
                        String nombreEmpleado = sc.nextLine();
                        System.out.print("Apellido del empleado: ");
                        String apellidoEmpleado = sc.nextLine();
                        System.out.print("Puesto del empleado: ");
                        String puestoEmpleado = sc.nextLine();
                        System.out.print("Salario del empleado: ");
                        double salarioEmpleado = Double.parseDouble(sc.nextLine());
                        System.out.print("ID del departamento: ");
                        int idEmpleadoDepartamento = Integer.parseInt(sc.nextLine());
                        app.agregarEmpleado(nombreEmpleado, apellidoEmpleado, puestoEmpleado, salarioEmpleado, idEmpleadoDepartamento);
                    }
                    case 5 -> {
                        System.out.print("ID del empleado a modificar: ");
                        int idModificarEmpleado = Integer.parseInt(sc.nextLine());
                        System.out.print("Nuevo nombre del empleado: ");
                        String nuevoNombreEmpleado = sc.nextLine();
                        System.out.print("Nuevo apellido del empleado: ");
                        String nuevoApellidoEmpleado = sc.nextLine();
                        System.out.print("Nuevo puesto del empleado: ");
                        String nuevoPuestoEmpleado = sc.nextLine();
                        System.out.print("Nuevo salario del empleado: ");
                        double nuevoSalarioEmpleado = Double.parseDouble(sc.nextLine());
                        app.modificarEmpleado(idModificarEmpleado, nuevoNombreEmpleado, nuevoApellidoEmpleado, nuevoPuestoEmpleado, nuevoSalarioEmpleado);
                    }
                    case 6 -> {
                        System.out.print("ID del empleado a eliminar: ");
                        int idEliminarEmpleado = Integer.parseInt(sc.nextLine());
                        app.eliminarEmpleado(idEliminarEmpleado);
                    }
                    case 7 -> {
                        System.out.print("ID del empleado a mostrar: ");
                        int idMostrarEmpleado = Integer.parseInt(sc.nextLine());
                        app.printEmpleadoEspecifico(idMostrarEmpleado);
                    }
                    case 8 -> {
                        System.out.print("ID del departamento a mostrar: ");
                        int idMostrarDepartamento = Integer.parseInt(sc.nextLine());
                        app.printEmpleadosPorDepartamento(idMostrarDepartamento);
                    }
                    case 9 -> {
                        System.out.println("==========================");
                        app.printTodosDepartamentos();
                    }
                    case 10 ->
                        app.printTodosEmpleados();
                    default -> {
                        System.out.println("==========================");
                        System.out.println("Opción no válida.");
                    }
                }
            }
            app.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
