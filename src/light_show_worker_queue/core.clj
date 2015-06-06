(ns light-show-worker-queue.core
  (:require [light-show-worker-queue.queue-worker :as queue])
  (:gen-class))

(def light-show-queue "https://sqs.us-east-1.amazonaws.com/405483072970/remote-light-show")

(defn -main
  "start a queue that listens to the provided queue url indefinitely"
  [& args]
  (queue/start-listening light-show-queue))
