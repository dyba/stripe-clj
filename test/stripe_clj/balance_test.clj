(ns stripe-clj.balance-test
  (:use [midje.sweet]
        [stripe-clj.test-helper]
        [stripe-clj.utils]
        [clj-http.fake]
        [stripe-clj.core]
        [cheshire.core :only [generate-string parse-string]]
        [cheshire.generate :refer [add-encoder]])
  (:require [stripe-clj.balance :as balance]
            [clojure.string :as s]
            [ring.middleware.params :refer (wrap-params)])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

(facts "Balance"
  (let [bal {:pending [{:amount 32301180 :currency "usd"}]
             :available [{:amount -104 :currency "usd"}]
             :livemode false
             :object "balance"}
        txn {:id "txn_1234567890"
             :object "balance_transaction"
             :amount 500
             :currency "usd"
             :net 455
             :type "charge"
             :fee 45
             :fee-details [{:amount 45
                            :currency "usd"
                            :type "stripe_fee"
                            :description "Stripe processing fee"
                            :application nil}]
             :source "ch_1234567890"
             :description nil}
        history {:object "list"
                 :url "/v1/balance/history"
                 :has-more false
                 :data [txn]}]
    (with-fake-routes
      {"https://api.stripe.com/v1/balance" {:get (wrap-fix-body
                                                   (fn [req]
                                                     {:status 200
                                                      :headers {}
                                                      :body (generate-string (stripeify-keys bal))}))}
       #"https://api.stripe.com/v1/balance/history/(.+)" {:get (wrap-fix-body
                                                                 (fn [req]
                                                                   {:status 200
                                                                    :headers {}
                                                                    :body (generate-string (stripeify-keys txn))}))}
       "https://api.stripe.com/v1/balance/history" {:get (wrap-fix-body
                                                           (fn [req]
                                                             {:status 200
                                                              :headers {}
                                                              :body (generate-string (stripeify-keys history))}))}}
      (fact "retrieves a balance"
        (balance/retrieve) => bal)
      (fact "retrieves a balance history"
        (balance/list-history) => history)
      (fact "retrieves a balance transaction"
        (balance/retrieve-transaction "txn_1234567890") => txn))))
