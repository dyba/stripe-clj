(ns stripe-clj.card
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [cheshire.core :refer :all]
            [stripe-clj.utils :refer :all])
  (:use [clojure.walk :only [postwalk]]))

(def ^:dynamic *stripe-api-key* "sk_test_BQokikJOvBiI2HlWgH4olfQ2")
(def stripe-base-url "https://api.stripe.com")

(defn api-action
  [method path & [opts]]
  (let [url (str/join "/" [stripe-base-url "v1" path])]
    (client/request
      (merge {:method method :url url :basic-auth [(str *stripe-api-key* ":")]} opts))))

(defn api-request
  [method path & [opts]]
  (->
    (api-action method path opts)
    :body
    (parse-string str->spear-cased-keyword)))

(defn stripeify-keys
  [m]
  (let [f (fn [[k v]] (if (keyword? k) [(snake-case (name k)) v] [k v]))]
    (postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))

(defn create
  [customer-id params & opts]
  (let [path (str "customers/" customer-id "/cards")
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

(defn update
  [customer-id card-id params & opts]
  (let [path (str "customers/" customer-id "cards/" card-id)
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

(defn retrieve
  [customer-id card-id & opts]
  (let [path (str "customers/" customer-id "cards/" card-id)]
    (api-request :get path opts)))

(defn delete
  [customer-id card-id & opts]
  (let [path (str "customers/" customer-id "cards/" card-id)]
    (api-request :delete path opts)))

