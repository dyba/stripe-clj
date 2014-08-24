# Cards

For detailed information about the Cards model, see the [Stripe Documentation for Cards](https://stripe.com/docs/api/curl#cards).

_Note that all keywords must be spear-cased. This library will convert spear-cased keywords to snake-cased strings before the request is sent to Stripe._

#### Using the cards API

``` clojure
(ns my-app.core
  (:require [stripe-clj.card :as card]))
```

#### Creating a new card

``` clojure
;; using a token
(card/create {:card "tok_01234567890"})

;; or, creating a card from a dictionary containing the user's credit card details
(card/create {:card {:number "4242424242424242" :exp-year "2015" :exp-month "12" :cvc "123"}})
```

#### Retrieve an existing card

``` clojure
;; the first argument is the customer id, the second the card id
(card/retrieve "cus_4e5KvBKTHzO3Vx" "card_14Unz12eZvKYlo2CTbVDYGcx")
```

#### Update a card

``` clojure
(card/update "cus_4e5KvBKTHzO3Vx" "card_14Unz12eZvKYlo2CTbVDYGcx" {:name "Jane Austen"})
```

#### Delete a card

``` clojure
(card/delete "cus_4e5KvBKTHzO3Vx" "card_14Unz12eZvKYlo2CTbVDYGcx")
```

#### List all cards

``` clojure
(card/list-all "cus_4e5KvBKTHzO3Vx")

;; or with options
(card/list-all "cus_4e5KvBKTHzO3Vx" {:limit 3})
```
