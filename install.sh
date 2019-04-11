#!/usr/bin/env bash

ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "git clone https://github.com/pimoroni/bme680"
ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "sudo python bme680/library/setup.py install"
ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "mkdir /home/pi/com"
ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "mkdir /home/pi/com/bigboxer23"
ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "mkdir /home/pi/com/bigboxer23/climate-service"
ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "mkdir /home/pi/com/bigboxer23/climate-service/1.0.0"
ssh -t pi@192.168.0.24 -o StrictHostKeyChecking=no "cp /home/pi/bme680/examples/read-all.py /home/pi/com/bigboxer23/climate-service/1.0.0"
scp -o StrictHostKeyChecking=no -r keystore.p12 pi@homecontroller:/home/pi/

#Still have to install these manually for now...
#cd /home/pi
# sudo nohup java -jar /home/pi/com/bigboxer23/climate-service/1.0.0/climate-service-1.0.0.jar