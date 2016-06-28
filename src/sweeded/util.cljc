(ns sweeded.util)

(defmacro for-indexed
  "This is basically just like map-indexed only
  slightly easier-to-read syntax when outputting hiccup"
  [[idx one all] & body]
  `(map-indexed (fn [~idx ~one] (do ~@body))
                ~all))

(defn r-key [& inputs]
  {:key (some->> inputs
                 (map #(if (number? %) (str %) (name %)))
                 (clojure.string/join "-"))})
                        

