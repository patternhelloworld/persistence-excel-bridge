# Persistence-Excel-Bridge

> Memory-efficient mass data transfer between Excel and database using Apache POI

## Table of Contents
- [Features](#Features)
- [Requirements](#Requirements)
- [Quick Start with Samples](#Quick-Start-with-Samples)
- [Quick Guide on Usage](#Quick-Guide-on-Usage)
- [Structure](#Structure)
- [Gitlab Container Registry](#Gitlab-Container-Registry)
- [Extra Information](#Extra-Information)
---

## Features

- In the case of data transfer from the database to Excel, ``Persistence-Excel-Bridge`` fetches data using pagination.
- Using Spring Events, it calculates the total data size and then creates a job queue.
  - It sets the Max ID value to ignore any data generated after this point to avoid disrupting the pagination process.
- It utilizes idle threads to perform asynchronous chunked data transfer between Excel and the database.

## Requirements

| Category          | Dependencies                                     |
|-------------------|--------------------------------------------------|
| Backend-Language  | Java 17                                          |
| Backend-Framework | Spring-Boot 3.1.2                                |
| Libraries         | JPA & QueryDSL are necessary... More in pom.xml. |

## Quick Start with Samples

- SQL
- properties
- images in reference