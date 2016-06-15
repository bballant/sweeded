(ns sweeded.nav
  (:require
    [sweeded.slides :refer [slides]]
    [sweeded.router :as r]
    [dommy.core :as dommy :refer-macros [sel sel1]]))

(def next-button (sel1 :#next-button))
(def prev-button (sel1 :#prev-button))

(defn inc-or-reset [n]
  (let [next-n (inc n)]
    (if (= next-n (count slides))
      0
      next-n)))

(defn dec-or-reset [n]
  (if (= n 0)
    (dec (count slides))
    (dec n)))

(defn click-handler [e page-fn]
  (r/nav! (r/page-path {:n (page-fn @r/current-page)})))

(defn next-click-handler [e]
  (click-handler e inc-or-reset))

(defn prev-click-handler [e]
  (click-handler e dec-or-reset))

(dommy/listen! next-button
               :click next-click-handler)

(dommy/listen! prev-button
               :click prev-click-handler)
