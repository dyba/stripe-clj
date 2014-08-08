(ns stripe-clj.core-test
  (:use midje.sweet)
  (:require [stripe-clj.core :refer :all]))

(facts "The core API"
  (fact "the stripe API key defaults to a test key"
    *stripe-api-key* => "sk_test_BQokikJOvBiI2HlWgH4olfQ2"))

(facts "Customers Endpoint"
  (fact "creates a new customer"
    (let [customer-id (:id (create-customer))]
      customer-id => (re-find #"^cus_.*" customer-id)))
  (fact "deletes an existing customer"
    (let [customer-id (:id (create-customer))]
      (:id (delete-customer customer-id)) => customer-id))
  (fact "updates an existing customer"
    (let [customer-id (:id (create-customer))
          params {"description" "Hello new customer!"}]
      (:description (update-customer customer-id params)) => (get params "description")))
  (fact "retrieves an existing customer"
    (let [customer-id (:id (create-customer))]
      (:id (retrieve-customer customer-id)) => customer-id)))
