(defproject stripe-clj "0.0.1"
  :description "Stripe API for Clojure"
  :url "http://github.com/dyba/stripe-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.stripe/stripe-java "1.15.1"]
                 [clj-http "1.0.0"]
                 [cheshire "5.3.1"]
                 [prismatic/schema "0.2.6"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]
                                  [clj-http-fake "0.7.8"]
                                  [ring/ring-core "1.3.0"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-marginalia "0.7.1"]]}})
