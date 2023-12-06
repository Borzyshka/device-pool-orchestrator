# Device Pool Orchestrator

## General

This is a simple web application designed to create Appium Servers and book devices for regressions.

## Prerequisites

The following software should be installed

* [Node.js and NPM](https://nodejs.org/en/download)
* [Android SDK](https://developer.android.com/studio)
    * [Android SDK Build Tools](https://developer.android.com/tools#tools-build)
    * [Android SDK Platform Tools](https://developer.android.com/tools#tools-platform)
* [Maven](https://maven.apache.org/download.cgi)
* [Java 17](https://jdk.java.net/)

## Local Runs

To run application locally:

1. run mvn clean package on the source folder
2. open directory with device-pool-{versionId}.jar file
3. open CMD terminal
4. run the following command `java -jar device-pool-{versionId}.jar` with parameters listed below

| Param                        | Required | Command Line Format            | Description                         | Default value |
|------------------------------|----------|--------------------------------|-------------------------------------|---------------|
| server.port                  | NO       | --server.port                  | custom port to start application at | 8080          |
| app.appium.appium-path       | YES      | -Dapp.appium.appium-path       | path to Appium                      | none          |
| app.appium.driver-executable | YES      | -Dapp.appium.driver-executable | path to NodeJs executable           | none          |

Example

````
java -jar device-pool-{versionId}.jar --server.port=8090 -Dapp.appum.appium-path="C:/User/AppData/Roaming/npm/node_modules/appium
- Dapp.appium.driver-executable="C:/Program Files/nodejs/node.exe"
````

5. wait for application to start
6. application will run on the `http://127.0.0.1:{port}` endpoint. Default port is 8080

Note: instead of providing `-D.app.server.driver-executable` and `-Dapp.server.appium-path` in the command, they can
be specified in `application.yaml` configuration file

## Usage

#### Acquiring device/server bundle

Application allows to reserve one or several device/server bundles. Each bundle includes Device information, server host
and additional device capabilities, such as uniques SystemPort, ChromiumDriverPort and etc.

To acquire bundles for test execution execute `POST http://127.0.0.1:8080/v1/bundles/reservations?quantity=1`, where
quantity can be an integer number eqals or higher than one. If quantity is omitted, one bundle will be reserved
In the response device udid and server URL are returned.

```json
{
  "bundles": [
    {
      "reservationId": "9cbebc40-dc37-44f8-b9d1-570b59e5c299",
      "deviceInfo": {
        "udid": "9cbebc40-dc37-44f8-b9d1-570b59e5c299"
      },
      "serverHost": "http://127.0.0.1:4723",
      "capabilities": {
        "SystemPort": 1234,
        "adbForwardPort": 4321
      }
    }
  ]
}
```

If less amount of devices are available, a shorter list of bundles will be returned.
If none devices are free for 2 minutes ALL_OBJECTS_BOOKED error will return.

#### Returning device to a pool

To recycle bundle to a pool execute `DELETE http://127.0.0.1:8080/v1/bundles/reservations/{reservationId}`.
This will return device to a device pool and destroy the appium server.

## Documentation

OpenAPI documentation is available at  `{application_url}/openapi.html` or
in `src/main/resources/static/openapi.yaml`
