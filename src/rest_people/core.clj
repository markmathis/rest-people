(ns rest-people.core
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
    (map str/trim (str/split record regex))))

(let [field-names [:last-name
                   :first-name
                   :gender
                   :favorite-color
                   :date-of-birth]]
  (defn parse-record [record]
    (let [field-values (split-record record)]
      (update (zipmap field-names field-values) :date-of-birth #(LocalDate/parse %)))))

(let [date-format (DateTimeFormatter/ofPattern "MM/dd/yyyy")]
  (defn record->str [{:keys [last-name first-name gender favorite-color date-of-birth]}]
    (let [date-str (. date-of-birth (format date-format))]
      (format "%1$s %2$s is a %3$s who was born on %4$s and whose favorite color is %5$s"
              first-name last-name gender date-str favorite-color))))

(defn display-records
  ([records key-fn]
   (display-records records key-fn compare))
  ([records key-fn comparator]
   (doseq [record (sort-by key-fn comparator records)]
     (println (record->str record)))
   (println "----")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [raw-records (read-record-files *command-line-args*)
        records (map parse-record raw-records)]
    (display-records records (juxt :gender :last-name))
    (display-records records :date-of-birth)
    (display-records records :last-name (comp - compare))))
