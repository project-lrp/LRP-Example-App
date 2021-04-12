# LRP Example Application

The example application using LRP service through AIDL. An Android application can call APIs **startParInf()** and **reduceDozeAndSchedule()** provided by the LEP daemon to reduce LTE network latency.

## Using LRP to reduce LTE network latency
1. Setup the AIDL of LRP in the application: https://developer.android.com/guide/components/aidl
2. In the application, before sending the packets, the application need to call **startParInf()** to ensure the LRP updates configurations. Whenever a data packet is sent, the application calls **reduceDozeAndSchedule(currentTimestamp,estimatedDelayOfNextPacket)** to reduce latency for the next packet.
3. Before running the application requiring LRP service, please run the LRP Daemon application in the background at the same time.
