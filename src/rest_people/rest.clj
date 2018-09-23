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

(defn ->json
  "Attempt to create json string from argument"
  [x]
  (json/generate-string x))

(def empty-response (->json {}))

(defn get-handler
  "Creates a handler that responds to a GET request by returning
  records as json in the order specified by sort-key"
  [records sort-key]
  (fn [request]
    (->> @records
         (sort-by sort-key)
         ->json)))

(defn post-handler
  "Creates a handler that responds to a POST request by parsing the
  body of the request as a record and adding it to the known records."
  [records]
  (fn [request]
    (let [body (slurp (:body request))
          record (rec/parse-record body)]
      (swap! records conj record)
      (->json record))))

(defn record-routes
  "The routes that are handled by the REST api."
  [records]
  (let-routes [records (atom records)]
    (context "/records" []
             (POST "/" [] (post-handler records))
             (GET "/gender" [] (get-handler records :gender))
             (GET "/birthdate" [] (get-handler records :date-of-birth))
             (GET "/name" [] (get-handler records (juxt :last-name :first-name))))
    (route/not-found empty-response)))

(defn start-server
  "Starts webserver that will handle REST requests using records as the backing data."
  [records]
  (ring-jetty/run-jetty (record-routes records) {:port 3000 :join? false}))
