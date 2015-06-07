(ns light-show-worker-queue.queue-worker
  (require [light-show-worker-queue.push-adapter :as push]
           [light-show-worker-queue.party-builder :as party-builder]
           [cemerick.bandalore :as sqs]
           [clojure.data.json :as json]))

(def client (sqs/create-client))

(defn- reduce-excess-logs []
  (.setLevel (java.util.logging.Logger/getLogger "com.amazonaws")
             java.util.logging.Level/WARNING))

(def parse-message (comp json/read-str :body))

(defn- send-mobile-message [message-content]
  (push/send-push-message message-content))

(defn- join-party [name]
  (send-mobile-message (party-builder/build-party-for-user name)))

(defn- add-partygoer-to-list [token]
  (push/register-endpoint token))

(defn- redirect-job [parsed-message]
  (let [{:strs [name token]} parsed-message]
    (cond token (add-partygoer-to-list token)
          name (join-party name)
          :else (println (str "unknown message" parsed-message)))))

(defn- handle-message [raw-message]
  (redirect-job (parse-message raw-message)))

(defn- listen-forever [queue_url]
  (doall (map (sqs/deleting-consumer client handle-message)
               (sqs/polling-receive client queue_url :max-wait Long/MAX_VALUE))))

(defn start [queue_url]
  (do
    (reduce-excess-logs)
    (listen-forever queue_url)))
