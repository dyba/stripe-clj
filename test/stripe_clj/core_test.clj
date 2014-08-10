(ns stripe-clj.core-test
  (:use midje.sweet)
  (:require [stripe-clj.core :refer :all]
            [stripe-clj.customer :as customer]
            [stripe-clj.plan :as plan]
            [stripe-clj.subscription :as subscription]))

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

(facts "Customers Endpoint - New API"
  (fact "creates a new customer"
    (let [customer-id (:id (customer/create))]
      customer-id => (re-find #"cus_.*" customer-id)))
  (fact "creates a new customer with parameters"
    (let [params {:description "Simple description" :email "simple.description@example.com"}
          customer (customer/create params)]
      (:description customer) => (:description params)))
  (fact "deletes an existing customer"
    (let [customer-id (:id (customer/create))]
      (:id (customer/delete customer-id)) => customer-id))
  (fact "updates an existing customer"
    (let [customer-id (:id (customer/create))
          params {:description "Hello new customer!"}
          customer (customer/update customer-id params)]
      (:description customer) => (:description params)))
  (fact "retrieves an existing customer"
    (let [customer-id (:id (customer/create))
          customer (customer/retrieve customer-id)]
      (:id customer) => customer-id)))

(comment (facts "Subscriptions Endpoint"
           (fact "creates a new subscription"
             (let [customer-id (:id (customer/create))
                   plan-params {:amount 2000 :interval "month" :name "The Basic Plan" :currency "usd" :id "basic"}
                   plan (plan/create plan-params)
                   params {:plan (:id plan)}
                   subscription (subscription/create {:customer-id customer-id :form-params params})]
               (:plan subscription) => (:id plan-params)
               (:customer subscription) => customer-id))))
