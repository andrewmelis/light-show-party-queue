(ns light-show-worker-queue.app-registration
  (require [amazonica.aws.sns :as push-service]))


;; sandbox
;; (def apns-sandbox-application "arn:aws:sns:us-east-1:405483072970:app/APNS_SANDBOX/Remote-Light-Show")

;; prod
(def apns-application "arn:aws:sns:us-east-1:405483072970:app/APNS/RemoteLightShowProd")

(defn- token->endpoint [token]
  (:endpoint-arn
   (try
     (push-service/create-platform-endpoint :platform-application-arn apns-application
                                            :token token)
     (catch Exception e
       (println (str "EXCEPTION " e))))))

(defn register-endpoint-to-topic [token topic-arn]
  (try
    (push-service/subscribe :topic-arn topic-arn
                            :protocol "application"
                            :endpoint (token->endpoint token))
    (catch Exception e
      (println (str "EXCEPTION " e)))))
