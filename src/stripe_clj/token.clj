(ns stripe-clj.token
  (:require [stripe-clj.utils :refer :all]))

(defn create
  ([params & opts]
     (let [path "tokens"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :post path (merge form-params opts)))))

(defn retrieve
  [token-id & opts]
  (let [path (str "tokens/" token-id)]
    (api-request :get path opts)))
