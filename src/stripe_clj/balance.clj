;; # Using the balance API
;;
;;     (ns my-app.core
;;       (:require [stripe-clj.balance :as balance]))
;;
;; For detailed information about the Customers model, see the [Stripe Documentation for Balance](https://stripe.com/docs/api/curl#balance).
(ns stripe-clj.balance
  (:use [stripe-clj.utils :refer :all]))

;; ## Retrieve balance
;;
;;     (balance/retrieve)
;;
(defn retrieve
  "Retrieves the current account balance, based on the API key
   that was used to make the request."
  [& opts]
  (let [path "balance"]
    (api-request :get path opts)))

;; ## Retrieve a balance transaction
;;
;;     (balance/retrieve-transaction "txn_1234567890")
;;
(defn retrieve-transaction
  "Retrieves a balance transaction with the given ID."
  [transaction-id & opts]
  (let [path (str "balance/history/" transaction-id)]
    (api-request :get path opts)))

;; ## List balance history
;;
;;     (balance/list-history)
;;
;; You can also pass additional options:
;;
;;     (balance/list-history
;;       {:limit 3 :type "charge"})
;;
(defn list-history
  "Returns a list of transactions that have contributed to the
   Stripe account balance (includes charges, refunds, transfers,
   and so on). The transactions are returned in sorted order, with
   the most recent transactions appearing first."
  [& opts]
  (let [path "balance/history"]
    (api-request :get path opts)))
