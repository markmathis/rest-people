(ns rest-people.rest
  (:require [rest-people.record :as rec]
            [cheshire
             [core :as json]
             [generate]]
            [compojure
             [core :refer [context let-routes GET POST]]
             [route :as route]]
            [ring.adapter.jetty :as ring-jetty]
            [ring.util.response :as response]))

(cheshire.generate/add-encoder java.time.LocalDate
                               (fn [c jsonGenerator]
                                 (.writeString jsonGenerator (.toString c))))

(defn ->json [x]
  (json/generate-string x))

(def empty-response (->json {}))

(defn get-handler [records sort-key]
  (fn [request]
    (->> @records
         (sort-by sort-key)
         ->json)))

(defn post-handler [records]
  (fn [request]
    (let [body (slurp (:body request))
          record (rec/parse-record body)]
      (swap! records conj record)
      (->json record))))

(defn record-routes [records]
  (let-routes [records (atom records)]
    (context "/records" []
             (POST "/" [] (post-handler records))
             (GET "/gender" [] (get-handler records :gender))
             (GET "/birthdate" [] (get-handler records :date-of-birth))
             (GET "/name" [] (get-handler records (juxt :last-name :first-name))))
    (route/not-found empty-response)))

(defn start-server [records]
  (ring-jetty/run-jetty (record-routes records) {:port 3000}))
