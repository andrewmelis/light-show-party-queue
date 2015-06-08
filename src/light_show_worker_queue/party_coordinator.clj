(ns light-show-worker-queue.party-coordinator
  (require [light-show-worker-queue.push-adapter :as push]
           [light-show-worker-queue.app-registration :as registration]))

(def topic-arn "arn:aws:sns:us-east-1:405483072970:remote-light-show")

(defn- light-show-rgb []
  "builds light show rgb string. each value x is 0 < x < 1"
  (str (rand) "," (rand) "," (rand)))

(defn- light-show-info []
  {:rgb (light-show-rgb)})

(defn- build-party-for-user [user-info]
  {:party (merge (light-show-info)
                 {:name user-info})})

(defn throw-party-for-user [name]
  "sends a party notification to clients through notification topic"
  (push/send-push-message (build-party-for-user name) topic-arn))

(defn add-partygoer-to-list [token]
  "registers user for notifications to application"
  (registration/register-endpoint-to-topic token topic-arn))
