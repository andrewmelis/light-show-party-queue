(ns light-show-worker-queue.queue-worker
  (require [light-show-worker-queue.party-coordinator :as party-coordinator]
           [cemerick.bandalore :as queue]
           [clojure.data.json :as json]))

(defn- reduce-excess-logs []
  "mute extremely verbose aws logs"
  (.setLevel (java.util.logging.Logger/getLogger "com.amazonaws")
             java.util.logging.Level/WARNING))

(def client (queue/create-client))
(def forever Long/MAX_VALUE)

(def parse-message (comp json/read-str :body))

(defn- redirect-job [message]
  "parse body of messages from queue and redirect based on content"
  (let [{:strs [name token]} (parse-message message)]
    (cond token (party-coordinator/add-partygoer-to-list token)
          name  (party-coordinator/throw-party-for-user name)
          :else (println (str "unknown message" message)))))

(defn- listen-forever [queue_url]
  "poll queue forever and messages from queue on reception"
  (doall (map (queue/deleting-consumer client redirect-job)
              (queue/polling-receive client queue_url :max-wait forever))))

(defn start [queue_url]
  "main entry point for queue worker. starts service"
  (do
    (reduce-excess-logs)
    (listen-forever queue_url)))
