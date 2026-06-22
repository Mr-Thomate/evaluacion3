# evaluacion3

--INTEGRANTES--
-Thomas Mery
-Matias Diaz
-Christian Villalobos

--DESCRIPCION--
Este proyecto consiste en una API desarrollada con Java concretamente en Spring Boot, el proyeco esta enfocado en la administración de una biblioteca. La idea principal es poder gestionar los distintos elementos importantes de un sistema basado en una biblioteca tales como: Autores, Libros, Empleados, prestamos, etc.

El proyecto está organizado utilizando la estructura CSR vista en clase (controller, service, repository, model y dto).

se utilizaron buenas prácticas básicas (vistas en clase) de desarrollo backend y conexión a base de datos mediante Spring Data JPA.

--PASOS PARA EJECUTAR EL PROGRAMA--
1.-ejecutar laragon e iniciar el servicio de mysql
2.-En Laragon, en la base de datos el script que usamos para crear la base de datos es: CREATE DATABASE biblioteca_ev2;
3.- en la herramineta de base de datos incluida en mysql utlizar el scrypt para crear la base de datos necesaria para utilizr correctamente el proyecto
4.- utlizar la IDE preferida (en este caso VScode) para abrir el proyecto
5.- asegurarse que application.properties este todo correctamente y bien escrito para no tener errores de conexion con la base de datos
6.-ejcutar la aplicaion desde la clase principal
7.-el proyecto utilizara la url "http://localhost:8080"
8.- utilizar "Postman" para utlizar y verificar cada endpoint utilizable tales como: libros, clientes, prestamos, categorias, empleados, etc.
