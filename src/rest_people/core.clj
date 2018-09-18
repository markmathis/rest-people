(ns rest-people.core
  (:require [rest-people.record :as record]))

(defn display-records
  ([records key-fn]
   (display-records records key-fn compare))
  ([records key-fn comparator]
   (doseq [record (sort-by key-fn comparator records)]
     (println (record/record->str record)))
   (println "----")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [raw-records (record/read-record-files *command-line-args*)
        records (map record/parse-record raw-records)]
    (display-records records (juxt :gender :last-name))
    (display-records records :date-of-birth)
    (display-records records :last-name (comp - compare))))
