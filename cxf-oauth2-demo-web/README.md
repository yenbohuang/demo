Follow the following documents for how it works:

* CXF OAuth 2.0 support <http://cxf.apache.org/docs/jax-rs-oauth2.html>
* Spring security <https://docs.spring.io/spring-security/site/docs/4.2.3.RELEASE/reference/htmlsingle/>
* Spring security + embedded Jetty <https://dzone.com/articles/embedded-jetty-and-apache-cxf>
* Servlet Transport <http://cxf.apache.org/docs/servlet-transport.html>

The followings are test cases:
* Display "index.html" <http://localhost:8080/>
* Access unprotected resource <http://localhost:8080/api/demo>
* Access protected resource <http://localhost:8080/secured/secret>
* Get authorization code
<http://localhost:8080/oauth2/authorize?response_type=code&client_id=78fa6a41-aec6-4690-9237-7cd6bb6e1a84&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fdemo&scope=demo1%20demo2&state=1234>