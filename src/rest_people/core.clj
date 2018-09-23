(ns rest-people.core
  (:require [rest-people
             [record :as record]
             [rest :as r]]))

(defn display-records
  ([records key-fn]
   (display-records records key-fn compare))
  ([records key-fn comparator]
   (when (seq records)
     (doseq [record (sort-by key-fn comparator records)]
       (println (record/record->str record)))
     (println "----"))))

(defn part-1 [records]
  (display-records records (juxt :gender :last-name))
  (display-records records :date-of-birth)
  (display-records records :last-name (comp - compare)))

(defn part-2 [records]
  (r/start-server records))

(defn -main
  "Read files passed in as commmand line arguments.
  Display file data sorted three ways, then start a webserver
  which serves a REST api to view and manipulate the data."
  [& args]
  (let [records (->> *command-line-args*
                     (apply record/read-record-files)
                     (map record/parse-record))]
    (part-1 records)
    (part-2 records)))
