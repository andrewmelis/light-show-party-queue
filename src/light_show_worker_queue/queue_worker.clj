(ns light-show-worker-queue.queue-worker
  (require [light-show-worker-queue.push-adapter :as push]
           [cemerick.bandalore :as sqs]
           [clojure.data.json :as json]))

(def client (sqs/create-client))

(def parse-message (comp json/read-str :body))

(defn- light-show-rgb []
  (str (rand) "," (rand) "," (rand) "," (rand)))

(defn- light-show-info []
  {:rgb (light-show-rgb)})

(def user-info {:name "claudia"})

(defn- build-party-map [user-info]
  ;; {:party (merge (light-show-info)
  ;;                {:name (get user-info "name")})})
  {:party (merge (light-show-info)
                 user-info)})

(defn- send-mobile-message [message-content]
  (push/send-push-message message-content))

(defn- handle-message [raw-message]
  (-> (parse-message raw-message)
      build-party-map
      send-mobile-message))

(defn start-listening [queue_url]
  (doall (map (sqs/deleting-consumer client handle-message)
              (sqs/polling-receive client queue_url :max-wait 3000))))

(def light-show-queue "https://sqs.us-east-1.amazonaws.com/405483072970/remote-light-show")
(start-listening light-show-queue)

  
