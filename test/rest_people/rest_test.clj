(ns rest-people.rest-test
  (:require [clojure.test :refer :all]
            [rest-people.rest :refer :all]
            [cheshire.core :as json]
            [ring.mock.request :as mock])
  (:import (java.time LocalDate)))

(deftest test-routes-empty
  (let [routes (record-routes [])]
    (is (= ((juxt :status :body) (routes (mock/request :get "/records/gender")))
           [200 "[]"]))
    (is (= ((juxt :status :body) (routes (mock/request :get "/records/name")))
           [200 "[]"]))
    (is (= ((juxt :status :body) (routes (mock/request :get "/records/birthdate")))
           [200 "[]"]))
    (is (= ((juxt :status :body) (routes (mock/request :get "/invalid/path")))
           [404 "[]"]))))

(defn response->last-names [response]
  (->> (:body response)
       (json/parse-string)
       (map (fn [x] (get x "last-name")))))

(deftest test-routes-with-data
  (let [routes (record-routes [{:last-name "li" :first-name "jo" :gender "female"
                                :favorite-color "burgundy" :date-of-birth (LocalDate/parse  "1974-08-05")}
                               {:last-name "egar" :first-name "olen" :gender "male"
                                :favorite-color "gray" :date-of-birth (LocalDate/parse "1936-12-12")}
                               {:last-name "carow" :first-name "deborah" :gender "female"
                                :favorite-color "copper" :date-of-birth (LocalDate/parse "1988-12-09")}])
        gender-response (->> (routes (mock/request :get "/records/gender"))
                             (response->last-names))
        birthdate-response (->> (routes (mock/request :get "/records/birthdate"))
                                (response->last-names))
        name-response (->> (routes (mock/request :get "/records/name"))
                           (response->last-names))]
    (is (= gender-response ["li" "carow" "egar"]))
    (is (= birthdate-response ["egar" "li" "carow"]))
    (is (= name-response ["carow" "egar" "li"]))
    (is (= ((juxt :status :body) (routes (mock/request :get "/invalid/path")))
           [404 "[]"]))))

(deftest test-post
  (let [routes (record-routes [])]
    (is (= (:body (routes (-> (mock/request :post "/records")
                              (mock/body "li, jo, female, burgundy, 1974-08-05"))))
           "{\"last-name\":\"li\",\"first-name\":\"jo\",\"gender\":\"female\",\"favorite-color\":\"burgundy\",\"date-of-birth\":\"1974-08-05\"}"
           ))
    (is (= (->> (routes (mock/request :get "/records/name"))
                (response->last-names))
           ["li"]))
    (is (= (:status (routes (-> (mock/request :post "/records")
                                (mock/body "carow | deborah | female | copper | 1988-12-09"))))
           200))
    (is (= (->> (routes (mock/request :get "/records/name"))
                (response->last-names))
           ["carow" "li"]))))
