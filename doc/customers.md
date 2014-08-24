# Customers

For detailed information about the Customers model, see the [Stripe Documentation for Customers](https://stripe.com/docs/api/curl#customers).

_Note that all keywords must be spear-cased. This library will convert spear-cased keywords to snake-cased strings before the request is sent to Stripe._

#### Using the customer API

``` clojure
(ns my-app.core
  (:require [stripe-clj.customer :as customer]))
```

#### Creating a new customer

``` clojure
(customer/create)

;; or, with form parameters
(customer/create {:description "My first customer" :email "customer@first.org"})
```

#### Retrieve an existing customer

``` clojure
(customer/retrieve "cus_1234567890")
```

#### Update a customer

``` clojure
(customer/update "cus_1234567890" {:description "Gets a winning prize"})
```
#### Delete a customer

``` clojure
(customer/delete "cus_1234567890")
```
