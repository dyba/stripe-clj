(ns stripe-clj.utils
  (:require [clojure.string :as str]
            [clj-http.client :as client]
            [cheshire.core :refer :all])
  (:use [clojure.walk :only [postwalk]]))

;; General utilities

(defn spear-case
  "Converts all _ characters in a string to -"
  [s]
  (str/replace s #"_" "-"))

(defn snake-case
  "Converts all - characters in a string to _"
  [s]
  (str/replace s #"-" "_"))

(defn str->spear-cased-keyword ;; can we create a general function str->keyword and then provide transformation options? i.e. spear-case, or snake-case?
  "Converts a string containing underscores to a keyword that contains hyphens"
  [s]
  (keyword (spear-case s)))

(defn spear-cased-keyword->str
  "Converts a spear-cased keyword to a string"
  [k]
  (snake-case (name k)))

(defn stripeify-keys
  [m]
  (let [f (fn [[k v]] (if (keyword? k) [(snake-case (name k)) v] [k v]))]
    (postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))

;; HTTP utilities

(def ^:dynamic *stripe-api-key* "sk_test_BQokikJOvBiI2HlWgH4olfQ2")

(def stripe-base-url "https://api.stripe.com")

(defn api-action
  [method path & [opts]]
  (let [url (str/join "/" [stripe-base-url "v1" path])]
    (try
      [true (client/request
              (merge {:method method
                      :url url
                      :basic-auth [(str *stripe-api-key* ":")]}
                opts))]
      (catch Exception e
        (let [error (-> e .getData :object :body)]
          [false error])))))

(defn api-request
  [method path & [opts]]
  (let [[valid? response] (api-action method path opts)]
    (if valid?
      (-> response :body (parse-string str->spear-cased-keyword))
      (parse-string response))))
