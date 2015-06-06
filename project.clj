(defproject light-show-worker-queue "0.1.0-SNAPSHOT"
  :description "work push notification jobs off a queue"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [amazonica "0.3.22"]
                 [com.cemerick/bandalore "0.0.6"]]
  :main ^:skip-aot light-show-worker-queue.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
