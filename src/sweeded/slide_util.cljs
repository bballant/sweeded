(ns sweeded.slide-util
  (:require
    [dommy.core :as dommy :refer-macros [sel sel1]]
    [markdown.core :as md]
    [sweeded.slides :refer [slides]]))

(def slide-div (sel1 :#slide))

(defn draw-slide [n]
  (let [current-slide (get slides n)]
    (dommy/set-html! slide-div
                     (str "<h2>" (:title current-slide) "</h2>"
                          (md/md->html (:body current-slide))))))
