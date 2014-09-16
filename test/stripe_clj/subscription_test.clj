(ns stripe-clj.subscription-test
  (:use [midje.sweet]
        [stripe-clj.test-helper]
        [clj-http.fake]
        [stripe-clj.core]
        [cheshire.core :only [generate-string parse-string]]
        [cheshire.generate :refer [add-encoder]])
  (:require [stripe-clj.subscription :as subscription]
            [clojure.string :as s]
            [ring.middleware.params :refer (wrap-params)])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

(facts "Subscriptions Endpoint"
  (with-fake-routes
    {#"https://api.stripe.com/v1/customers/(.+)/subscriptions" {:post (wrap-fix-body
                                                                        (fn [req]
                                                                          {:status 200
                                                                           :headers {}
                                                                           :body (generate-string
                                                                                   {"id" "sub_0123456789"
                                                                                    "plan" {"name" "Monthly"
                                                                                            "id" "basic"}
                                                                                    "customer" "cus_1234567890"})}))}
     #"https://api.stripe.com/v1/customers/(.+)/subscriptions/(.+)" {:delete (fn [req]
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
        (fact "creates a new subscription"
          (let [customer-id "cus_1234567890"
                params {:plan "basic"}
                subscription (subscription/create customer-id params)]
            (:id (:plan subscription)) => "basic"
            (:customer subscription) => customer-id))))
