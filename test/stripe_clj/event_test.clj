(ns stripe-clj.event-test
  (:use [midje.sweet]
        [stripe-clj.test-helper]
        [stripe-clj.utils]
        [clj-http.fake]
        [stripe-clj.core]
        [cheshire.core :only [generate-string parse-string]]
        [cheshire.generate :refer [add-encoder]])
  (:require [stripe-clj.event :as event]
            [clojure.string :as s]
            [ring.middleware.params :refer (wrap-params)])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

(facts "Events"
  (let [evt {:id "evt_1234567890"
             :livemode false
             :data
             {:object
              {:id "ch_1234567890"
               :object "charge"
               :currency "usd"
               :card
               {:id "card_1234567890"
                :object "card"}}}}
        evts {:object "list"
              :url "/v1/events"
              :has-more false
              :data [evt]}]
    (with-fake-routes
      {#"https://api.stripe.com/v1/events/(.+)" {:get (wrap-fix-body
                                                        (fn [req]
                                                          {:status 200
                                                           :headers {}
                                                           :body (generate-string evt)}))}
       "https://api.stripe.com/v1/events" {:get (wrap-fix-body
                                                  (fn [req]
                                                    {:status 200
                                                     :headers {}
                                                     :body (generate-string evts)}))}}
      (fact "retrieves an event"
        (event/retrieve "evt_1234567890") => evt)
      (fact "lists all events"
        (event/list-all) => evts))))
