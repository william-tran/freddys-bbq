# Freddy's BBQ Joint

## Overview

This demo is an example of using Pivotal SSO and Spring Cloud Services in a microservice architecture.

![diagram](docs/diagram.gif "Diagram")

Here are the actors:
- Freddy, owner of Freddy’s BBQ Joint, the best ribs in DC
- Frank, Freddy’s most important customer (and the most powerful man in the world)
- The Developer, works for Frank and wants to impress him with a side project (this app)

This is the use case:
- Give Frank the ability to see the menu online and place orders
- Give Freddy the ability to manage the menu and close orders

The commits in this repo outline how to create, secure, and test the security of microservices, and add support for Pivotal SSO and Spring Cloud Services, one step at a time. All the development and testing can run locally, and about 2/3 the way through we start pushing to CF. We start with a single application and evolve the architecture to 2 UI applications and 2 API applications.

## Deploy to Cloud Foundry

In order to deploy Freddy's BBQ microservices to Cloud Foundry you must have Administrator access. The steps to deploy Freddy's BBQ into your Cloud Foundry environment are:

1. Install dependent Services
2. Create service instances for microservices to bind to
3. Set CF_TARGET for all applications (NOTE: Only if TLS is self-signed cert)
4. Deploy applications to Cloud Foundry
5. Setup authorization scopes and users (Freddy & Frank)
6. Add authorization scopes to microservices in SSO management dashboard
7. Verify application works

### Install Dependent Services

Freddy's BBQ requires the installation of the following Pivotal Cloud Foundry products:

* [SSO for PCF](https://network.pivotal.io/products/p-identity)
* [MySQL for PCF](https://network.pivotal.io/products/p-mysql)
* [RabbitMQ for PCF](https://network.pivotal.io/products/pivotal-rabbitmq-service)
* [Spring Cloud Services for PCF](https://network.pivotal.io/products/p-spring-cloud-services)

Follow these instructions on [how to add products to PCF Ops Manager](http://docs.pivotal.io/pivotalcf/1-8/customizing/add-delete.html).

It is important to note that installation of the [SSO for PCF](https://network.pivotal.io/products/p-identity) is not sufficient to enable creation of service instances that applications can bind to. In order to create a service in the Cloud Foundry marketplace you must create a new service plan. Please the directions in the [SSO for PCF docs](http://docs.pivotal.io/p-identity/1-8/getting-started.html) to create a new service plan. The rest of this page assumes the `Auth Domain` for your SSO serice plan is `auth`.

After installing these products and setting up an SSO provider, you can verify that they are available for creating service instances by running `cf marketplace` in a terminal. You should see the following services available in the PCF marketplace:

```
service                       plans
p-circuit-breaker-dashboard   standard
p-config-server               standard
p-identity                    auth
p-mysql                       100mb
p-rabbitmq                    standard
p-service-registry            standard
```

### Create Service instances

The following service instances must be created before pushing the application instances involved in running Freddy's BBQ:

* sso
* mysql
* service-registry
* circuit-breaker
* config-server

To create all of these service instances run the following commands:

```
cf create-service p-identity auth sso
cf create-service p-mysql 100mb mysql
cf create-service p-service-registry standard service-registry
cf create-service p-circuit-breaker-dashboard standard circuit-breaker
cf create-service p-config-server standard config-server
```

Some of these services are created asynchronously. In order to verify that they were created successfully you can run the following command and look for output that includes `create succeeded` on each line in the `last operation` column:

```
$ cf services
name              service                       plan       last operation
circuit-breaker   p-circuit-breaker-dashboard   standard   create succeeded
config-server     p-config-server               standard   create succeeded
mysql             p-mysql                       100mb      create succeeded
service-registry  p-service-registry            standard   create succeeded
sso               p-identity                    auth       create succeeded
```

### Set CF_TARGET

If your PCF environment is deployed with self-signed certificates to enable TLS for application domains then you must set CF_TARGET to the API endpoint for PCF. Run the following command as a Cloud Foundry Administrator to set CF_TARGET to the API endpoint for all applications deployed into PCF environment:

```
cf srevg '{"CF_TARGET":"https://api.mypcf.example.com"}'
```
### Deploy Applications

The file `manifest.yml` contains the configuration for all of the application instances to be deployed for Freddy's BBQ. In order to deploy the applications to PCF run the following command from the folder containing `manifest.yml`:

```
cf push
```

### Setup Authorization Scopes and Users

There are 2 scripts in the `/uaa` directory that will create the necessary authorized scopes and users for the Freddy's BBQ applications:

```
/uaa
  - zoneadmin.sh
  - zoneusers.sh
```

These scripts need some environment variables defined in order to run against Cloud Foundry UAA endpoints.

```
# UAA_ENDPOINT eg uaa.mypcf.example.com
# ADMIN_CLIENT_ID eg admin
# ADMIN_CLIENT_SECRET get this from Ops Manager Elastic Runtime UAA "Admin Client Credentials"
# IDENTITY_ZONE_ID this is the GUID of the identity zone which is the first GUID in the URI for any page in the `sso` service instance dashboard
# ZONEADMIN_CLIENT_ID pick a name for the admin client in the zone
# ZONEADMIN_CLIENT_SECRET
# ZONE_ENDPOINT the auth domain URL from SSO for PCF eg auth.login.mypcf.example.com
```

After you set these environment variables, run the following commands:

```
./uaa/zoneadmin.sh
./uaa/zoneusers.sh
```

This should run through successfully creating 2 users, `freddy` the administrator and `frank` the user. It should also create four authorization scopes that users and applications can be configued to access: `menu.read`, `menu.write`, `order.admin`, and `order.me`.

### Authorize Applications to Scopes

In order for the `admin` and `customer` portals of Freddy's BBQ application to access the dependent microservices `menu-service` and `order-service` you must configure the 'scopes' in the `sso` service dashboard. In order to access the `sso` dashboard, run the following command and go to the URL listed in `Dashboard` property:

```
$ cf service sso

Service instance: sso
Service: p-identity
Bound apps: customer-portal,menu-service,admin-portal,order-service
Tags:
Plan: auth
Description: Single Sign-On as a Service
Documentation url: http://docs.pivotal.io/p-identity/index.html
Dashboard: https://p-identity.mypcf.example.com/dashboard/identity-zones/{ZONE_GUID}/instances/{INSTANCE_GUID}/
...
```
On the dashboard, add all of the scopes for Freddy's BBQ application instances: `menu.read`, `menu.write`, `order.admin`, and `order.me`.

### Verify Application Works

Go to the Customer and Admin Portals via a web browser. Use different browser sessions for each so that you can authenticate with the appropriate user.

* For Customer Portal [http://customer-portal.mypcf.example.com] authenticate as `frank`
* For Admin Portal [http://admin-portal.mypcf.example.com] authenticate as `freddy`

You should now be able to order food from the menu as `frank` and add items for sale to the menu as `freddy`.
