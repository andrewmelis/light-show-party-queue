(ns light-show-worker-queue.core
  (:gen-class))

(def light-show-queue "https://sqs.us-east-1.amazonaws.com/405483072970/remote-light-show")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
