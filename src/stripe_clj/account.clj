(ns stripe-clj.account
  (:use [stripe-clj.utils :refer :all]))

(defn retrieve
  "Retrieves the details of the account, based on the API key that was used to make the request."
  [& opts]
  (let [path "account"]
    (api-request :get path opts)))
