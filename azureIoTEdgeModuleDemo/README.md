# For K8S edge preview version only

IoT edge on K8S (preview) only deploys edge agent "0.1.0-alpha", and it only supports
docker registry with port number 80. That is fixed by "1.0.8+".
 
* https://docs.microsoft.com/en-us/azure/iot-edge/how-to-install-iot-edge-kubernetes
* https://github.com/Azure/iotedge/issues/1191

A workaround is to bind local docker registry at port 80 as follows:

```
docker run -d -p 80:5000 --restart=always --name registry registry:2
```

You don't need this for a normal IoT edge device.