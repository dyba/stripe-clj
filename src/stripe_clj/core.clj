(ns stripe-clj.core
  (:require [stripe-clj.utils :refer :all]
            [stripe-clj.customer :as customer]))

;; Alternative customer API

(def create-customer customer/create)
(def retrieve-customer customer/retrieve)
(def delete-customer customer/delete)
(def update-customer customer/update)
