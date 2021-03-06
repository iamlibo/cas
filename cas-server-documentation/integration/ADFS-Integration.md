---
layout: default
title: CAS - ADFS Integration
---

# Overview

The integration between the CAS Server and ADFS delegates user authentication from CAS Server 
to ADFS, making CAS Server a WS-Federation client. 
Claims released from ADFS are made available as attributes to CAS Server, and by extension CAS Clients.

Support is enabled by including the following dependency in the WAR overlay:

```xml
<dependency>
  <groupId>org.apereo.cas</groupId>
  <artifactId>cas-server-support-wsfederation-webflow</artifactId>
  <version>${cas.version}</version>
</dependency>
```

You may also need to declare the following Maven repository in your 
CAS Overlay to be able to resolve dependencies:

```xml
<repositories>
    ...
    <repository>
        <id>shibboleth-releases</id>
        <url>https://build.shibboleth.net/nexus/content/repositories/releases</url>
    </repository>
    ...
</repositories>
```


## WsFed Configuration

Adjust and provide settings for the ADFS instance, and make sure you have obtained the ADFS signing certificate
and made it available to CAS at a location that can be resolved at runtime.

To see the relevant list of CAS properties, please [review this guide](../installation/Configuration-Properties.html).

## Encrypted Assertions

CAS is able to automatically decrypt SAML assertions that are issued by ADFS. To do this, 
you will first need to generate a private/public keypair:

```bash
openssl genrsa -out private.key 1024
openssl rsa -pubout -in private.key -out public.key -inform PEM -outform DER
openssl pkcs8 -topk8 -inform PER -outform DER -nocrypt -in private.key -out private.p8
openssl req -new -x509 -key private.key -out x509.pem -days 365

# convert the X509 certificate to DER format
openssl x509 -outform der -in x509.pem -out certificate.crt
```

Configure CAS to reference the keypair, and configure the relying party trust settings
in ADFS to use the `certificate.crt` file for encryption.
 
## Modifying ADFS Claims
The WsFed configuration optionally may allow you to manipulate claims coming from ADFS but 
before they are inserted into the CAS user principal. For this to happen, you need
to put together an implementation of `WsFederationAttributeMutator` that changes and manipulates ADFS claims:

```java
package org.apereo.cas.support.wsfederation;

@Component("wsfedAttributeMutator")
public class WsFederationAttributeMutatorImpl implements WsFederationAttributeMutator {
    public void modifyAttributes(...) {
        ...
    }
}
```

Finally, ensure that the attributes sent from ADFS are available and mapped in
your `attributeRepository` configuration.

## Handling CAS Logout

An optional step, the `casLogoutView.html` can be modified to place a link to ADFS's logout page.

```html
<a href="https://adfs.example.org/adfs/ls/?wa=wsignout1.0">Logout</a>
```

## Per-Service Relying Party Id

In order to specify a relying party identifier per service definition, adjust your service
registry to match the following:

```json
{
  "@class" : "org.apereo.cas.services.RegexRegisteredService",
  "serviceId" : "^https://.+",
  "name" : "sample service",
  "id" : 100,
  "properties" : {
    "@class" : "java.util.HashMap",
    "wsfed.relyingPartyIdentifier" : {
      "@class" : "org.apereo.cas.services.DefaultRegisteredServiceProperty",
      "values" : [ "java.util.HashSet", [ "custom-identifier" ] ]
    }
  }
}
```
