***

# EmpleadosApp

**EmpleadosApp** es una aplicación Java de consola que permite la gestión básica de empleados y departamentos usando una base de datos MySQL. La aplicación fue desarrollada con el objetivo de aprender interacción con bases de datos mediante JDBC y prácticas de CRUD (Create, Read, Update, Delete) en Java.

***

## Características

- Gestión de **departamentos**: alta, modificación, eliminación y listado.
- Gestión de **empleados**: alta, modificación, eliminación y listado.
- Relación entre empleados y departamentos.
- Búsqueda y visualización de datos mediante ID o listados completos.
- Validación de entradas y manejo de excepciones.
- Uso de transacciones para asegurar la integridad de los datos.

***

## Requisitos

- Java 17 o superior.
- MySQL Server (corriendo localmente en `127.0.0.1:3306`).
- Driver JDBC para MySQL.
- Base de datos llamada `EMPLEADOS` con las tablas `DEPARTAMENTO` y `EMPLEADO`.

***

## Instalación

1. **Clona este repositorio**

    ```bash
    git clone https://github.com/usuario/EmpleadosApp.git
    cd EmpleadosApp
    ```

2. **Configura la base de datos**

   Ejecuta en MySQL:

    ```sql
    CREATE DATABASE EMPLEADOS;
    USE EMPLEADOS;

    CREATE TABLE DEPARTAMENTO (
      DEPARTAMENTO_ID INT AUTO_INCREMENT PRIMARY KEY,
      NOMBRE_DEPARTAMENTO VARCHAR(50) NOT NULL
    );

    CREATE TABLE EMPLEADO (
      EMPLEADO_ID INT AUTO_INCREMENT PRIMARY KEY,
      NOMBRE_EMPLEADO VARCHAR(50) NOT NULL,
      APELLIDO_EMPLEADO VARCHAR(50) NOT NULL,
      PUESTO VARCHAR(50) NOT NULL,
      SALARIO DOUBLE NOT NULL,
      DEPARTAMENTO_ID INT NOT NULL,
      FOREIGN KEY (DEPARTAMENTO_ID) REFERENCES DEPARTAMENTO(DEPARTAMENTO_ID)
    );
    ```

3. **Configura las credenciales**

   - Por defecto, se usa usuario `root` y contraseña `root`, ambos configurables en la clase.
   - Ajusta la URL, usuario y contraseña según tu configuración local.

4. **Compila y ejecuta el programa**

    ```bash
    javac -cp .:mysql-connector-java.jar com/actividad/EmpleadosApp.java
    java -cp .:mysql-connector-java.jar com.actividad.EmpleadosApp
    ```

***

## Uso

Sigue el menú interactivo en consola para realizar las distintas gestiones de empleados y departamentos. Ingresa el número de opción correspondiente y los datos solicitados según corresponda.

***

## Organización del código

- Toda la lógica está contenida en la clase `EmpleadosApp` dentro del paquete `com.actividad`.
- Las operaciones disponibles son claras y están validadas para evitar errores de datos.


***

## Autor

Desarrollado por [Natasha Solange Marcos Curbalán].

