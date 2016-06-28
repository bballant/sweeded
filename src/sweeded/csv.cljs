(ns sweeded.csv
  (:require cljsjs.papaparse
            [sweeded.util :as u :refer-macros [for-indexed]]
            [sweeded.state :refer [csv-data]]))

(defn results-array->seq [results]
  (reduce (fn [acc row]
            (conj acc (array-seq row)))
          []
          (array-seq results)))

(defn parse-csv [file]
  (js/Papa.parse file
                 (js-obj
                   "complete"
                   (fn [results]
                     (js/console.log "Finished" (.-data results))
                     (reset! csv-data (results-array->seq (.-data results))))
                   "error"
                   (fn [e f]
                     (js/console.log "Error" e f)))))

(defn read-single-file [e]
  (let [file (first (array-seq (.. e -target -files)))]
    (parse-csv file)))

(defn csv-component []
  (when-let [csv-rows @csv-data]
    [:table.u-full-width
     [:thead
      (let [head-cols (first csv-rows)]
        [:tr
         (for-indexed [i c head-cols]
           [:th (u/r-key :h i) c])])]
     [:tbody
      (let [body-rows (rest csv-rows)]
        (for-indexed [i row body-rows]
          [:tr (u/r-key :r i)
           (for-indexed [j col row]
             [:td (u/r-key :d i j) col])]))]]))
