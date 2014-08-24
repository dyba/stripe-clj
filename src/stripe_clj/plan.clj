(ns stripe-clj.plan
  (:use [stripe-clj.utils :refer :all]))

(defn create
  [params & opts]
  (let [path "plans"
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

(defn retrieve
  [plan-id & opts]
  (let [path (str "plans/" plan-id)]
    (api-request :get path opts)))

(defn update
  [plan-id params & opts]
  (let [path (str "plans/" plan-id)
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

(defn delete
  [plan-id & opts]
  (let [path (str "plans/" plan-id)]
    (api-request :delete path opts)))

(defn list-all
  ([] (list-all {}))
  ([params & opts]
     (let [path "plans"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :get path (merge form-params opts)))))

