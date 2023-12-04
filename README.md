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
    "id": 5,
    "name": "JetBrains",
    "cost": 1444.0,
    "availability": 120,
    "expirationDate": "2023-10-04"
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
    "id": 1,
    "name": "Postman",
    "cost": 666.0,
    "unusedPeriod": 13
  },
  {
    "id": 2,
    "name": "Adobe Suite",
    "cost": 1313.0,
    "unusedPeriod": 30
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
    "logo": "[100, 101, 102, 97, 117, 108, 116, 45, 108, 111, 103, 111, 45, 98, 97, 115, 101, 54, 52, 45, 118, 97, 108, 117, 101]",
    "licenseName": "Visual Studio",
    "description": "Visual Studio API",
    "cost": 800.0,
    "currency": "USD",
    "licenseDuration": 10,
    "durationUnit": "MONTH",
    "seatsAvailable": 10,
    "seatsTotal": 250,
    "isActive": false,
    "expirationDate": "2022-01-10",
    "licenseType": "SOFTWARE",
    "isRecurring": false
  },
  {
    "logo": "[100, 101, 102, 97, 117, 108, 116, 45, 108, 111, 103, 111, 45, 98, 97, 115, 101, 54, 52, 45, 118, 97, 108, 117, 101]",
    "licenseName": "Sketch",
    "description": "Sketch API",
    "cost": 1200.0,
    "currency": "USD",
    "licenseDuration": 15,
    "durationUnit": "MONTH",
    "seatsAvailable": 10,
    "seatsTotal": 250,
    "isActive": false,
    "expirationDate": "2022-02-20",
    "licenseType": "SOFTWARE",
    "isRecurring": false
  }
]
```
* Returns: List of `LicenseSummaryDTO` representing all licenses.

---
**POST** http://localhost:8080/license/add-new-license
_Request body:_

```json
{
  "licenseName": "Sample License 1",
  "description": "This is a sample license description with a call-to-action.",
  "website": "www.sambplelicense.com",
  "cost": 6.0,
  "currency": "USD",
  "availability": 365,
  "seatsTotal": 100,
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
**GET** `http://localhost:8080/license/get-license?name=Postman`

_Parameters:_
* name (String): Mandatory parameter to get license data from database. If License name has whitespace, It should be replaced by "%20" 

_Response body:_
```json
{
    "licenseId": 1,
    "licenseName": "Postman",
    "website": null,
    "description": "Postman API",
    "logo": null,
    "credentials": [
        {
            "username": "john.doe@endava.com",
            "password": "johndoe",
            "role": "USER"
        }
    ],
    "cost": 666.0,
    "currency": "USD",
    "availability": 365,
    "isRecurring": false,
    "seatsTotal": 250,
    "seatsAvailable": 10,
    "licenseType": "SOFTWARE",
    "expiresOn": "31-Oct-2023"
}

```
* Returns HTTP status OK and License data from database 
  _Response status:_
```
200
```

**PUT** http://localhost:8080/license/edit-license
_Request body:_
```json
{
  "licenseId" : 1,
  "licenseName": "Edited Postman License",
  "description": "This is a sample license description with a call-to-action.",
  "website" : "www.sambplelicense.com",
  "cost" : 6.0,
  "currency": "USD",
  "availability": 365,
  "seatsTotal": 100,
  "seatsAvailable" : 98,
  "isActive": true,
  "expiresOn": "12-May-2023",
  "licenseType": "TRAINING",
  "isRecurring": "true",
  "credentials" : [
    {
      "username" : "john.doe@endava.com",
      "password" : "johndoe"
    },
    {
      "username" : "jane.smith@endava.com",
      "password" : "JaneSmith"
    }
  ],
  "logo" : null
}
```
* Edit License data in database
* Returns HTTP status OK 
  _Response status:_
```
200
```

_Response body:_
```json
"OK"
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
    "requestDate": "05-Dec-2023 00:00",
    "startOfUse": "06-Jun-2023",
    "username": "Jane Smith",
    "discipline": "CREATIVE_SERVICES"
  },
  {
    "requestId": 3,
    "status": "PENDING",
    "app": "JetBrains",
    "requestDate": "05-Dec-2023 00:00",
    "startOfUse": "06-Jun-2023",
    "username": "Steve Brown",
    "discipline": "DEVELOPMENT"
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
[
  {
    "name": "Admin User",
    "position": null,
    "discipline": null,
    "du": null,
    "status": null,
    "lastActive": null,
    "credential": {
      "username": "admin@example.com",
      "password": "$2a$10$mL.haPLgT6l0Z9KYetTQuupR0.OhUKC80JDTffxkblBuv8yCU/cdm",
      "role": "ADMIN"
    },
    "id": 5
  },
  {
    "name": "John Doe",
    "position": "DEVELOPER",
    "discipline": "DEVELOPMENT",
    "du": "MDD",
    "status": "ACTIVE",
    "lastActive": 100,
    "credential": {
      "username": "john.doe@endava.com",
      "password": "johndoe",
      "role": "USER"
    },
    "id": 1
  },
  {
    "name": "Jane Smith",
    "position": "MANAGER",
    "discipline": "CREATIVE_SERVICES",
    "du": "MDD",
    "status": "ACTIVE",
    "lastActive": 376,
    "credential": {
      "username": "jane.smith@endava.com",
      "password": "JaneSmith",
      "role": "USER"
    },
    "id": 2
  }
]
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
  "DEVELOPMENT": 2,
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
  "name": "John Doe",
  "position": "DEVELOPER",
  "discipline": "DEVELOPMENT",
  "du": "MDD",
  "status": "ACTIVE",
  "lastActive": 100,
  "credential": {
    "username": "john.doe@endava.com",
    "password": "johndoe",
    "role": "USER"
  },
  "id": 1
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
    "credential": {
      "username": "john.doe@endava.com",
      "password": "johndoe",
      "role": "USER"
    },
    "id": 1
  },
  {
    "name": "Jane Smith",
    "position": "MANAGER",
    "discipline": "CREATIVE_SERVICES",
    "du": "MDD",
    "status": "INACTIVE",
    "lastActive": 376,
    "credential": {
      "username": "jane.smith@endava.com",
      "password": "JaneSmith",
      "role": "USER"
    },
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
    "credential": {
      "username": "john.doe@endava.com",
      "password": "johndoe",
      "role": "ADMIN"
    },
    "id": 1
  },
  {
    "name": "Jane Smith",
    "position": "MANAGER",
    "discipline": "CREATIVE_SERVICES",
    "du": "MDD",
    "status": "INACTIVE",
    "lastActive": 376,
    "credential": {
      "username": "jane.smith@endava.com",
      "password": "JaneSmith",
      "role": "ADMIN"
    },
    "id": 2
  }
]
```

_Response status:_
```
200
```
---

**GET** `http://localhost:8080/users/get-users-overview`

_Response body:_
```json
{
  "totalUsers": 5,
  "totalDisciplines": 3,
  "deltaUsers": 0,
  "disciplines": [
    {
      "name": "CREATIVE_SERVICES",
      "numberOfUsers": 1
    },
    {
      "name": "TESTING",
      "numberOfUsers": 1
    },
    {
      "name": "DEVELOPMENT",
      "numberOfUsers": 2
    }
  ]
}
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
  "totalCosts2023": 6427,
  "deltaTotalCosts2023": 2227,
  "software": 3,
  "deltaSoftware": 1,
  "trainings": 3,
  "deltaTrainings": 1,
  "costsPerMonth": [
    {
      "month": "Dec 23",
      "value": 999
    },
    {
      "month": "Jun 23",
      "value": 1444
    },
    {
      "month": "May 23",
      "value": 6
    },
    {
      "month": "Nov 23",
      "value": 1313
    },
    {
      "month": "Oct 23",
      "value": 666
    },
    {
      "month": "Sep 23",
      "value": 1999
    }
  ]
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
{
  "calculation": 1480,
  "disciplineCosts": [
    {
      "disciplineName": "CREATIVE_SERVICES",
      "averageCostsUserDiscipline": 1999
    },
    {
      "disciplineName": "DEVELOPMENT",
      "averageCostsUserDiscipline": 999
    },
    {
      "disciplineName": "TESTING",
      "averageCostsUserDiscipline": 700
    }
  ]
}
```
_Response status:_
```
200
```
---