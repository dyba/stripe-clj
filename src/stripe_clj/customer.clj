(ns stripe-clj.customer
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [cheshire.core :refer :all]
            [stripe-clj.utils :refer :all]))

(def ^:dynamic *stripe-api-key* "sk_test_BQokikJOvBiI2HlWgH4olfQ2")
(def stripe-base-url "https://api.stripe.com")

(defn api-request
  [method path & [opts]]
  (let [url (str/join "/" [stripe-base-url "v1" path])]
    (-> (client/request
          (merge {:method method :url url :basic-auth [(str *stripe-api-key* ":")]} opts))
      :body
      (parse-string str->spear-cased-keyword))))

(defn stringify-keys
  [m]
  (let [stringify-key (fn [m] {(name (first m)) (second m)})]
    (apply merge (map stringify-key m))))

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
