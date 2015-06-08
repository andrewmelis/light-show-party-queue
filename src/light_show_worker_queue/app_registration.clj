(ns light-show-worker-queue.app-registration
  (require [amazonica.aws.sns :as push-service]))

(def apns-sandbox-application "arn:aws:sns:us-east-1:405483072970:app/APNS_SANDBOX/Remote-Light-Show")

(defn- token->endpoint [token]
  (try
    (push-service/create-platform-endpoint :platform-application-arn apns-sandbox-application
                                           :token token)
    (catch Exception e
      (println (str "EXCEPTION " e)))))

(defn register-endpoint-to-topic [token topic-arn]
  (try
    (push-service/subscribe :topic-arn topic-arn
                            :protocol "application"
                            :endpoint (token->endpoint token))
    (catch Exception e
      (println (str "EXCEPTION " e)))))
