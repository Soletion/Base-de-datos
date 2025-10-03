package com.actividad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest {

    private Connection conn;
    private EmpleadosApp app;

    // Configuración inicial de la base de datos se hace otra vez porque en EmpleadosApp es privada.
    @BeforeAll
    void setup() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/EMPLEADOS?serverTimezone=UTC", "root", "root"
        );
        app = new EmpleadosApp();
        limpiarTablas();
    }

    @AfterAll
    void tearDown() throws SQLException {
        limpiarTablas();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        app.close();
    }

    void limpiarTablas() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM EMPLEADO");
            st.executeUpdate("DELETE FROM DEPARTAMENTO");
            st.executeUpdate("ALTER TABLE EMPLEADO AUTO_INCREMENT = 1");
            st.executeUpdate("ALTER TABLE DEPARTAMENTO AUTO_INCREMENT = 1");
        }
    }

    @Test
    void testAgregarDepartamento() throws SQLException {
        app.agregarDepartamento("Pruebas");
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM DEPARTAMENTO WHERE NOMBRE_DEPARTAMENTO=?")) {
            ps.setString(1, "Pruebas");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "No se encontró el departamento insertado");
            }
        }
    }

    @Test
    void testModificarDepartamento() throws SQLException {
        app.agregarDepartamento("Ventas");
        int id = getDepartamentoId("Ventas");
        app.modificarDepartamento(id, "Comercial");
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM DEPARTAMENTO WHERE DEPARTAMENTO_ID=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "No se encontró el departamento modificado");
                assertEquals("Comercial", rs.getString("NOMBRE_DEPARTAMENTO"));
            }
        }
    }

    @Test
    void testEliminarDepartamento() throws SQLException {
        app.agregarDepartamento("Temporal");
        int id = getDepartamentoId("Temporal");
        app.eliminarDepartamento(id);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM DEPARTAMENTO WHERE DEPARTAMENTO_ID=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                assertFalse(rs.next(), "El departamento no fue eliminado");
            }
        }
    }

    @Test
    void testAgregarEmpleado() throws SQLException {
        app.agregarDepartamento("Recursos Humanos");
        int depId = getDepartamentoId("Recursos Humanos");
        app.agregarEmpleado("Ana", "López", "Jefa", 3000.0, depId);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM EMPLEADO WHERE NOMBRE_EMPLEADO=? AND APELLIDO_EMPLEADO=?")) {
            ps.setString(1, "Ana");
            ps.setString(2, "López");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "No se encontró el empleado insertado");
                assertEquals("Jefa", rs.getString("PUESTO"));
            }
        }
    }

    @Test
    void testModificarEmpleado() throws SQLException {
        app.agregarDepartamento("ModEmp");
        int depId = getDepartamentoId("ModEmp");
        app.agregarEmpleado("Luis", "Martín", "Analista", 2000.0, depId);
        int empId = getEmpleadoId("Luis", "Martín");
        app.modificarEmpleado(empId, "Luis", "Martínez", "Analista Senior", 2500.0);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM EMPLEADO WHERE EMPLEADO_ID=?")) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "No se encontró el empleado modificado");
                assertEquals("Martínez", rs.getString("APELLIDO_EMPLEADO"));
                assertEquals("Analista Senior", rs.getString("PUESTO"));
                assertEquals(2500.0, rs.getDouble("SALARIO"));
            }
        }
    }

    @Test
    void testEliminarEmpleado() throws SQLException {
        app.agregarDepartamento("BorrarEmp");
        int depId = getDepartamentoId("BorrarEmp");
        app.agregarEmpleado("Pedro", "Gómez", "Tester", 1500.0, depId);
        int empId = getEmpleadoId("Pedro", "Gómez");
        app.eliminarEmpleado(empId);
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM EMPLEADO WHERE EMPLEADO_ID=?")) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                assertFalse(rs.next(), "El empleado no fue eliminado");
            }
        }
    }

    // Métodos auxiliares para obtener IDs
    int getDepartamentoId(String nombre) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT DEPARTAMENTO_ID FROM DEPARTAMENTO WHERE NOMBRE_DEPARTAMENTO=?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Departamento no encontrado: " + nombre);
    }

    int getEmpleadoId(String nombre, String apellido) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT EMPLEADO_ID FROM EMPLEADO WHERE NOMBRE_EMPLEADO=? AND APELLIDO_EMPLEADO=?")) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Empleado no encontrado: " + nombre + " " + apellido);
    }
}
