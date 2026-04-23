# Client-Server-Architecture-CW (Smart campus API)

## Overview
The smart campus API built for the CSA coursework is a RESTful web service built using JAX-RS (jersey) and Apache Tomcat. It provides a comprehensive interface for managing campus rooms and sensors. The API supports full CRUD operations for rooms and sensors, historical tracking, and robust error handling as required by the coursework specification.



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

