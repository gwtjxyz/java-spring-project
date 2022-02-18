## TJV-Application

My Spring Boot-based application for the TJV class.
This is the server side. The client side is in
another repository: [https://gitlab.fit.cvut.cz/udaviyur/tjvclient](https://gitlab.fit.cvut.cz/udaviyur/tjvclient)

#### Prerequisites for running the server
Need to have PostgreSQL installed and running in your system.
If there's anything in the database before the app
will clear it out after starting and set up its own
entities.

#### Running the server
Click on the green arrow in TjvApplication.java.
Gradle's bootRun also seems to work.

#### Communicating with the server
REST API is supported. By default the address of
the server is http://localhost:9090/, further
instructions on sending REST requests are inside
the `REST API Specification.txt` file.