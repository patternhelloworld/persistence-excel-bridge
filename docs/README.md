# DOC : Persistence Excel Bridge Docs
## Overview

* This Doc module is for testing "persistence-excel-bridge"

## Dependencies

| Category          | Dependencies                               |
|-------------------|--------------------------------------------|
| Backend-Language  | Java 17                                    |
| Backend-Framework | Spring Boot 3.1.2                          |
| Main Libraries    | Spring Security Authorization Server 1.2.3 |
| Package-Manager   | Maven 3.6.3 (mvnw, Dockerfile)             |
| RDBMS             | Mysql 8.0.17                               |

## Implementation

#### Import the SQL file in the ``mysql`` folder.

#### The API information is found on ``http://localhost:8505/docs/api-app.html``, managed by Spring Rest Doc 



### Running this App with Docker
* Use the following module for Blue-Green deployment:
  * https://github.com/Andrew-Kang-G/docker-blue-green-runner
* The above module references this app's Dockerfile and the entrypoint script in the .docker folder.