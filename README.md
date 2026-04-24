# Client-Server-Architecture-CW (Smart campus API)

## Overview
The smart campus API built for the CSA coursework is a RESTful web service built using JAX-RS (jersey) and Apache Tomcat. It provides a comprehensive interface for managing campus rooms and sensors. The API supports full CRUD operations for rooms and sensors, historical tracking, and robust error handling as required by the coursework specification.


### Core Resources
- **Rooms** - Physical spaces on campus (`/api/v1/rooms`)
- **Sensors** - Devices deployed in rooms (`/api/v1/sensors`)
- **Sensor Readings** - Historical data logged by sensors (`/api/v1/sensors/{id}/readings`)


### Technology Stack Used
- Java 17
- JAX-RS (Jersey 3.1.3)
- Apache Tomcat 10.1.54
- Maven
- Jackson (JSON)



---

## How to Build and Run


### Requirements
- Java JDK 17 or higher
- Apache Maven
- Apache Tomcat 10.1.54
- Apache NetBeans IDE



### Step by step instructions to build the project and launch the server

**1. Clone the repository:**
```bash
git clone https://github.com/MthukaFernando/Client-Server-Architecture-CW.git
```

**2. Open the project in NetBeans:**
- Open Apache NetBeans
- Go to File → Open Project
- Navigate to the cloned folder and open it

**3. Build the project:**
- Right click the project in the Projects panel
- Click "Clean and Build"
- Wait for 'BUILD SUCCESS'

**4. Configure Tomcat in NetBeans:**
- Go to Tools → Servers
- Add Apache Tomcat pointing to your Tomcat installation folder

**5. Run the project:**
- Right-click the project
- Click "Run"
- Wait for "Started application at context path [/SmartCampusAPI]"

**6. The API will be available at:**
http://localhost:8080/SmartCampusAPI/api/v1



---

## Sample curl commands with successful interactions with different parts of the API


**1. Get API discovery info:**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1
```

**2. Create a room:**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'
```

**3. Create a sensor:**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":22.5,"roomId":"LIB-301"}'
```

**4. Get sensors filtered by type:**
```bash
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
```

**5. Post a sensor reading:**
```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":25.5}'
```

**6. Get all readings for a sensor:**
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings
```

**7. Delete a room with no sensors (successful deletion):**
```bash
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
```



---


## Conceptual Report


### Part 1 - Question 1: JAX-RS Resource Lifecycle
By default JAX-RS creates a new instance of a resource class for every incoming HTTP request. This is called **per-request scope**. This means each request gets its own fresh object which avoids issues with shared state between requests. However because a new instance is created per request, resource classes cannot store data in instance variables. Any data stored there would be lost after the request completes. This is why we use a static `DataStore` class with static `HashMap` collections to store rooms sensors and readings. Since static fields belong to the class rather than any instance, they persist across all requests and are shared by all resource instances. To prevent race conditions in a multi-threaded environment, `ConcurrentHashMap` could be used instead of `HashMap`.


### Part 1 - Question 2: HATEOAS
HATEOAS (Hypermedia as the Engine of Application State) is considered to be a hallmark of advanced RESTful design because it makes APIs self discoverable. Instead of requiring clients to memorise or look up endpoint URLs from external documentation, the API itself provides links to related resources within its responses. For example, the discovery endpoint at `GET /api/v1` returns links to `/api/v1/rooms` and `/api/v1/sensors`. This benefits client developers because they can navigate the API dynamically, reducing the risk of hardcoding URLs that may change in future versions. It also makes the API more resilient to change as clients follow links rather than constructing URLs manually.


### Part 2 - Question 1: IDs vs Full Objects
Returning only IDs in a list response reduces the payload size significantly which saves network bandwidth, especially when there are thousands of rooms. However it forces the client to make additional requests to fetch details for each room, increasing the number of round trips. Returning full objects provides all the information in one request, reducing round trips but increasing payload size. For large collections the best practice is to return a summary of each object (e.g., id and name only) rather than full objects or just IDs, balancing bandwidth and usability.


### Part 2 - Question 2: DELETE Idempotency
Yes, the DELETE operation is idempotent in my implementation. Idempotency means that making the same request multiple times produces the same result as making the request once. In my implementation the first DELETE request for a room that exists and has no sensors will successfully delete it and return 204 No Content. If the same DELETE request is sent again, the room no longer exists and the service returns 404 Not Found. Although the response code differs, the end state of the system is the same.(The room does not exist)This is consistent with the HTTP specification's definition of idempotency, which focuses on the server state rather than the response code.


### Part 3 - Question 1: @Consumes Annotation
The `@Consumes(MediaType.APPLICATION_JSON)` annotation tells Jersey that the POST method only accepts requests with a `Content-Type: application/json` header. If a client sends data in a different format such as `text/plain` or `application/xml`, JAX-RS will automatically reject the request and return a `415 Unsupported Media Type` response without even reaching the method. This protects the API from malformed input and ensures that Jackson can safely deserialise the request body into the Java objects.


### Part 3 - Question 2: @QueryParam vs Path Parameter
Using `@QueryParam` for filtering (e.g., `/api/v1/sensors?type=CO2`) is generally considered superior to embedding the filter in the path (e.g., `/api/v1/sensors/type/CO2`) for several reasons. Query parameters are optional, meaning the same endpoint works both with and without the filter. Path parameters suggest a hierarchical resource structure, implying that `type` is a sub resource of `sensors` which is semantically incorrect because we are filtering a collection, not navigating to a child resource. Query parameters also support multiple filters easily (e.g., `?type=CO2&status=ACTIVE`), while path based filtering becomes complex and unreadable with multiple criteria.


### Part 4 - Question: Sub-Resource Locator Pattern
The Sub Resource Locator pattern improves API architecture by asigning the handling of nested paths to dedicated classes. In the implementation, `SensorResource` asigns all `/readings` paths to `SensorReadingResource`. This separation of concerns makes each class smaller and easier to understand and maintain. In a large API with many nested resources putting every endpoint in one class would result in an unmanageable, large controller. By splitting responsibilities each class has a single, clear purpose. It also makes testing easier as each resource class can be tested independently.


### Part 5 - Question 1: Comparison of 422 and 404
HTTP 422 Unprocessable Entity is more semantically accurate than 404 Not Found when a referenced resource ID inside a valid JSON payload does not exist. A 404 suggests that the requested URL/endpoint was not found, which is misleading — the endpoint exists and is working correctly. The issue is that the data inside the request body references a non existent resource (a roomId that doesn't exist). HTTP 422 specifically means the server understood the request and the syntax was correct but the content was semantically invalid, which is exactly what is happening when a client provides a roomId that does not exist in the system.


### Part 5 - Question 2: Stack Trace Security Risks
Exposing internal Java stack traces to external API consumers poses significant security risks. Stack traces reveal the internal structure of the application, including class names, method names, and line numbers. An attacker can use this information to identify which frameworks and libraries are being used and their versions, allowing them to look up known vulnerabilities. Stack traces can also reveal the application's file system paths and architecture, making it easier to craft targeted attacks. Our `GlobalExceptionMapper` prevents this by catching all unhandled exceptions and returning only a generic 500 error message, hiding all internal details from the client.


### Part 5 - Question 3: Filters vs Manual Logging
Using JAX-RS filters for cross-cutting concerns like logging is far superior to manually inserting `Logger.info()` statements in every resource method. Filters are applied automatically to every request and response without modifying any resource class, following the DRY (Don't Repeat Yourself) principle. If logging logic needs to change it only needs to be updated in one place which is the filter class, rather than in every resource method. Manual logging is also more prone to errors, as developers may forget to add it to new methods. Filters also have access to request and response metadata that may not be easily available inside resource 


---
 