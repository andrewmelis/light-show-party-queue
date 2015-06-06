(ns light-show-worker-queue.push-adapter
  (require [amazonica.aws.sns :as sns]
           [clojure.data.json :as json]))

;; andrew's phone arn during dev
(def party-topic-arn "arn:aws:sns:us-east-1:405483072970:endpoint/APNS_SANDBOX/Remote-Light-Show/3f91e295-c42a-305f-a8fa-71bad0e44591")

(def default-party-message-body {:default "wassup"})

(def silent-notification-property {:aps {:content-available 1}})

(defn- apns-dictionary [party-map]
  (merge silent-notification-property
         party-map))

(defn- apns-message-body [party-map]
  {:APNS_SANDBOX (json/write-str (apns-dictionary party-map))})

(defn- build-party-message-body [party-map]
  (merge default-party-message-body
         (apns-message-body party-map)))

(defn- publish-party [serialized-message-body]
  (sns/publish :target-arn party-topic-arn
               :message serialized-message-body
               :message-structure "json"))

(defn send-push-message [message-content]
  (-> (build-party-message-body message-content)
      json/write-str
      publish-party))

