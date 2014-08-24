# Tokens

For detailed information about the Tokens model, see the [Stripe Documentation for Tokens](https://stripe.com/docs/api/curl#tokens).

_Note that all keywords must be spear-cased. This library will convert spear-cased keywords to snake-cased strings before the request is sent to Stripe._

#### Using the tokens API

``` clojure
(ns my-app.core
  (:require [stripe-clj.token :as token]))
```

#### Creating a card token

``` clojure
(token/create {:card {:number "4242424242424242" :exp-month "12" :exp-year "2015" :cvc "123"}})
```

#### Creating a bank account token

``` clojure
(token/create {:bank-account {:country "US" :routing-number "110000000" :account-number "000123456789"}})
```

#### Retrieve an existing token

``` clojure
(token/retrieve "tok_14UmnY2eZvKYlo2CSTozPYmm")
```
