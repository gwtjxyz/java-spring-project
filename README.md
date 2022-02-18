# java-spring-project
A project I made for school using Spring Boot in Java
## Untitled Java Spring Project

My Spring Boot-based application for the for a university class.
The project is a three-layer application, complete with a functional REST API,
a business logic layer and a data manipulation layer. All three layers are
covered by unit tests. The application itself uses a PostgreSQL database installed
on the user's PC (Docker functionality is NOT implemented) while the tests use
an in-memory H2 database instead.

#### Prerequisites for running the server
PostgreSQL needs to be installed and running on your system.
Upon launching the app clears anything that may or may not have been
in the database prior and initializes tables needed for its operation.

#### Running the server
The application supports classic gradle commands for building and
running the app.

#### Communicating with the server
REST API is supported. By default the address of
the server is http://localhost:9090/, further
instructions on sending REST requests are inside
the `REST API Specification.txt` file.
