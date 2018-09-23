(ns rest-people.record
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))

(defn read-file-into
  "Open file with clojure.java.io/reader and read by line into target collection"
  [target file]
  (with-open [reader (io/reader file)]
    (into target (line-seq reader))))

(defn read-record-files
  "Reads lines of all files into a vector"
  [& files]
  (reduce read-file-into [] files))

(defn split-record
  "Takes a string with fields delimited by either a space, comma, or pipe
  and splits it into a seq of fields."
  [record]
  (let [regex (condp #(str/includes? %2 %1) record
                "," #","
                "|" #"\|"
                " " #" ")]
    (->> (str/split record regex)
         (map str/trim)
         (remove str/blank?))))

(defn parse-local-date
  "Parses a date of the form 2007-12-03 from a string."
  [s]
  (LocalDate/parse s))

(def field-names [:last-name :first-name :gender :favorite-color :date-of-birth])

(defn parse-record
  "create a record given a line of text of the form:
  \"lastname, firstname, gender, favorite-color, date-of-birth\"
  where the ', ' can be any of ', ', ' | ' or ' '."
  [record]
  (let [field-values (split-record record)
        record (zipmap field-names field-values)]
    (update record :date-of-birth parse-local-date)))

(def date-format (DateTimeFormatter/ofPattern "MM/dd/yyyy"))

(defn record->str
  "Create a sentence using the values from record"
  [{:keys [last-name first-name gender favorite-color date-of-birth]}]
  (let [date-str (. date-of-birth (format date-format))]
    (format "%1$s %2$s is a %3$s who was born on %4$s and whose favorite color is %5$s"
            first-name last-name gender date-str favorite-color)))
