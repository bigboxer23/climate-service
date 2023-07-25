[![CodeQL](https://github.com/bigboxer23/climate-service/actions/workflows/codeql.yml/badge.svg)](https://github.com/bigboxer23/climate-service/actions/workflows/codeql.yml)

## Introduction

Sprint Boot WebService wrapper around a BME680 Climate Sensor running on a Raspberry Pi

A (Spring Boot) webserver exposing an averaged reading from a BME680 (`https://shop.pimoroni.com/products/bme680-breakout`)
via a web url returning JSON formatted data.  This requires the installation of
python and the BME library via pip.

Another good resource on the sensor: `https://learn.pimoroni.com/tutorial/sandyj/getting-started-with-bme680-breakout`

## Installation

1. Install BME Library
2. `sudo apt-get install pip`
3. `sudo pip install bme680`
4. Create application.properties in `src/main/resources`
5. run `install.sh` script to transfer exe, setup service

```server.port: 443
server.ssl.key-store: keystore.p12
server.ssl.key-store-password
server.ssl.keyStoreType: PKCS12
logbackserver:192.168.0.7:5671
```

