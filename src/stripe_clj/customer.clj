;; # Using the customer API
;;
;;     (ns my-app.core
;;       (:require [stripe-clj.customer :as customer]))
;;
;; For detailed information about the Customers model, see the [Stripe Documentation for Customers](https://stripe.com/docs/api/curl#customers).
(ns stripe-clj.customer
  (:require [clojure.string :as str]
            [stripe-clj.utils :refer :all]))

;; ## Creating a new customer
;; If you want to create a customer with no attributes set:
;;
;;     (customer/create)
;; 
;; You can also set attributes:
;;
;;     (customer/create
;;       {:description "My first customer"
;;        :email "customer@first.org"})
;;
(defn create
  "Creates a new customer object."
  ([] (create {}))
  ([params & opts]
     (let [path "customers"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :post path (merge form-params opts)))))

;; ## Updating a customer
;;
;;     (customer/update "cus_1234567890"
;;       {:description "Gets a winning prize!"})
;;
(defn update
  "Updates the specified customer by setting the values of the
  parameters passed. Any parameters not provided will be left
  unchanged. For example, if you pass the card parameter, that becomes
  the customer's active card to be used for all charges in the
  future. When you update a customer to a new valid card: for each of
  the customer's current subscriptions, if the subscription is in the
  `past_due` state, then the latest unpaid, unclosed invoice for the
  subscription will be retried (note that this retry will not count as
  an automatic retry, and will not affect the next regularly scheduled
  payment for the invoice). (Note also that no invoices pertaining to
  subscriptions in the `unpaid` state, or invoices pertaining to
  canceled subscriptions, will be retried as a result of updating the
  customer's card.)  This request accepts mostly the same arguments as
  the customer creation call.  map of the attributes to update."
  [customer-id params & opts]
  (let [path (str "customers/" customer-id)
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

;; ## Retrieving an existing customer
;;
;;     (customer/retrieve "cus_1234567890")
;;
(defn retrieve
  "Retrieves the details of an existing customer. You need only supply
  the unique customer identifier that was return upon customer
  creation."
  [customer-id & opts]
  (let [path (str "customers/" customer-id)]
    (api-request :get path opts)))

;; ## Deleting a customer
;;
;;     (customer/delete "cus_1234567890")
;;
(defn delete
  "Permanently deletes a customer. It cannot be undone. Also
  immediately cancels any active subscriptions on the customer."
  [customer-id & opts]
  (let [path (str "customers/" customer-id)]
    (api-request :delete path opts)))

;; ## List all customers
;;
;;     (customer/list-all)
;;
(defn list-all
  "Returns a list of your customers. The customers are returned sorted
  by creation date, with the most recently created customers appearing
  first."
  ([] (list-all {}))
  ([params & opts]
     (let [path "customers"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :get path (merge form-params opts)))))
