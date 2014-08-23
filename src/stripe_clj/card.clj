(ns stripe-clj.card
  (:require [clojure.string :as str]
            [stripe-clj.utils :refer :all]))

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

