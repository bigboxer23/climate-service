#!/usr/bin/env python
import time

# These calibration data can safely be commented
# out, if desired.

print('Calibration data:')
print('\n\nInitial reading:')

print('\n\nPolling:')
try:
    while True:
        print('19.73 C,985.42 hPa,39.04 %RH,10370 Ohm')

        time.sleep(1)

except KeyboardInterrupt:
    pass