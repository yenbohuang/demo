# Summary

This project is for testing "ARM mbed Device Connector" free-tier services. Create a free-tier account on <https://connector.mbed.com/>.

# This demo does not work yet

I am stuck here and don't know how to resolve it yet.

    SEVERE: Could not create signature.
    java.security.InvalidKeyException: No installed provider supports this key: sun.security.ec.ECPrivateKeyImpl
	    at java.security.Signature$Delegate.chooseProvider(Unknown Source)
    	at java.security.Signature$Delegate.engineInitSign(Unknown Source)
	    at java.security.Signature.initSign(Unknown Source)
	    at org.eclipse.californium.scandium.dtls.CertificateVerify.setSignature(CertificateVerify.java:172)
	    at org.eclipse.californium.scandium.dtls.CertificateVerify.<init>(CertificateVerify.java:84)
	    at org.eclipse.californium.scandium.dtls.ClientHandshaker.receivedServerHelloDone(ClientHandshaker.java:550)
	    at org.eclipse.californium.scandium.dtls.ClientHandshaker.doProcessMessage(ClientHandshaker.java:250)
	    at org.eclipse.californium.scandium.dtls.Handshaker.processMessage(Handshaker.java:373)
	    at org.eclipse.californium.scandium.DTLSConnector.processOngoingHandshakeMessage(DTLSConnector.java:780)
	    at org.eclipse.californium.scandium.DTLSConnector.processDecryptedHandshakeMessage(DTLSConnector.java:767)
	    at org.eclipse.californium.scandium.DTLSConnector.processHandshakeRecordWithConnection(DTLSConnector.java:751)
	    at org.eclipse.californium.scandium.DTLSConnector.processHandshakeRecord(DTLSConnector.java:687)
	    at org.eclipse.californium.scandium.DTLSConnector.receiveNextDatagramFromNetwork(DTLSConnector.java:422)
	    at org.eclipse.californium.scandium.DTLSConnector.access$200(DTLSConnector.java:101)
	    at org.eclipse.californium.scandium.DTLSConnector$2.doWork(DTLSConnector.java:324)
	    at org.eclipse.californium.scandium.DTLSConnector$Worker.run(DTLSConnector.java:1433)


# Prepare "mbeddemo.properties" file

"mbeddemo.properties" contains my account information; create your own file and make some code changes.

    device1.serverCertFilePath=
    device1.clientCertFilePath=
    device1.privateKeyFilePath=
    accesskey1.name=
    accesskey1.key=

# Private key

A private key should be look like this:

    -----BEGIN PRIVATE KEY-----
    ...
    -----END PRIVATE KEY-----

No conversion is needed.

See details on <http://stackoverflow.com/questions/15344125/load-a-rsa-private-key-in-java-algid-parse-error-not-a-sequence>.

# mbed references

* ARM mbed Device Connector <https://connector.mbed.com/>
* Getting started with mbed Device Connector <https://docs.mbed.com/docs/getting-started-with-mbed-device-connector/en/latest/>
* ARM mbed Device Connector <https://docs.mbed.com/docs/mbed-device-connector-web-interfaces/en/latest/>
* mbed web application - example <https://github.com/ARMmbed/mbed-webapp-example>