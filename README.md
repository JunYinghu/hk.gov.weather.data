# HK system API Testing Project

# Introduction

This repo holds the code for assignment <HongKong Observatory> API <Current Weather Report>testing

It is divided into smoke , destructive test covering api basic negative and positive as well as performance testing All
test cases runner has been tested on Window

## Table of Contents

- [Prerequisites](#Prerequisites)
- [Technical Stack](#Technical-Stack)
- [Installation and Pre-Config](#Clone-and-PreConfig)
    - [Clone](#Clone)
    - [Pre-Config Language](#PreConfig-Language)
- [Test Suite Run](#Test-Suite-Run)
    - [Docker](#Docker)
    - [MAVEN](#Maven-Run)
- [Allure Test Report](#Allure-Test-Report)
- [Test Categories / Coverage](#Test-Categories/Coverage)

## Prerequisites

You will need:

- java 11 and above
- maven 3.6.3
- Docker 19.03.13

## Technical Stack

Reference to following libraries / frameworks

- Rest Assured 4.2.0
- Json Schema validator 4.3.2
- Testng 7.3.0
- Allure Report

## Clone and PreConfig

### Clone

- Simply clone report use your own way from
  https://github.com/JunYinghu/appium-test-automation.git

### PreConfig Language

You may config the language. It is very flexible way to give language you want

```
cd hk.gov.weather.data/src/test/resources/testng/testng_suite.xml
<parameter name="language" value="sc"/>
```

## Test Suite Run

- Run desired command as the following approaches

### Docker

#### Run Smoke Test

```
    cd hk.gov.weather.data/CICD   
    docker-compose run smoketestsuit
```

#### Run All Test

```
    cd hk.gov.weather.data/CICD   
    docker-compose run regressiontestsuit   
```

### Maven Run

#### Run Test Suit

```
    cd hk.gov.weather.data
    mvn clean test 
```

## Allure Test Report

- After test cases are executed completely, you may run the following command to see test result.
- Start Allure Server and View Report

```
   cd hk.gov.weather.data 
   mvn io.qameta.allure:allure-maven:serve
```

- Generating Allure report in <target> folder

```
   cd hk.gov.weather.data 
   mvn io.qameta.allure:allure-maven:report
   cd target\site\allure-maven-plugin\index.html
```

## Test Categories/Coverage

### Smoke API Test

#### Mainly verification on system returning correct response as specification

- Positive Test - Header Validation
- Positive Test - Response Code, Content Type, Response status line
- Positive Test - Json Schema validation (details refer to )
- Positive Test - Language Validation

### Destructive API Test

#### Mainly verification on system dealing with exception request

- Negative Test - Not Allowed Method
- Negative Test - Invalid QueryParameter verification combination
- Negative Test - Invalid PathParameter verification combination
