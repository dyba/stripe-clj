(ns stripe-clj.core-test
  (:use [midje.sweet]
        [clj-http.fake]
        [stripe-clj.test-helper]
        [cheshire.core :only [generate-string parse-string]])
  (:require [stripe-clj.core :refer :all]
            [stripe-clj.customer :as customer]
            [stripe-clj.plan :as plan]
            [stripe-clj.card :as card]
            [stripe-clj.account :as account]
            [stripe-clj.token :as token]
            [stripe-clj.subscription :as subscription]
            [stripe-clj.utils :refer :all]
            [ring.middleware.params :refer (wrap-params)]
            [clojure.string :as s]
            [cheshire.generate :refer [add-encoder]])
  (:import [org.apache.http.entity StringEntity]))

(add-encoder StringEntity
  (fn [c jsonGenerator]
    (.writeString jsonGenerator (slurp (.getContent c)))))

(facts "The core API"
  (fact "the stripe API key defaults to a test key"
    *stripe-api-key* => "sk_test_BQokikJOvBiI2HlWgH4olfQ2"))

(facts "Tokens Endpoint"
  (with-fake-routes
    {"https://api.stripe.com/v1/tokens" {:post (fn [req]
                                                 {:status 200
                                                  :headers {}
                                                  :body (generate-string
                                                          {:card {:id "card_1234567890"}})})}
     #"https://api.stripe.com/v1/tokens/(.+)" {:get (fn [req]
                                                     {:status 200
                                                      :headers {}
                                                      :body (generate-string
                                                              {:id "tok_1234567890"})})}}
    (fact "creates a new card token"
      (let [params {:card {:number "4242424242424242" :exp-month "12" :exp-year "2015" :cvc "123"}}
            token (token/create params)]
        (re-find #"card_.*" (get-in token [:card :id])) => "card_1234567890"))
    (fact "retrieves an existing token"
      (let [token-id "tok_1234567890"]
        (:id (token/retrieve "tok_1234567890")) => token-id))))

(facts "Account"
  (with-fake-routes
    {"https://api.stripe.com/v1/account" {:get (fn [req]
                                                 {:status 200
                                                  :headers {}
                                                  :body (generate-string
                                                          {:id "acct_1032D82eZvKYlo2C"
                                                           :email "site@stripe.com"
                                                           :display-name "Stripe.com"})})}}
    (fact "retrieves an account"
      (:id (account/retrieve)) => "acct_1032D82eZvKYlo2C")))

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

(facts "Plans Endpoint"
  (with-fake-routes
    {"https://api.stripe.com/v1/plans" {:post (fn [req]
                                                {:status 200
                                                 :headers {}
                                                 :body (generate-string
                                                         {:interval "month"
                                                          :name "Newbie Plan"
                                                          :amount 1000
                                                          :currency "usd"
                                                          :id "newbie"})})
                                        :get (fn [req]
                                               {:status 200
                                                :headers {}
                                                :body (generate-string
                                                        {:data [{:name "Monthly"
                                                                 :interval "month"
                                                                 :id "test1"}
                                                                {:name "Weekly"
                                                                 :interval "week"
                                                                 :id "test2"}]})})}
     #"https://api.stripe.com/v1/plans/(.+)" {:get (fn [req]
                                                     {:status 200
                                                      :headers {}
                                                      :body (generate-string
                                                              {:id "newbie"})})
                                              :post (wrap-fix-body
                                                      (wrap-params
                                                        (fn [req]
                                                          {:status 200
                                                           :headers {}
                                                           :body (generate-string
                                                                   {:id "newbie"
                                                                    :name "New plan name"})})))
                                              :delete (fn [req]
                                                        {:status 200
                                                         :headers {}
                                                         :body (generate-string
                                                                 {:deleted true
                                                                  :id "newbie"})})}}
    (fact "creates a new plan"
      (let [params {:id "newbie" :amount 1000 :currency "usd" :interval "month" :name "Newbie Plan"}
            plan (plan/create params)]
        (:id plan) => (:id params)
        (:amount plan) => (:amount params)
        (:currency plan) => (:currency params)
        (:name plan) => (:name params)))
    (fact "retrieves a plan"
      (let [plan-id "newbie"
            response (plan/retrieve plan-id)]
        (:id response) => plan-id))
    (fact "updates a plan"
      (let [params {:name "New plan name"}
            plan-id "newbie"
            response (plan/update plan-id params)]
        (:name response) => (:name params)))
    (fact "lists all plans"
      (let [response (plan/list-all)]
        (count (:data response))) => 2)
    (fact "deletes an existing plan"
      (let [plan-id "newbie"
            response (plan/delete plan-id)]
        (:id response) => plan-id))))
