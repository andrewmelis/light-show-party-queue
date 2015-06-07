(ns light-show-worker-queue.party-builder)

(defn- light-show-rgb []
  (str (rand) "," (rand) "," (rand) "," (rand)))

(defn- light-show-info []
  {:rgb (light-show-rgb)})

(defn build-party-for-user [user-info]
  {:party (merge (light-show-info)
                 {:name user-info})})
  
