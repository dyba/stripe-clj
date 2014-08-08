(ns stripe-clj.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
            [clojure.string :as str]
            [stripe-clj.utils :refer :all]))

(def ^:dynamic *stripe-token* nil)
(def ^:dynamic *stripe-api-key* "sk_test_BQokikJOvBiI2HlWgH4olfQ2")
(def stripe-base-url "https://api.stripe.com")

(defn api-request
  [method path & [opts]]
  (let [url (str/join "/" [stripe-base-url "v1" path])]
    (-> (client/request
          (merge {:method method :url url :basic-auth [(str *stripe-api-key* ":")]} opts))
      :body
      (parse-string str->spear-cased-keyword))))

(defn path
  [ks & args]
  (let [mapping {:customers
                 {:create (fn [] "customers")
                  :retrieve #(str/join "/" ["customers" %])
                  :delete #(str/join "/" ["customers" %])
                  :update #(str/join "/" ["customers" %])
                  :all (fn [] "customers")}
                 :cards
                 {:create #(str/join "/" ["customers" %1 "cards"])
                  :retrieve #(str/join "/" ["customers" %1 "cards" %2])
                  :update #(str/join "/" ["customers" %1 "cards" %2])
                  }}]
    (if args
      (apply (get-in mapping ks) args)
      (apply (get-in mapping ks) []))))

(defn create-card
  [customer-id card-or-token]
  (api-request
    :post
    {:body (generate-string card-or-token)})) ;; fix this

(defn create-customer
  []
  (api-request
    :post
    (path [:customers :create])))

(defn retrieve-customer
  [customer-id]
  (api-request
    :get
    (path [:customers :retrieve] customer-id)))

(defn delete-customer
  [customer-id]
  (api-request
    :delete
    (path [:customers :delete] customer-id)))

(defn update-customer
  [customer-id params]
  (api-request
    :post
    (path [:customers :update] customer-id)
    {:form-params params}))

(defn all-customers
  []
  (api-request
    :get
    (path [:customers :all])))

(defn retrieve-card
  [customer-id card-id]
  (api-request
    :get
    (path [:cards :retrieve] customer-id card-id)))

(defn update-card
  [customer-id card-id params]
  (api-request
    :post
    (path [:cards :update] customer-id card-id)
    {:form-params params}))

(defn create-card
  [customer-id params]
  (api-request
    :post
    (path [:cards :create] customer-id)
    {:body (generate-string params)}))
