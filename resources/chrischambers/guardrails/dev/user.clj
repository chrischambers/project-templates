(ns user
  "Tools for REPL Driven Development"
  (:require
   [clojure.pprint         :as pp]
   [clojure.tools.namespace.repl :as namespace]
   [com.brunobonacci.mulog :as μ]
   [mulog-events]
   [portal.api]))

;; (alter-var-root #'*print-meta* (constantly true))
(alter-var-root #'*warn-on-reflection* (constantly true))

;; ---------------------------------------------------------
;; Help

(println "---------------------------------------------------------")
(println "Loading custom user namespace tools...")
(println "---------------------------------------------------------")

(defn help
  []
  (println "---------------------------------------------------------")
  (println "Namesapece Management:")
  (println "(namespace/refresh)        ; refresh all changed namespaces")
  (println "(namespace/refresh-all)    ; refresh all namespaces")
  (println)
  (println "Hotload libraries:         ; Clojure 1.12.x")
  (println "(add-lib 'library-name)")
  (println "(add-libs '{domain/library-name {:mvn/version \"v1.2.3\"}})")
  (println "(sync-deps)                ; load dependencies from deps.edn")
  (println)
  (println "Portal:")
  (println "(portal!)                  ; launch new portal instance")
  (println "@@portal                   ; return current selection from portal")
  (println)
  (println "(help)                     ; print help text")
  (println "---------------------------------------------------------"))

(help)

;; End of Help
;; ---------------------------------------------------------

;; Avoid reloading `dev` code:
;; code in `dev` directory should be evaluated if changed to reload into REPL
(println
 "Set REPL refresh directories to "
 (namespace/set-refresh-dirs "src" "resources"))
;; ---------------------------------------------------------

;; ---------------------------------------------------------
;; Mulog event logging:
;; `mulog-publisher` namespace used to launch tap> events to tap-source
;; (portal)
;; `mulog-events` namespace sets mulog global context for all events

;; Example mulog event message
(μ/log ::dev-user-ns
       :message "Example event from user namespace"
       :ns      (ns-publics *ns*))
;; ---------------------------------------------------------


(defmacro jit
  [sym]
  `(requiring-resolve '~sym))

(def portal (atom nil))
(def portal-default-options {:theme :portal.colors/material-ui})

(defn portal!
  "Open a Portal window and register a tap handler for it. The result can be
  treated like an atom."
  [& {:as opts}]
  ;; Portal is both an IPersistentMap and an IDeref, which confuses pprint.
  (prefer-method @(jit pp/simple-dispatch)
                 clojure.lang.IPersistentMap
                 clojure.lang.IDeref)
  (let [options (merge portal-default-options opts)
        p       ((jit portal.api/open) @portal options)]
    (reset! portal p)
    (add-tap (jit portal.api/submit))
    p))

(comment
  (portal!)
  ;; -------------------------------------------------------------------------
)
