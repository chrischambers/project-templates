;; ---------------------------------------------------------
;; Mulog Global Context and Custom Publisher
;;
;; - set event log global context
;; - tap publisher for use with Portal and other tap sources
;; - publish all mulog events to Portal tap source
;; ---------------------------------------------------------

(ns mulog-events
  (:require
   [com.brunobonacci.mulog        :as μ]
   [com.brunobonacci.mulog.buffer :as μbuffer]))

(μ/set-global-context!
 {:app-name "{{main/ns}}",
  :version "0.1.0",
  :env "dev"})

(deftype TapPublisher
         [buffer transform]
  com.brunobonacci.mulog.publisher.PPublisher
  (agent-buffer [_] buffer)
  (publish-delay [_] 200)
  (publish [_ buffer]
    (doseq [item (transform (map second (μbuffer/items buffer)))]
      (tap> item))
    (μbuffer/clear buffer)))

#_{:clj-kondo/ignore [:unused-private-var]}
(defn ^:private tap-events
  [{:keys [transform] :as _config}]
  (TapPublisher. (μbuffer/agent-buffer 10000) (or transform identity)))

(def tap-publisher
  "Start mulog custom tap publisher to send all events to Portal
  and other tap sources.

  `mulog-tap-publisher` to stop publisher"
  (μ/start-publisher!
   {:type :custom, :fqn-function "mulog-events/tap-events"}))

#_{:clj-kondo/ignore [:unused-public-var]}
(defn stop
  "Stop mulog tap publisher to ensure multiple publishers are not started
 Recommended before using `(restart)` or evaluating the `user` namespace"
  []
  tap-publisher)

;; Example mulog event message
;; (μ/log ::dev-user-ns :message "Example event message" :ns (ns-publics *ns*))
;; ---------------------------------------------------------
