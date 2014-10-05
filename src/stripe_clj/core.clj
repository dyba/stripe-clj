(ns stripe-clj.core
  (:require [stripe-clj.utils :refer :all]
            [stripe-clj.customer :as customer]
            [stripe-clj.recipient :as recipient]
            [stripe-clj.balance :as balance]
            [stripe-clj.subscription :as subscription]
            [stripe-clj.card :as card]
;;            [stripe-clj.refund :as refund]
;;            [stripe-clj.charge :as charge]
;;            [stripe-clj.event :as event]
            [stripe-clj.token :as token]
            [stripe-clj.account :as account]))

;; Customer API

(def create-customer customer/create)
(def retrieve-customer customer/retrieve)
(def delete-customer customer/delete)
(def update-customer customer/update)

;; Recipient API

(def create-recipient recipient/create)
(def retrieve-recipient recipient/retrieve)
(def update-recipient recipient/update)
(def delete-recipient recipient/delete)
(def list-all-recipients recipient/list-all)

;; Account API

(def retrieve-account account/retrieve)

;; Balance API

(def retrieve-balance balance/retrieve)
(def list-balance-history balance/list-history)
(def retrieve-balance-transaction balance/retrieve-transaction)

;; Subscription API

(def create-subscription subscription/create)
(def retrieve-subscription subscription/retrieve)
(def update-subscription subscription/update)
(def cancel-subscription subscription/cancel)
(def list-all-subscriptions subscription/list-all)

;; Card API

(def create-card card/create)
(def update-card card/update)
(def retrieve-card card/retrieve)
(def delete-card card/delete)

;; Token API

(def create-token token/create)
(def retrieve-token token/retrieve)

;; Charge API

;; (def create-charge charge/create)
;; (def update-charge charge/update)
;; (def retrieve-charge charge/retrieve)
;; (def capture-charge charge/capture)
;; (def list-all-charges charge/list-all)

;; Event API

;; (def retrieve-event event/retrieve)
;; (def list-all-events event/list-all)

;; Refund API

;; (def create-refund refund/create)
;; (def update-refund refund/update)
;; (def retrieve-refund refund/retrieve)
;; (def list-all-refunds refund/list-all)

