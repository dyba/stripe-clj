(ns stripe-clj.recipient
  (:use [stripe-clj.utils :refer :all]))

(defn create
  "Creates a new recipient object and verifies both the recipient's
  identity and, if provided, the recipient's bank account information
  or debit card.

  Returns a recipient object if the call succeeded. The returned
  object will have the identity verification and bank account
  validation results.  If a bank account has been attached to the
  recipient, the returned recipient object will have an active_account
  attribute containing the accounts's details."
  ([] (create {}))
  ([params & opts]
     (let [path "recipients"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :post path (merge form-params opts)))))

(defn update
  "Updates the specified recipient by setting the values of the
  parameters passed. Any parameters not provided will be left
  unchanged.  If you update the name or tax ID, the identity
  verification will automatically be rerun. If you update the bank
  account, the bank account validation will automatically be rerun."
  [recipient-id params & opts]
  (let [path (str "recipients/" recipient-id)
        form-params {:form-params (stripeify-keys params)}]
    (api-request :post path (merge form-params opts))))

(defn retrieve
  "Retrieves the details of an existing recipient. You need only
  supply the unique recipient identifier that was returned upon
  recipient creation."
  [recipient-id & opts]
  (let [path (str "recipients/" recipient-id)]
    (api-request :get path opts)))

(defn delete
  "Permanently deletes a recipient. It cannot be undone."
  [recipient-id & opts]
  (let [path (str "recipients/" recipient-id)]
    (api-request :delete path opts)))

(defn list-all
  "Returns a list of your recipients. The recipients are returned sorted by creation date, with the most recently created recipient appearing first."
  ([] (list-all {}))
  ([params & opts]
     (let [path "recipients"
           form-params {:form-params (stripeify-keys params)}]
       (api-request :get path (merge form-params opts)))))
