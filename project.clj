(defproject light-show-worker-queue "0.1.0-SNAPSHOT"
  :description "work push notification jobs off a queue"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [amazonica "0.3.22"]
                 [com.cemerick/bandalore "0.0.6"]]
  :main ^:skip-aot light-show-worker-queue.core
  :min-lein-version "2.0.0"
  :uberjar-name "light-show-worker-queue.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
