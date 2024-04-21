(ns portal
  (:require
   [portal.api :as inspect]))

(def instance
  "Open portal window if no portal sessions have been created.
   A portal session is created when opening a portal window"
  (or (seq (inspect/sessions))
      (inspect/open {:portal.colors/theme :portal.colors/zerodark})))

;; Add portal as tapsource (add to clojure.core/tapset)
(add-tap #'portal.api/submit)
;; ---------------------------------------------------------
