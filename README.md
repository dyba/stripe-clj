# stripe-clj
[![Build Status](https://travis-ci.org/dyba/stripe-clj.svg?branch=master)](https://travis-ci.org/dyba/stripe-clj)

A Stripe API for Clojure

## Usage

The only model currently supported is Customers:

``` clojure
(ns my-app.core
  (:require [stripe-clj.customer :as customer]))

;; Set your API Key
(def *stripe-api-key* "sk_test_BQokikJOvBiI2HlWgH4olfQ2")

;; create a customer
(customer/create)

;; create a customer with form parameters
(customer/create {:description "My first customer" :email "customer@first.org"})

;; retrieve a customer
(customer/retrieve "cus_1234567890")

;; update a customer
(customer/update "cus_1234567890" {:description "Gets a winning prize"})

;; retrieve a customer
(customer/retrieve "cus_1234567890")

;; delete a customer
(customer/delete "cus_1234567890")
```

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
