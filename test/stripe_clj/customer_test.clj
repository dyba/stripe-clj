(ns stripe-clj.customer-test
  (:use [midje.sweet]
        [stripe-clj.test-helper]
        [clj-http.fake]
        [stripe-clj.core]
        [cheshire.core :only [generate-string parse-string]]
        [cheshire.generate :refer [add-encoder]])
  (:require [stripe-clj.customer :as customer]
            [clojure.string :as s]
            [ring.middleware.params :refer (wrap-params)])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

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

(facts "Customers"
  (with-fake-routes
    {"https://api.stripe.com/v1/customers" {:post (wrap-fix-body
                                                    (fn [req]
                                                      {:status 200
                                                       :headers {}
                                                       :body (generate-string
                                                               {"id" "cus_4cHUOHJHFfcv4n"
                                                                "description" "Simple description"})}))}
     #"https://api.stripe.com/v1/customers/(.+)" {:delete (fn [req]
                                                            (let [customer-id (last (s/split (:uri req) #"/"))]
                                                              {:status 200
                                                               :headers {}
                                                               :body (generate-string {:deleted true :id customer-id})}))
                                                  :post (wrap-fix-body
                                                          (wrap-params
                                                            (fn [req]
                                                              (let [customer-id (last (s/split (:uri req) #"/"))
                                                                    params (:params req)]
                                                                {:status 201
                                                                 :headers {}
                                                                 :body (generate-string
                                                                         {:description (get params "description")})}))))
                                                  :get (fn [req]
                                                         (let [customer-id (last (s/split (:uri req) #"/"))]
                                                           {:status 200
                                                            :headers {}
                                                            :body (generate-string {:id customer-id})}))}}
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
        (:id customer) => customer-id))))
