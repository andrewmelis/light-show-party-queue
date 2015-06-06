(ns light-show-worker-queue.queue-worker
  (require [light-show-worker-queue.push-adapter :as push]
           [light-show-worker-queue.party-builder :as party-builder]
           [cemerick.bandalore :as sqs]
           [clojure.data.json :as json]))

(def client (sqs/create-client))

(def parse-message (comp json/read-str :body))

(defn- send-mobile-message [message-content]
  (push/send-push-message message-content))

(defn- handle-message [raw-message]
  (-> (parse-message raw-message)
      party-builder/build-party-for-user
      send-mobile-message))

(defn start-listening [queue_url]
  (doall (pmap (sqs/deleting-consumer client handle-message)
              (sqs/polling-receive client queue_url :max-wait Long/MAX_VALUE))))


