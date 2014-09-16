(ns stripe-clj.customer-test
  (:use [midje.sweet]
        [stripe-clj.test-helper]
        [clj-http.fake]
        [stripe-clj.core]
        [cheshire.core :only [generate-string parse-string]]
        [cheshire.generate :refer [add-encoder]])
  (:require [stripe-clj.recipient :as recipient]
            [clojure.string :as s]
            [ring.middleware.params :refer (wrap-params)])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

(facts "Recipients"
  (with-fake-routes
    {"https://api.stripe.com/v1/recipients" {:post (wrap-fix-body
                                                     (fn [req]
                                                       {:status 200
                                                        :headers {}
                                                        :body (generate-string
                                                                {"id" "rp_01234567890"
                                                                 "description" "Simple description"})}))
                                             :get (wrap-fix-body
                                                    (fn [req]
                                                      {:status 200
                                                       :headers {}
                                                       :body (generate-string
                                                               {:object "list"
                                                                :data
                                                                [{"id" "rp_12341234"
                                                                  "object" "recipient"}
                                                                 {"id" "rp_6789067890"
                                                                  "object" "recipient"}]})}))}
     #"https://api.stripe.com/v1/recipients/(.+)" {:delete (fn [req]
                                                             (let [recipient-id (last (s/split (:uri req) #"/"))]
                                                               {:status 200
                                                                :headers {}
                                                                :body (generate-string {:deleted true :id recipient-id})}))
                                                  :post (wrap-fix-body
                                                          (wrap-params
                                                            (fn [req]
                                                              (let [recipient-id (last (s/split (:uri req) #"/"))
                                                                    params (:params req)]
                                                                {:status 201
                                                                 :headers {}
                                                                 :body (generate-string
                                                                         {:description (get params "description")})}))))
                                                  :get (fn [req]
                                                         (let [recipient-id (last (s/split (:uri req) #"/"))]
                                                           {:status 200
                                                            :headers {}
                                                            :body (generate-string {:id recipient-id})}))}}
    (fact "creates a new recipient"
      (let [recipient-id (:id (recipient/create))]
        recipient-id => (re-find #"rp_.*" recipient-id)))
    (fact "creates a new recipient with parameters"
      (let [params {:description "Simple description" :email "simple.description@example.com"}
            recipient (recipient/create params)]
        (:description recipient) => (:description params)))
    (fact "deletes an existing recipient"
      (let [recipient-id (:id (recipient/create))]
        (:id (recipient/delete recipient-id)) => recipient-id))
    (fact "updates an existing recipient"
      (let [recipient-id (:id (recipient/create))
            params {:description "Hello new recipient!"}
            recipient (recipient/update recipient-id params)]
        (:description recipient) => (:description params)))
    (fact "retrieves an existing recipient"
      (let [recipient-id (:id (recipient/create))
            recipient (recipient/retrieve recipient-id)]
        (:id recipient) => recipient-id))
    (fact "lists all recipients"
      (let [recipients (recipient/list-all)]
        (count (:data recipients)) => 2))))
