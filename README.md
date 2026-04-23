# Client-Server-Architecture-CW (Smart campus API)

## Overview
The smart campus API built for the CSA coursework is a RESTful web service built using JAX-RS (jersey) and Apache Tomcat. It provides a comprehensive interface for managing campus rooms and sensors. The API supports full CRUD operations for rooms and sensors, historical tracking, and robust error handling as required by the coursework specification.



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

