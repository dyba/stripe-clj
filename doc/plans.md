# Plans

For detailed information about the Plan model, see the [Stripe Documentation for Plans](https://stripe.com/docs/api/curl#plans).

_Note that all keywords must be spear-cased. This library will convert spear-cased keywords to snake-cased strings before the request is sent to Stripe._

#### Using the plan API

``` clojure
(ns my-app.core
  (:require [stripe-clj.plan :as plan]))
```

#### Creating a plan

``` clojure
(plan/create {:amount 2000 :interval "month" :id "gold" :currency "usd" :name "Amazing Gold Plan"})
```

#### Retrieve a plan

``` clojure
(plan/retrieve "gold")
```

#### Update a plan

``` clojure
(plan/update "gold" {:name "The Finest Plan"})
```
#### Delete a plan

``` clojure
(plan/delete "gold")
```

#### List all plans

``` clojure
(plan/list-all)
```
