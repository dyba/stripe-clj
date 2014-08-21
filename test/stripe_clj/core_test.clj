(ns stripe-clj.core-test
  (:use [midje.sweet]
        [clj-http.fake]
        [cheshire.core :only [generate-string parse-string]])
  (:require [stripe-clj.core :refer :all]
            [stripe-clj.customer :as customer]
            [stripe-clj.plan :as plan]
            [stripe-clj.card :as card]
            [stripe-clj.subscription :as subscription]
            [stripe-clj.utils :refer :all]
            [ring.middleware.params :refer (wrap-params)]
            [clojure.string :as s]
            [cheshire.generate :refer [add-encoder]])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

(defn wrap-fix-body [app]
  (fn [req]
    (letfn [(fix-body [body]
              (if (instance? StringEntity body)
                (.getContent body)
                body))]
      (app (update-in req [:body] fix-body)))))

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

(facts "Cards Endpoint"
  (let [exp-month 1
        exp-year "2016"]
    (with-fake-routes
      {#"https://api.stripe.com/v1/customers/(.+)/cards" (fn [req]
                                                           {:status 200
                                                            :headers {}
                                                            :body (generate-string
                                                                    {"exp_month" exp-month
                                                                     "exp_year" exp-year})})}
      (fact "creates a new card"
        (let [customer (customer/create)
              params {:card {:number "4242424242424242" :exp-month exp-month :exp-year exp-year}}
              card (card/create (:id customer) params)]
          (:exp_month card) => (:exp-month params)
          (:exp_year card) => (:exp-year params))))))

;; TODO: Implement Plans Endpoint Tests
(comment (facts "Plans Endpoint"
           (fact "creates a new plan"
             (let [params {:id "newbie" :amount 1000 :currency "usd" :interval "month" :name "Newbie Plan"}
                   plan (plan/create params)]
               (:id plan) => (:id params)
               (:amount plan) => (:amount params)
               (:currency plan) => (:currency params)
               (:name plan) => (:name params)))
           (fact "deletes an existing plan")))

;; TODO: Implement Subscriptions Endpoint Tests
(comment (facts "Subscriptions Endpoint"
           (fact "creates a new subscription"
             (let [customer-id (:id (customer/create))
                   plan-params {:amount 2000 :interval "month" :name "The Basic Plan" :currency "usd" :id "basic"}
                   plan (plan/create plan-params)
                   params {:plan (:id plan)}
                   subscription (subscription/create {:customer-id customer-id :form-params params})]
               (:plan subscription) => (:id plan-params)
               (:customer subscription) => customer-id))))
