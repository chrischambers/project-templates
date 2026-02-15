((clojure-mode . ((cider-preferred-build-tool . clojure-cli)
                  (clojure-guardrails-enabled . t)
                  (cider-clojure-cli-aliases . ":test/env:dev:guardrails:flowstorm")
                  (cider-jack-in-nrepl-middlewares . ("refactor-nrepl.middleware/wrap-refactor"
                                                      "cider.nrepl/cider-middleware"
                                                      "flow-storm.nrepl.middleware/wrap-flow-storm")))))
