(ns light-show-worker-queue.push-adapter
  (require [amazonica.aws.sns :as push-service]
           [clojure.data.json :as json]))

(def default-message-body {:default "error"})
(def silent-notification-property {:aps {:content-available 1}})

(def serialize json/write-str)

(defn- build-apns-body [message-content]
  "build a (silent) notification for Apple Push Notification Service"
  (serialize (merge silent-notification-property
                    message-content)))

(defn- build-notification-message-body [message-content]
  "builds a multi-platform notification. expects a map"
  (let [apns-body (build-apns-body message-content)]
    {:default      "error"
     :APNS         apns-body
     :APNS_SANDBOX apns-body}))

(defn- publish-notification [serialized-message-body topic-endpoint]
  "delivers a properly structured notification to subscribers of app topic"
  (try
    (push-service/publish :target-arn topic-endpoint
                          :message serialized-message-body
                          :message-structure "json")
    (catch Exception e
      (println (str "EXCEPTION " e)))))

(defn send-push-message [message-content topic-endpoint]
  "formats input content for delivery and publishes a notification"
  (-> (build-notification-message-body message-content)
      serialize
      (publish-notification topic-endpoint)))
