Sprint Boot WebService wrapper around a BME680 Climate Sensor running on a Raspberry Pi

A (Spring Boot) webserver exposing an averaged reading from a BME680 (`https://shop.pimoroni.com/products/bme680-breakout`)
via a web url returning JSON formatted data.

Running `install.sh` will install and utilize pimoroni's python code to read from this sensor.  

Properties to define:
```server.port: 443
server.ssl.key-store: keystore.p12
server.ssl.key-store-password
server.ssl.keyStoreType: PKCS12
logbackserver:``