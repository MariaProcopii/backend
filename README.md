# **TrainingLicenseSharing**

## Overview
TrainingLicenseSharing is a robust Spring Boot application designed to
streamline the management of software and training licenses within a corporate
environment. Developed using Gradle as the build automation tool,
this application offers a comprehensive solution for tracking and managing
the allocation and usage of various licenses by employees in a company.

##### **Key Features:**
* **License Tracking:** Keeps a detailed record of both software and training licenses used across different departments.
* **User-Friendly Interface:** Simplifies the process of license management, making it accessible for administrators and managers.
* **Real-Time Updates:** Ensures that all information regarding license usage and allocation
  is up-to-date, facilitating better resource management.
* **Reporting and Analytics:** Provides insightful reports and analytics to help in decision-making
  regarding license purchases and renewals.

### **Installation:**

**Prerequisites**

Before you begin, ensure you have the following instaled:
* JDK 17
* PostgreSQL
* Gradle
* Rancher Desktop

**Building the Application Locally**
1. Open terminal in the root folder of the project
2. Run `docker-compose up -d`
3. Start the docker container `docker start postgres`
4. Check container status `docker container ls -a`
5. Run Database Migrations (from Linux terminal): `sh database/db-local.sh`

**Building the Application Remotely**
1. After creating the workspace and deploying docker, open terminal in the root folder of the project
2. Run Database Migrations (from Linux terminal): `sh database/db-env.sh`

**Running the Application Locally**
1. Open the Application using IntelliJ Idea
2. Run it
3. The application will be running on `http://localhost:8080`
4. Access the various endpoints through a REST client or browser as needed
5. In Postman choose Basic Auth in Authorization tab, and enter the following credentials: 
* Username: _admin@example.com_ 
* Password: _adminPassword_

6. You can access detailed documentation using Swagger following this link: `http://localhost:8080/swagger-ui/index.html`


# API Endpoints:
### LicenseController

---
**GET** `http://localhost:8080/license/get-expiring-licenses`

_Response body:_
```json
[
  {
    "name": "JetBrains",
    "cost": 1444,
    "availability": 120,
    "unusedPeriod": 0
  }
]
```
* Retrieves a list of licenses that are nearing expiration.
* Returns: List of `LicenseDTO` representing expiring licenses.
---

**GET** `http://localhost:8080/license/get-unused-licenses`

_Response body:_
```json
[
  {
    "name": "Postman",
    "cost": 666,
    "availability": 365,
    "unusedPeriod": 13
  }
]
```
* Fetches a list of licenses that are currently unused.
* Returns: List of `LicenseDTO` representing unused licenses.

---

**GET** `http://localhost:8080/license/get-all-licenses`

_Response body:_
```json
[
  {
    "logo": "ZGVmYXVsdC1sb2dvLWJhc2U2NC12YWx1ZQ==",
    "licenseName": "LinkedIn Learning",
    "description": "LinkedIn Learning API",
    "cost": 800.0,
    "currency": "USD",
    "licenseDuration": null,
    "durationUnit": "MONTH",
    "seatsAvailable": 10,
    "seatsTotal": 250,
    "isActive": false,
    "expirationDate": "2022-10-13",
    "licenseType": "TRAINING",
    "isRecurring": false
  },
  {
    "logo": "ZGVmYXVsdC1sb2dvLWJhc2U2NC12YWx1ZQ==",
    "licenseName": "Codecademy",
    "description": "Codecademy API",
    "cost": 1400.0,
    "currency": "USD",
    "licenseDuration": null,
    "durationUnit": "MONTH",
    "seatsAvailable": 10,
    "seatsTotal": 250,
    "isActive": false,
    "expirationDate": "2022-10-19",
    "licenseType": "TRAINING",
    "isRecurring": false
  }
]
```
* Returns: List of `LicenseSummaryDTO` representing all licenses.

---
**POST** http://localhost:8080/license/add-new-license
_Response body:_

```json
{
  "licenseName": "Sample License 1",
  "description": "This is a sample license description with a call-to-action.",
  "website": "www.sambplelicense.com",
  "cost": 6.0,
  "currency": "USD",
  "availability": 365,
  "seats": 100,
  "isActive": true,
  "expiresOn": "12-May-2023",
  "licenseType": "TRAINING",
  "isRecurring": "true",
  "credentials": [
    {
      "username": "john.doe@endava.com",
      "password": "johndoe"
    },
    {
      "username": "jane.smith@endava.com",
      "password": "JaneSmith"
    }
  ],
  "logo": ["your logo"]
}
```
* Creates new license
* Returns HTTP status
  _Response status:_
```
200
```
---

### RequestController

---
**GET** `http://localhost:8080/requests/get-requests?asc=true&field=requestDate`

_Parameters:_
* asc (Boolean): Optional parameter to define ascending order.
* field (String): Optional parameter to specify the field to sort by.

_Response body:_
```json
[
    {
        "requestId": 2,
        "status": "PENDING",
        "app": "JetBrains",
        "requestDate": "17-Nov-2023 00:00",
        "startOfUse": "06-Jun-2023",
        "username": "Jane Smith",
        "discipline": "CREATIVE_SERVICES"
    }
]

```

---

**POST** `http://localhost:8080/requests/request-access`
* Submits a request for access by a user.
  _Request body:_
```json
{
  "username" : "John Doe",
  "discipline": "DEVELOPMENT",
  "app" : "JetBrains",
  "startOfUse" : "06-Jun-2023"
}
```

_Response body:_
```json
"OK"
```

_Response status:_
```
200
```
---

**PUT** `http://localhost:8080/requests/approve-access`
* Approves access requests for a list of user IDs.
  _Request body:_
```json
[
  2
]
```

_Response body:_
```json
"OK"
```

_Response status:_
```
200
```
---

**PUT** `http://localhost:8080/requests/reject-access`
* Rejects access requests for a list of user IDs.
  _Request body:_
```json
[
  2
]
```

_Response body:_
```json
"OK"
```

_Response status:_
```
200
```
---

### UserController
**GET** `http://localhost:8080/users/get-all-users`
* Retrieves a list of all users in the system.
  _Response body:_
```json
{
  "name": "Steve Brown",
  "position": "MANAGER",
  "discipline": "DEVELOPMENT",
  "du": "MDD",
  "status": "ACTIVE",
  "lastActive": 200,
  "role": "ADMIN",
  "email": "steve.brown@example.com",
  "id": 3
}
```
_Response status:_
```
200
```
---

**GET** `http://localhost:8080/users/get-total-users`

  _Response body:_
```json
[
  5
]
```
_Response status:_
```
200
```
---

**GET** `http://localhost:8080/users/get-new-users`

_Response body:_
```json
[
  0
]
```
_Response status:_
```
200
```
---

**GET** `http://localhost:8080/users/get-total-disciplines`

_Response body:_
```json
[
  3
]
```
_Response status:_
```
200
```
---

**GET** `http://localhost:8080/users/get-disciplines-with-users`

_Response body:_
```json
{
  "TESTING": 1,
  "DEVELOPMENT": 3,
  "CREATIVE_SERVICES": 1
}
```
_Response status:_
```
200
```
---

**POST** `http://localhost:8080/users/save-user`
* Saves a new user to the system.
  _Request body:_
```json
{ 
  "name" : "John Smith", 
  "position" : "MANAGER", 
  "discipline" : "DEVELOPMENT", 
  "du" : "MDD", 
  "status" : "ACTIVE", 
  "last_active" : 30, 
  "role" : "USER", 
  "email" : "john.smith@example.com"
}
```

_Response body:_
```json
"OK"
```

_Response status:_
```
200
```
---

**GET** `http://localhost:8080/users/{id}`
* Finds a user by their unique ID.
* Path Variable: id (user ID)


_Response body:_
```json
{
  "name": "Jane Smith",
  "position": "MANAGER",
  "discipline": "CREATIVE_SERVICES",
  "du": "MDD",
  "status": "ACTIVE",
  "lastActive": 376,
  "role": "REVIEWER",
  "email": "jane.smith@example.com",
  "id": 2
}
```
_Response status:_
```
200
```
---

**PUT**  `http://localhost:8080/users/deactivate-user`
* Deactivates a list of users by their IDs.


_Request body:_
```json
[
  1,2
]
```

_Response body:_
```json
[
  {
    "name": "John Doe",
    "position": "DEVELOPER",
    "discipline": "DEVELOPMENT",
    "du": "MDD",
    "status": "INACTIVE",
    "lastActive": 100,
    "role": "USER",
    "email": "john.doe@example.com",
    "id": 1
  },
  {
    "name": "Jane Smith",
    "position": "MANAGER",
    "discipline": "CREATIVE_SERVICES",
    "du": "MDD",
    "status": "INACTIVE",
    "lastActive": 376,
    "role": "REVIEWER",
    "email": "jane.smith@example.com",
    "id": 2
  }
]
```

_Response status:_
```
200
```
---

**PUT**  `http://localhost:8080/users/changing-role?role=ADMIN`
* role (new role as a string)
* Changes the role of a list of users.


_Request body:_
```json
[
  1,2
]
```

_Response body:_
```json
[
  {
    "name": "John Doe",
    "position": "DEVELOPER",
    "discipline": "DEVELOPMENT",
    "du": "MDD",
    "status": "INACTIVE",
    "lastActive": 100,
    "role": "ADMIN",
    "email": "john.doe@example.com",
    "id": 1
  },
  {
    "name": "Jane Smith",
    "position": "MANAGER",
    "discipline": "CREATIVE_SERVICES",
    "du": "MDD",
    "status": "INACTIVE",
    "lastActive": 376,
    "role": "ADMIN",
    "email": "jane.smith@example.com",
    "id": 2
  }
]
```

_Response status:_
```
200
```
---


### CostController

**GET** `http://localhost:8080/cost/get-cost`


_Response body:_
```json
{
  "totalCosts2022": 4200,
  "deltaTotalCosts2022": 0,
  "software": 2,
  "deltaSoftware": 0,
  "trainings": 2,
  "deltaTrainings": 0
}
```
_Response status:_
```
200
```
---

### AverageUserCostController
**GET** `http://localhost:8080/average-user-cost/get-average-user-cost`


_Response body:_
```json
[
  {
    "calculation": 1479,
    "disciplineName": "CREATIVE_SERVICES",
    "averageCostsUserDiscipline": 1999
  },
  {
    "calculation": 1479,
    "disciplineName": "DEVELOPMENT",
    "averageCostsUserDiscipline": 999
  },
  {
    "calculation": 1479,
    "disciplineName": "TESTING",
    "averageCostsUserDiscipline": 700
  }
]
```
_Response status:_
```
200
```
---