;; # Using the event API
;;
;;     (ns my-app.core
;;       (:require [stripe-clj.balance :as balance]))
;;
;; For detailed information about the Customers model, see the [Stripe Documentation for Balance](https://stripe.com/docs/api/curl#balance).
(ns stripe-clj.event
  (:use [stripe-clj.utils :refer :all]))

;; ## Retrieve an event
;;
;;     (event/retrieve "evt_14WJ2X2eZvKYlo2ChqnPabod")
;;
(defn retrieve
  "Retrieves the details of an event. Supply the unique identifier of
  the event, which you might have received in a webhook."
  [event-id & opts]
  (let [path (str "events/" event-id)]
    (api-request :get path opts)))

;; ## List all events
;;
;;     (event/list-all)
;;
;; You can also add options:
;;
;;     (event/list-all
;;       {:limit 5 :type "account.updated"})
;;
(defn list-all
  ([] (list-all {}))
  ([params & opts]
     (let [path "events"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :get path (merge form-params opts)))))
