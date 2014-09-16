(ns stripe-clj.test-helper
  (:use [midje.sweet]
        [clj-http.fake]
        [cheshire.core :only [generate-string parse-string]]
        [cheshire.generate :refer [add-encoder]])
  (:import [org.apache.http.entity StringEntity]))

(defn wrap-fix-body [app]
  (fn [req]
    (letfn [(fix-body [body]
              (if (instance? StringEntity body)
                (.getContent body)
                body))]
      (app (update-in req [:body] fix-body)))))
