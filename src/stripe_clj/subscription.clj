;; # Using the subscription API
;;
;;     (ns my-app.core
;;       (:require [stripe-clj.subscription :as subscription]))
;;
;; For detailed information about the Subscription model, see the [Stripe Documentation for Subscriptions](https://stripe.com/docs/api/curl#subscriptions).
(ns stripe-clj.subscription
  (:use [stripe-clj.utils :refer :all]))

;; ## Creating a new subscription
;; If you want to create a subscription for an existing customer:
;;
;;     (subscription/create "cus_1234567890" "basic")
;;
;; The first parameter is the id of the customer, the second is 
;; the id of the plan the customer is subscribing to.
;;
(defn create
  "Creates a new subscription on an existing customer."
  [customer-id params & opts]
  (let [path (str "customers/" customer-id "/subscriptions")
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

;; ## Retrieving a subscription
;;
;; If you want to retrieve a subscription belonging to an existing
;; customer:
;;
;;     (subscription/retrieve "cus_1234567890" "sub_1234567890")
;;
;; The first parameter is the id of the customer, the second is the
;; id of the subscription.
;;
(defn retrieve
  "By default, you can see the 10 most recent active subscriptions
  stored on a customer directly on the customer object, but you can
  also retrieve details about a specific active subscription for a
  customer."
  [customer-id subscription-id params & opts]
  (let [path (str "customers/" customer-id "/subscriptions/" subscription-id)]
    (api-request :get path opts)))

;; ## Updating a subscription
;;
;; You can update an existing subscription for a customer like so:
;;
;;     (subscription/update "cus_1234567890" "sub_1234567890"
;;       {:plan "gold"})
;;
;; The first parameter is the id of the customer, the second is the
;; id of the subscription, and the last parameter is a map of the
;; attributes of the subscription you want to update.
(defn update
  "Updates an existing subscription on a customer to match the
  specified parameters. When changing plans or quantities, we will
  optionally prorate the price we charge next month to make up for any
  price changes."
  [customer-id subscription-id params & opts]
  (let [path (str "customers/" customer-id "/subscriptions/" subscription-id)
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

;; ## Canceling a subscription
;;
;; To cancel a subscription:
;;
;;     (subscription/cancel "cus_1234567890" "sub_1234567890")
;;
(defn cancel
  "Cancels a customer's subscription. If you set the at_period_end
  parameter to true, the subscription will remain active until the end
  of the period, at which point it will be canceled and not
  renewed. By default, the subscription is terminated immediately. In
  either case, the customer will not be charged again for the
  subscription. Note, however, that any pending invoice items that
  you've created will still be charged for at the end of the period
  unless manually deleted. If you've set the subscription to cancel at
  period end, any pending prorations will also be left in place and
  collected at the end of the period, but if the subscription is set
  to cancel immediately, pending prorations will be removed.  By
  default, all unpaid invoices for the customer will be closed upon
  subscription cancellation. We do this in order to prevent unexpected
  payment retries once the customer has canceled a
  subscription. However, you can reopen the invoices manually after
  subscription cancellation to have us proceed with automatic retries,
  or you could even re-attempt payment yourself on all unpaid invoices
  before allowing the customer to cancel the subscription at all."
  [customer-id subscription-id & opts]
  (let [path (str "customers/" customer-id "/subscriptions/" subscription-id)]
    (api-request :delete path opts)))

;; ## Listing all active subscriptions
;;
;; You can see a list of the customer's active subscriptions:
;;
;;     (subscription/list-all "cus_1234567890")
;;
;; You can also apply list options:
;;
;;     (subscription/list-all "cus_1234567890"
;;       {:limit 3})
;;
(defn list-all
  "You can see a list of the customer's active subscriptions. Note
  that the 10 most recent active subscriptions are always available by
  default on the customer object. If you need more than those 10, you
  can use the limit and starting_after parameters to page through
  additional subscriptions."
  [customer-id & opts]
  (let [path (str "customers/" customer-id "/subscriptions" )]
    (api-request :get path opts)))
