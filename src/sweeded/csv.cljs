(ns sweeded.csv
  (:require cljsjs.papaparse))

(defn parse-csv [file]
  (js/Papa.parse file
                 (js-obj
                   "complete"
                   (fn [results]
                     (js/console.log "Finished" (.-data results)))
                   "error"
                   (fn [e f]
                     (js/console.log "Error" e f)))))

(defn read-single-file [e]
  (let [file (first (array-seq (.. e -target -files)))]
    (parse-csv file)))
