(ns stripe-clj.customer
  (:require [clojure.string :as str]
            [stripe-clj.utils :refer :all])
  (:use [clojure.walk :only [stringify-keys]]))

(defn create
  ([] (create {}))
  ([params & opts]
     (let [path "customers"
           form-params {:form-params (stringify-keys params)}]
       (api-request :post path (merge form-params opts)))))

(defn update
  [customer-id params & opts]
  (let [path (str "customers/" customer-id)
        form-params {:form-params (stringify-keys params)}]
    (api-request :post path (merge form-params opts))))

(defn retrieve
  [customer-id & opts]
  (let [path (str "customers/" customer-id)]
    (api-request :get path opts)))

(defn delete
  [customer-id & opts]
  (let [path (str "customers/" customer-id)]
    (api-request :delete path opts)))
