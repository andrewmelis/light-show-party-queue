(ns light-show-worker-queue.push-adapter
  (require [amazonica.aws.sns :as sns]
           [clojure.data.json :as json]))

;; andrew's phone arn during dev
;; (def party-topic-arn "arn:aws:sns:us-east-1:405483072970:endpoint/APNS_SANDBOX/Remote-Light-Show/3f91e295-c42a-305f-a8fa-71bad0e44591")

(def party-topic-arn "arn:aws:sns:us-east-1:405483072970:remote-light-show")

(def default-party-message-body {:default "error"})

(def silent-notification-property {:aps {:content-available 1}})

(def serialize json/write-str)

(defn- apns-dictionary [party-map]
  (merge silent-notification-property
         party-map))

(defn- apns-message-body [party-map]
  {:APNS_SANDBOX (serialize (apns-dictionary party-map))})

(defn- build-party-message-body [party-map]
  (merge default-party-message-body
         (apns-message-body party-map)))

(defn- publish-party [serialized-message-body]
  (sns/publish :target-arn party-topic-arn
               :message serialized-message-body
               :message-structure "json"))

(defn send-push-message [message-content]
  (-> (build-party-message-body message-content)
      serialize
      publish-party))

;;; registration stuff

(def apns-sandbox-application "arn:aws:sns:us-east-1:405483072970:app/APNS_SANDBOX/Remote-Light-Show")

(defn- token->endpoint [token]
  (sns/create-platform-endpoint :platform-application-arn apns-sandbox-application
                                :token token))

(defn register-endpoint [token]
  (sns/subscribe :topic-arn party-topic-arn
                 :protocol "application"
                 :endpoint (token->endpoint token)))
