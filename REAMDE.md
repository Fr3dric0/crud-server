Flowerpot Server
================


## 1 Setup
This guide denotes the minimum 
configuration to run the server.

### 1.1. Installation
Clone the repo, create a `server.properties` file with the following content.

```properties
##
# Basic configuration
##
port = 8080
host = 127.0.0.1

##
# Static configuration
##
static.root = /static
static.url = /static

##
# JWT token config
##
tokenSecret = 56cyDiMYfI08OtuZRItKSjhRp7hirZ80U5HskfHpRlXyGKzx9YbrDH8D
```

> You can access these values through the `Configuration`-class

### 1.2. Application configuration
Make `Application`-class extend `Server`. 
In the `main`-method run `setup()`.

```java
public class Application extends Server {
    
    public static void main(String[] args) {
        // Will setup and save the necessary configurations.
        // You can access these config values through `Configuration.getProperties()`
        setup(Application.class.getResource("/server.properties"));
        
        Application app = new Application();
        try {
            app.start(); // Starts the server
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
}
```

With the minimum setup completed, 
it is now time to implement your own _controllers_
and _routes_

#### 1.2.1. Custom controllers
Controllers are the objects which handles 
http-requests for the given _urls_ (_ref. 1.2.2_).

You have two primary controllers. 
The default request/response `Controller`
and the `StaticController`, 
which serves static content from the designated url.

> For routes which needs authentication, you can replace `Controller`
> with `AuthController`.

The `StaticController` only needs to be instantiated 
and registered to a route. `Controller` however, should
be extended by a child-class.

To implement your own logic for each request, 
simply override the methods. I.e. to implement logic
for `POST`, override `post(Request req, Response res) ...`.

Methods which have not been overriden, 
will return `HTTP 405 Method Not Allowed`.

##### Example: Custom Controller
Quick example on how to create a custom controller
which accepts `GET` requests 
(_how to register a controller to a route, is covered in 2.2_)

```java
public class IndexController extends Controller {
    
    @Override
    public String get(Request req, Response res) {
        if (!req.getQueries().containsKey('type')) {
            throw new ForbiddenException("Custom error message");
            // Will return {"error": "Custom error message"} to caller
        }
        
        HelloWorldModel hw = new HelloWorldModel("Hello world");
        
        // gson is directly accessible through Controller
        return gson.toJson(hw, HelloWorldModel.class);
    }
    
}
```

#### 1.2.2 Register Urls
Each controller should at least have a single url
registered. Our `Router`-object is responsible for this logic.

To add your own routes, simply override `urls(Router router) ...`,
and call `register` on the `router`-parameter.

##### Example: Register Controllers to urls
The static `urls`-method, is responsible for attaching routes
to controllers.

Here we attach `IndexController` to the route `"/api"`
```java
public class Application extends Server {
    
    public static void main(String[] args) {
        setup(Application.class.getResource("/server.properties"));
        
        Application app = new Application();
        
        try {
            app.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    // Routes is registered here
    @Override
    public void urls(Router router) {
        //               Uri        Controller
        router.register("/api", new IndexController());
        
        // Registers static content to be 
        // served on the url "/static" 
        // with the folder location "/static" (located in "resources" in our case)
        // 
        // This is also easily implemented, just by adding `static.root`
        // and `static.url` to `server.properties`
        router.register("/static", new StaticController(
            Main.class.getClass().getResource("/static") 
        ));
    }
}
```

### 1.3. Run server
The application should now be ready to run.
Use your favorite IDE, or use `gradlew` to run start the app.


## 2 Authentication
The server support some basic authentication, 
like JWT-authentication.

You implement it by extending `AuthController` (instead of `Controller`).
This will enable you to add authentication _filters_ to the controller.
In essence it's the filters who ensures only authenticated requests pass through.
Thus, if no filters are added, no authentication is applied.

> One can ignore authentication on specific methods in the controller by
> calling `ignoreMethods(String... methods)` (see example bellow)

### 2.1 Json Web Token Authentication (JWT-auth)
This section will give a walk-through on how to implement _JWT-auth_. 
The `JWTAuthController` is located in `no.fredrfli.http.controller` for reference
and usage.

#### 2.1.1. Create a _constructor_ and run `addFilter`
The first thing we will do is to create our authentication controller,
extract the `tokenSecret` from `server.properties`, 
and add a filter to do the actual authentication and authorization 
(_Note. The filter will be implemented later_).

```java
public class JWTAuthController extends AuthController {
    
    public JWTAuthController() {
        super();
        
        String secret = "";
        // The `tokenSecret` is expected to be stored in `server.properties`
        if (Configuration.getProperties().containsKey("tokenSecret")) {
            secret = (String) Configuration.getProperties().get("tokenSecret");
        }
        
        // JWTFilters needs a token secret to sign the tokens
        addFilter(new JWTFilter(secret));
    }
}
```

#### 2.1.2 Create JWTFilter
The core behind the authentication controllers is it's filters.
Generally, filters can be implemented in two ways.

1. "Normal" java class
2. Lambda-function

For our JWTFilter we will create a java class, however if you would
like to use a lambda function, it is created in the following manner.

```java
// Expects it to return true/false, or throw an HttpException
Filterable f = (req, res) -> req.getContentType() == MimeType.APPLICATION_JSON;
addFilter(f); // Can also be done inline
```

**JWTFilter**
The server has a JWT object which will be used in this example (checkout `no.fredrfli.http.auth.JWT`).
It has two core methods `create` and `verify`.

The JWTFilter should have three steps. 
Validate that the authorization-header exists, 
ensure the token is prefixed with 'bearer ' (can be ignored),
and has a valid token.

We will throw an HttpException (`UnauthorizedException` or `ForbiddenException`),
should any of these steps fail.

You can optionally return `false` in `canActivate`, 
which will give a generic `403 Forbidden`-response.

```java
public class JWTFilter extends Filter {
    private String secret;
    private String prefix = "bearer";
    private JWT jwt;

    public JWTFilter(String secret) {
        Objects.requireNonNull(secret, "Missing token-secret");

        this.secret = secret;
        jwt = new JWT(this.secret);
    }

    /**
     * Filter method.
     * Will check if the authorization header exists,
     * check if the token is prefixed with bearer,
     * and ensures the token is valid.
     *
     * @return boolean
     * */
    @Override
    public boolean canActivate(Request req, Response res) {
        requireTokenExistence(req);
        requireTokenBearer(req);
        requireValidToken(req);

        return true;
    }

    private void requireTokenExistence(Request req) {
        if (!req.getHeaders().containsKey("authorization")) {
            throw new UnauthorizedException("Missing authorization header");
        }
    }

    /**
     * Ensures a token is prefixed with 'bearer '
     *
     * */
    private void requireTokenBearer(Request req) {
        String token = req.getHeaders().get("authorization");

        if (!token.toLowerCase().startsWith(prefix)) {
            throw new UnauthorizedException(
                    "Authorization header must be prefixed with '" + prefix + "'");
        }
    }

    /**
     * Uses JWT.verify() to validate the provided token
     *
     * */
    private void requireValidToken(Request req) {
        String token = req.getHeaders()
                .get("authorization")
                .substring(prefix.length() + 1);

        try {
            jwt.verify(token);
        } catch(SignatureException se) {
            throw new ForbiddenException("Invalid token");
        } catch (ExpiredJwtException ie) {
            throw new ForbiddenException("Expired token");
        }
    }
}
```

That should be it. You should now be able to implement get, post, put, ... _methods_
which will be protected behind JWT-auth.

You add the controller to the urls just like any other controllers.

#### 2.1.3 Extra: Ignore methods from authentication
This is a quick way to ignore HTTP-methods from authentication.
We will do it in the same location where we register the routes,
but you can also just call `ignoreMethods()` directly in your controller's
constructor

```java
public void urls(Router router) {
    router.register("/secure" , new ProectedController()
                                    .ignoreMethods("post", "Patch"));
}
```

