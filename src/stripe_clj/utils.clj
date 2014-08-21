(ns stripe-clj.utils
  (:require [clojure.string :as str]))

(defn spear-case
  "Converts all _ characters in a string to -"
  [s]
  (str/replace s #"_" "-"))

(defn snake-case
  "Converts all - characters in a string to _"
  [s]
  (str/replace s #"-" "_"))

(defn str->spear-cased-keyword ;; can we create a general function str->keyword and then provide transformation options? i.e. spear-case, or snake-case?
  "Converts a string containing underscores to a keyword that contains hyphens"
  [s]
  (keyword (spear-case s)))

(defn spear-cased-keyword->str
  "Converts a spear-cased keyword to a string"
  [k]
  (snake-case (name k)))

(comment (defn delete-operation
           [path]
           (fn [& opts]
             (api-request :delete path opts))))
