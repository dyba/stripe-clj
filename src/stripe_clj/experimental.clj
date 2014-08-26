(ns stripe-clj.experimental
  (:require [clojure.string :as str])
  (:use [stripe-clj.utils :refer :all]
        [cheshire.core :only [parse-string]]))

;; Consider returning a function so that an api request acts
;; like a factory that can create new requests based on the method and opts
;; we pass
(defn api-request-new
  [method & [opts]]
  (fn [path & params]
    (let [[valid? response] (api-action method path (merge params opts))]
      (if valid?
        (-> response :body (parse-string str->spear-cased-keyword))
        (parse-string response)))))

(defn GET
  [path & opts]
  ((api-request-new :get opts) path))

;; You can test this out now in the REPL:
;; 
;; (GET "balance") =>
;; {:pending [{:amount 27718305, :currency "usd"}], :available [{:amount -312, :currency "usd"}], :livemode false, :object "balance"}

(defn POST
  [path params & opts]
  ((api-request-new :post opts) path params))

(defn DELETE
  [path & opts]
  ((api-request-new :delete opts) path))

(defn pathmap-builder
  [& kvs]
  (apply merge (map (fn [[k v]]
                      (cond
                        (keyword? k) {k v}
                        (vector? k) (into {} (for [op k]
                                               [op v]))
                        :else {}))
                 kvs)))

(def card-paths
  (pathmap-builder
    [:create (fn [{:keys [customer-id]}]
               (str/join "/" ["customers" customer-id "cards"]))]
    [[:delete :retrieve :update] (fn [{:keys [customer-id card-id]}]
                                   (str/join "/" ["customers" customer-id "cards" card-id]))]))
