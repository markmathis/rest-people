(ns rest-people.record
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))

(defn read-file-into [target file]
  (with-open [reader (io/reader file)]
    (into target (line-seq reader))))

(defn read-record-files [files-coll]
  (reduce read-file-into [] files-coll))

(defn split-record [record]
  (let [regex (condp #(str/includes? %2 %1) record
                "," #","
                "|" #"\|"
                " " #" ")]
    (->> (str/split record regex)
         (map str/trim)
         (remove str/blank?))))

(defn parse-local-date [s]
  (LocalDate/parse s))

(let [field-names [:last-name
                   :first-name
                   :gender
                   :favorite-color
                   :date-of-birth]]
  (defn parse-record [record]
    (let [field-values (split-record record)]
      (update (zipmap field-names field-values) :date-of-birth parse-local-date))))

(let [date-format (DateTimeFormatter/ofPattern "MM/dd/yyyy")]
  (defn record->str [{:keys [last-name first-name gender favorite-color date-of-birth]}]
    (let [date-str (. date-of-birth (format date-format))]
      (format "%1$s %2$s is a %3$s who was born on %4$s and whose favorite color is %5$s"
              first-name last-name gender date-str favorite-color))))
